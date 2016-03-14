package kuvaldis.play.dropwizard;

import org.h2.tools.Server;

import java.sql.SQLException;

/**
 * H2 tcp and web server starter class
 */
public class H2Starter {
    public static void main(String[] args) throws SQLException {
        Server.createTcpServer("-tcpAllowOthers").start();
        Server.createWebServer("-webAllowOthers").start();
    }
}
