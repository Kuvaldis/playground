package kuvaldis.play.citrus;

import spark.Spark;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpServer {

    public static void main(String[] args) {
        Spark.get("/hello", (request, response) -> {
//            final HttpURLConnection connection =
//                    (HttpURLConnection) new URL("http://localhost:4568/greet").openConnection();
//            connection.setRequestMethod("GET");
//            final StringBuilder result = new StringBuilder();
//            final BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//            String line;
//            while ((line = reader.readLine()) != null) {
//                result.append(line);
//            }
//            reader.close();
//            return result.toString() + ", " + request.headers("Header-Name") + "!";
            return "Hello, " + request.headers("Header-Name") + "!";
        });
    }
}
