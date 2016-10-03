package kuvaldis.play.java;

import org.junit.Test;

import java.text.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class I18n {

    @Test
    public void testLocaleConstructor() throws Exception {
        final Locale locale1 = new Locale("fr");
        assertEquals("fr", locale1.getLanguage());
        assertEquals("", locale1.getCountry());

        final Locale locale2 = new Locale("en", "US");
        assertEquals("en", locale2.getLanguage());
        assertEquals("US", locale2.getCountry());

        final Locale locale3 = new Locale("en", "US", "NY");
        assertEquals("en", locale3.getLanguage());
        assertEquals("US", locale3.getCountry());
        assertEquals("NY", locale3.getVariant());
    }

    @Test
    public void testLocaleBuilder() throws Exception {
        final Locale locale = new Locale.Builder()
                .setLanguage("en")
                .setRegion("US")
                .build();
        assertEquals("en", locale.getLanguage());
        assertEquals("US", locale.getCountry());
    }

    @Test
    public void testLocaleFactory() throws Exception {
        final Locale locale = Locale.forLanguageTag("en-US");
        assertEquals("en", locale.getLanguage());
        assertEquals("US", locale.getCountry());
    }

    @Test
    public void testLocaleConst() throws Exception {
        // no RUSSIA huh -_-
        final Locale locale = Locale.FRANCE;
        assertEquals("fr", locale.getLanguage());
        assertEquals("FR", locale.getCountry());
    }

    @Test
    public void testOtherLocaleParams() throws Exception {
        final Locale locale = Locale.US;
        assertEquals("United States", locale.getDisplayCountry());
        assertEquals("English", locale.getDisplayLanguage());
        assertEquals("English (United States)", locale.getDisplayName());
        assertEquals("USA", locale.getISO3Country());
        assertEquals("eng", locale.getISO3Language());
        assertEquals("en", locale.getLanguage());
        assertEquals("US", locale.getCountry());
    }

    @Test
    public void testResourceBundle() throws Exception {
        final ResourceBundle usBundle = ResourceBundle.getBundle("i18n.resourceBundle", Locale.US);
        assertEquals("Hi!", usBundle.getString("key"));
        final ResourceBundle caBundle = ResourceBundle.getBundle("i18n.resourceBundle", Locale.CANADA_FRENCH);
        assertEquals("Oui oui parle vous Francais", caBundle.getString("key"));
        final ResourceBundle rootBundle = ResourceBundle.getBundle("i18n.resourceBundle", Locale.ROOT);
        assertEquals("Hello World!", rootBundle.getString("key"));
        // probably will not work on your computer, as it takes default bundle, which is en_US for me
        final ResourceBundle defaultBundle = ResourceBundle.getBundle("i18n.resourceBundle", Locale.CHINA);
        assertEquals("Hi!", defaultBundle.getString("key"));
    }

    @Test
    public void testDateTimeFormat() throws Exception {
        final Date date = Date.from(LocalDateTime.of(2016, 7, 3, 13, 37).atZone(ZoneId.systemDefault()).toInstant());
        final DateFormat usDateFormat = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US);
        final String usFormattedDate = usDateFormat.format(date);
        assertEquals("Jul 3, 2016 1:37:00 PM", usFormattedDate);
        final DateFormat ukDateFormat = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.UK);
        final String ukFormattedDate = ukDateFormat.format(date);
        assertEquals("03-Jul-2016 13:37:00", ukFormattedDate);
    }

    @Test
    public void testCurrencyFormat() throws Exception {
        final Double currency = 525_600.10;

        final Currency usCurrentCurrency = Currency.getInstance(Locale.US);
        assertEquals("US Dollar", usCurrentCurrency.getDisplayName());
        final NumberFormat usCurrencyFormatter = NumberFormat.getCurrencyInstance(Locale.US);
        final String usFormattedCurrency = usCurrencyFormatter.format(currency);
        assertEquals("$525,600.10", usFormattedCurrency);

        final Currency ukCurrentCurrency = Currency.getInstance(Locale.UK);
        assertEquals("British Pound Sterling", ukCurrentCurrency.getDisplayName());
        final NumberFormat ukCurrencyFormatter = NumberFormat.getCurrencyInstance(Locale.UK);
        final String ukFormattedCurrency = ukCurrencyFormatter.format(currency);
        assertEquals("£525,600.10", ukFormattedCurrency);
    }

    @Test
    public void testNumberFormat() throws Exception {
        final Double number = 525_949.2;

        final NumberFormat usNumberFormat = NumberFormat.getNumberInstance(Locale.US);
        final String usFormattedNumber = usNumberFormat.format(number);
        assertEquals("525,949.2", usFormattedNumber);

        final Locale denmarkLocale = new Locale("da", "DK");
        final NumberFormat denmarkNumberFormat = NumberFormat.getNumberInstance(denmarkLocale);
        final String denmarkFormattedNumber = denmarkNumberFormat.format(number);
        assertEquals("525.949,2", denmarkFormattedNumber);
    }

    @Test
    public void testPercentageFormat() throws Exception {
        final Double percent = 0.25;

        final NumberFormat usPercentFormatter = NumberFormat.getPercentInstance(Locale.US);
        final String usFormattedPercent = usPercentFormatter.format(percent);
        assertEquals("25%", usFormattedPercent);

        final NumberFormat ukPercentFormatter = NumberFormat.getPercentInstance(Locale.UK);
        final String ukFormattedPercent = ukPercentFormatter.format(percent);
        assertEquals("25%", ukFormattedPercent);
    }

    @Test
    public void testTimezone() throws Exception {
        final ZoneId newYork = ZoneId.of("America/New_York");
        final ZoneId moscow = ZoneId.of("Europe/Moscow");
        final LocalDateTime localDateTime = LocalDateTime.of(2016, 10, 10, 1, 1);
        final ZonedDateTime dateTimeInMoscow = ZonedDateTime.of(localDateTime, moscow);
        final ZonedDateTime dateTimeInNewYork = dateTimeInMoscow.withZoneSameInstant(newYork);

        assertEquals(1, dateTimeInMoscow.getHour());
        assertEquals(10, dateTimeInMoscow.getDayOfMonth());
        assertEquals(18, dateTimeInNewYork.getHour());
        assertEquals(9, dateTimeInNewYork.getDayOfMonth());
    }

    @Test
    public void testCollator() throws Exception {
        final Locale locale = Locale.US;
        final Collator collator = Collator.getInstance(locale);

        assertTrue(collator.compare("ab", "yz") < 0);
    }

    @Test
    public void testCustomRulesCollator() throws Exception {
        final String rules = "< z < y < x";
        final RuleBasedCollator collator = new RuleBasedCollator(rules);

        assertTrue(collator.compare("x", "y") > 0);
    }

    @Test
    public void testWordsBreakIterator() throws Exception {
        final Locale locale = Locale.US;
        final BreakIterator breakIterator = BreakIterator.getWordInstance(locale);
        breakIterator.setText("Eat your own pants.");
        final List<Integer> indexes = new ArrayList<>();
        int boundaryIndex = breakIterator.first();
        while (boundaryIndex != BreakIterator.DONE) {
            indexes.add(boundaryIndex);
            boundaryIndex = breakIterator.next();
        }

        assertEquals(Arrays.asList(0, 3, 4, 8, 9, 12, 13, 18, 19), indexes);
    }
}
