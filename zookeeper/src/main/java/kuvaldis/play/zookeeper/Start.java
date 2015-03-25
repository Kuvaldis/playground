package kuvaldis.play.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.CuratorTempFramework;
import org.apache.curator.framework.api.CuratorEventType;
import org.apache.curator.framework.recipes.locks.InterProcessSemaphoreMutex;
import org.apache.curator.framework.recipes.shared.SharedValue;
import org.apache.curator.framework.recipes.shared.SharedValueListener;
import org.apache.curator.framework.recipes.shared.SharedValueReader;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.retry.RetryOneTime;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static java.util.Optional.ofNullable;

public class Start {

    private static final List<String> ALL_PROPERTIES = asList(
            "count", "db.url", "path.to.narnia"
    );
    private static final List<String> SHARED_PROPERTIES = asList(
            "count"
    );
    private static final String CONNECT_STRING = "localhost:2181";
    private static final int RETRY_INTERVAL = 1000;
    private static final int LOOKUP_CLIENT_TIMEOUT = 1000;
    private static final String ENVIRONMENT = "dev";
    private static final String CONFIG = "conf";
    private static final String CONFIG_FULL_PATH = String.format("/%s/%s", ENVIRONMENT, CONFIG);
    private static final int CONNECTION_TIMEOUT = 15000;
    private static final int SESSION_TIMEOUT = 60000;
    private static final int LOCK_TIME = 1000;
    private static final String SERVICE_DISCOVERY_BASE_PATH = "services";
    private static final String SERVICE_NAME = "zookeeper-play";

    private CuratorFramework client;
    private ServiceInstance<UUID> serviceInstance;
    private ServiceDiscovery<UUID> serviceDiscovery;
    private final UUID uuid = UUID.randomUUID();

    public static void main(String[] args) throws IOException {
        final Start instance = new Start();
        instance.init();
        instance.processCommands();
    }

    private void processCommands() throws IOException {
        final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            System.out.print("Enter command: ");
            final String command = reader.readLine();
            if ("stop".equals(command)) {
                break;
            } else {
                final String[] commandParts = command.split("[\\s]+");
                final String commandName = commandParts[0];
                if ("set".equals(commandName)) {
                    if (commandParts.length != 3) {
                        System.out.println(String.format("Error using command set. Should be: set <property> <value>"));
                        continue;
                    }
                    setProperty(commandParts[1], commandParts[2]);
                    System.out.println(String.format("Property '%s' is set to value '%s'", commandParts[1], commandParts[2]));
                } else if ("get".equals(commandName)) {
                    if (commandParts.length != 2) {
                        System.out.println("Error using command get. Should be: get <property>");
                        continue;
                    }
                    System.out.println(String.format("Property '%s' value is '%s'", commandParts[1], getProperty(commandParts[1])));
                } else if ("instances".equals(commandName)){
                    printInstances();
                } else {
                    System.out.println("Incorrect command");
                }
            }
        }
    }

    private void printInstances() {
        try {
            final Collection<ServiceInstance<UUID>> serviceInstances = serviceDiscovery.queryForInstances(SERVICE_NAME);
            serviceInstances.stream().forEach(this::printInstance);
        } catch (Exception e) {
            processException(e);
        }
    }

    private void printInstance(final ServiceInstance<UUID> serviceInstance) {
        System.out.println(asList(serviceInstance.getId().equals(this.serviceInstance.getId()) ? "This instance" : "Other instance",
                "(id = ", serviceInstance.getId(), ", customUuid = ", serviceInstance.getPayload().toString(), ")").stream()
                .collect(Collectors.joining(" ")));
    }

    private void init() {
        initClient();
        initConfig();
        initListeners();
        initDiscovery();
        checkConfigPath();
    }

    private void initListeners() {
        for (String property : SHARED_PROPERTIES) {
            final SharedValue sharedValue = new SharedValue(client, propertyPath(property), "".getBytes());
            sharedValue.getListenable().addListener(new SharedValueListener() {
                @Override
                public void valueHasChanged(SharedValueReader sharedValue, byte[] newValue) throws Exception {
                    System.out.println(String.format("Property '%s' has been changed to '%s'", property,
                            ofNullable(newValue).map(String::new).orElse(null)));
                }

                @Override
                public void stateChanged(CuratorFramework client, ConnectionState newState) { }
            });
            try {
                sharedValue.start();
            } catch (Exception e) {
                System.out.println(String.format("Failed to set up shared propery '%s'", property));
            }
        }
    }

    private void initClient() {
        client = CuratorFrameworkFactory.builder()
                .connectString(CONNECT_STRING)
                .connectionTimeoutMs(CONNECTION_TIMEOUT)
                .sessionTimeoutMs(SESSION_TIMEOUT)
                .retryPolicy(new RetryOneTime(RETRY_INTERVAL))
                .build();
        client.getCuratorListenable().addListener((client1, event) -> {
            if (CuratorEventType.SET_DATA.equals(event.getType())) {
                ALL_PROPERTIES.stream()
                        .filter(property -> propertyPath(property).equals(event.getPath()))
                        .findFirst()
                        .ifPresent(property -> System.out.println(String.format(
                                "Property '%s' has been set up to '%s'", property, ofNullable(event.getData()))));
            }
        });
        client.start();
    }

    private void initDiscovery() {
        serviceDiscovery = serviceDiscovery();
        serviceInstance = serviceInstance();
        try {
            serviceDiscovery.registerService(serviceInstance);
            serviceDiscovery.start();
        } catch (Exception e) {
            processException(e);
        }
    }

    private ServiceDiscovery<UUID> serviceDiscovery() {
        return ServiceDiscoveryBuilder.builder(UUID.class)
                .basePath(SERVICE_DISCOVERY_BASE_PATH)
                .client(client)
                .build();
    }

    private ServiceInstance<UUID> serviceInstance() {
        try {
            return ServiceInstance.<UUID>builder()
                    .name(SERVICE_NAME)
                    .payload(uuid)
                    .build();
        } catch (Exception e) {
            return processException(e);
        }
    }

    private ServiceInstance<UUID> processException(Exception e) {
        throw new RuntimeException("Something is wrong", e);
    }

    private boolean configPathExists() throws Exception {
        return client.checkExists().forPath(CONFIG_FULL_PATH) != null;
    }

    private void initConfig() {
        InterProcessSemaphoreMutex lock = null;
        try {
            if (!configPathExists()) {
                lock = new InterProcessSemaphoreMutex(client, "/");
                lock.acquire(LOCK_TIME, TimeUnit.MILLISECONDS);
                if (!configPathExists()) {
                    // non transactional
                    client.create().creatingParentsIfNeeded().forPath(CONFIG_FULL_PATH, null);
                    for (String property : ALL_PROPERTIES) {
                        client.create().creatingParentsIfNeeded().forPath(propertyPath(property), null);
                    }
                }
            }
        } catch (Exception e) {
            processException(e);
        } finally {
            if (lock != null) {
                try {
                    lock.release();
                } catch (Exception e) {
                    System.out.println("Can't release lock");
                }
            }
        }

    }

    private void checkConfigPath() {
        final CuratorTempFramework lookupClient = CuratorFrameworkFactory.builder()
                .connectString(CONNECT_STRING)
                .retryPolicy(new RetryOneTime(RETRY_INTERVAL))
                .buildTemp(LOOKUP_CLIENT_TIMEOUT, TimeUnit.MILLISECONDS);
        try {
            lookupClient.inTransaction().check().forPath(CONFIG_FULL_PATH).and().commit();
        } catch (Exception e) {
            processException(e);
        }
    }

    private String getProperty(final String property) {
        try {
            return ofNullable(client.getData().forPath(propertyPath(property)))
                    .map(String::new)
                    .orElse(null);
        } catch (Exception e) {
            System.out.println(String.format("Can't read property '%s'", property));
            e.printStackTrace();
            return "";
        }
    }

    private String propertyPath(String property) {
        return String.format("%s/%s", CONFIG_FULL_PATH, property.trim().replaceAll("\\.", "/"));
    }

    private void setProperty(final String property, final String value) {
        try {
            client.setData().forPath(propertyPath(property), value.getBytes());
        } catch (Exception e) {
            System.out.println(String.format("Can't write value '%s' for property '%s'", value, property));
            e.printStackTrace();
        }
    }
}
