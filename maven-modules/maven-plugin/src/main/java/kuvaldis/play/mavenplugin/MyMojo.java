package kuvaldis.play.mavenplugin;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Goal which prints passed name
 *
 * @goal print-name
 * @phase package
 */
public class MyMojo extends AbstractMojo {

    /**
     * @parameter expression="${print.name}"
     */
    private String name;

    /**
     * @parameter default-value="1"
     */
    private int times;

    public void execute() throws MojoExecutionException {
        for (int i = 0; i < times; i++) {
            System.out.println("Hello, " + name + "!");
        }
    }
}
