package kuvaldis.play.ideaplugin;

import com.intellij.openapi.components.ApplicationComponent;
import org.jetbrains.annotations.NotNull;

public class TestApplicationComponentImpl implements ApplicationComponent, TestApplicationComponent {
    @Override
    public void initComponent() {
        System.out.println("Init test application component");
    }

    @Override
    public void disposeComponent() {
        System.out.println("Init test application component");
    }

    @NotNull
    @Override
    public String getComponentName() {
        return "TestApplicationComponent";
    }
}
