package kuvaldis.play.ideaplugin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.ui.DialogBuilder;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;

public class TestAction extends AnAction {

    public TestAction() {
        super("Test Action");
    }

    @Override
    public void actionPerformed(final AnActionEvent event) {
        Messages.showInfoMessage(event.getData(PlatformDataKeys.PROJECT), "Test! Test! Test!", "Hello Test");
    }
}
