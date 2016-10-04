package kuvaldis.play.mapstruct;

import org.mapstruct.Named;

@Named("TitleTranslator")
public class Titles {

    @Named("EnglishToRussian")
    public String transateTitleEnglishToRussian(final String title) {
        return "In Russian: " + title;
    }

    @Named("RussianToEnglish")
    public String translateTitleRussianToEnglish(final String title) {
        return "In English: " + title;
    }
}
