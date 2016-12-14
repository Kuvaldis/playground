package kuvaldis.play.springframework.scoperesolver;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ScopeMetadata;
import org.springframework.context.annotation.ScopeMetadataResolver;

public class MyScopeResolver implements ScopeMetadataResolver {

    @Override
    public ScopeMetadata resolveScopeMetadata(final BeanDefinition definition) {
        System.out.println(definition);
        final ScopeMetadata scopeMetadata = new ScopeMetadata();
        final String scope = ScopeResolverBean.class.getName().equals(definition.getBeanClassName())
                ? "prototype"
                : definition.getScope();
        scopeMetadata.setScopeName(scope);
        return scopeMetadata;
    }
}
