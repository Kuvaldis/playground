package kuvaldis.play.hibernate.validator.container;

import javax.validation.valueextraction.ExtractedValue;
import javax.validation.valueextraction.ValueExtractor;

public class GearBoxValueExtractor implements ValueExtractor<GearBox<@ExtractedValue ?>> {

    @Override
    public void extractValues(final GearBox<@ExtractedValue ?> originalValue, final ValueReceiver receiver) {
        receiver.value(null, originalValue.getGear());
    }
}
