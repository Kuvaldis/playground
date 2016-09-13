package kuvaldis.play.mavenplugin;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Goal which prints passed name
 */
// You may use also: @goal print-name @phase package
@Mojo(name = "print-name", defaultPhase = LifecyclePhase.PACKAGE)
public class MyMojo extends AbstractMojo {

    // or use old way definitions like so: '@parameter expression="${print.name}"'
    @Parameter(name = "name", property = "print.name")
    private String name;

    @Parameter(name = "times", defaultValue = "1")
    private int times;

    public void execute() throws MojoExecutionException {
        for (int i = 0; i < times; i++) {
            getLog().info("Hello, " + name + "!");
        }
    }
}
