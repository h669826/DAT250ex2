package com.example.demo;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;

@Configuration(proxyBeanMethods = false)
public class JpaWorkaround implements BeanDefinitionRegistryPostProcessor, PriorityOrdered {

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        for (String name : registry.getBeanDefinitionNames()) {
            BeanDefinition bd = registry.getBeanDefinition(name);

            boolean looksLikeEmf =
                    "entityManagerFactory".equals(name) ||
                            "entityManagerFactory".equals(bd.getFactoryMethodName());

            if (looksLikeEmf) {
                bd.getPropertyValues().add("entityManagerFactoryInterface", EntityManagerFactory.class);
                System.out.println("[JpaProxyWorkaround] Patched EMF bean definition: " + name);
            }
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        // no-op
    }
}



