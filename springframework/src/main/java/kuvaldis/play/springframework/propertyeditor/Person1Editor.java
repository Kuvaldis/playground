package kuvaldis.play.springframework.propertyeditor;

import java.beans.PropertyEditorSupport;

public class Person1Editor extends PropertyEditorSupport {

    @Override
    public void setAsText(final String text) throws IllegalArgumentException {
        final String[] parts = text.split(" ");
        setValue(new Person1(parts[0], parts[1]));
    }
}
