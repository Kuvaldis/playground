package kuvaldis.play.picocontainer;

public class ServiceWithConfig implements Service {

    private final String dataBaseUrl;

    public ServiceWithConfig(String dataBaseUrl) {
        this.dataBaseUrl = dataBaseUrl;
    }

    @Override
    public String call() {
        return dataBaseUrl;
    }
}
