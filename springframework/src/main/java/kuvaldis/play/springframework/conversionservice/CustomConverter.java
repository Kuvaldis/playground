package kuvaldis.play.springframework.conversionservice;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.Converter;

import java.util.Set;

public class CustomConverter implements Converter<BeanWithListOfIntegers, BeanWithSetOfStrings>, ApplicationContextAware {

    private ApplicationContext context;

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    @Override
    public BeanWithSetOfStrings convert(final BeanWithListOfIntegers source) {
        final ConversionService conversionService = context.getBean(ConversionService.class);
        // default conversion service can be used here instead of the one from context
//        final DefaultConversionService conversionService = new DefaultConversionService();
        final Set<String> set = (Set<String>) conversionService.convert(source.getIntegers(), TypeDescriptor.forObject(source.getIntegers()),
                TypeDescriptor.collection(Set.class, TypeDescriptor.valueOf(String.class)));
        final BeanWithSetOfStrings result = new BeanWithSetOfStrings();
        result.setStrings(set);
        return result;
    }
}
