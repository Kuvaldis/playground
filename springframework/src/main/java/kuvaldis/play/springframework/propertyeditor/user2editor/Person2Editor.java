package kuvaldis.play.springframework.propertyeditor.user2editor;

import kuvaldis.play.springframework.propertyeditor.Person2;

import java.beans.PropertyEditorSupport;

public class Person2Editor extends PropertyEditorSupport {

    @Override
    public void setAsText(final String text) throws IllegalArgumentException {
        final String[] parts = text.split(" ");
        setValue(new Person2(parts[0], parts[1]));
    }
}
