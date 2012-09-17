package com.nedap.healthcare.kadasterbagclient.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.web.context.WebApplicationContext;

/**
 * Configures a mock {@link WebApplicationContext}. Each test class (or parent class) using
 * {@link MockWebApplicationContextLoader} must be annotated with this.
 * 
 * @author Dusko Vesin
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface MockWebApplication {
    String webapp() default "src/main/webapp";

    String name();

    String[] locations() default "/META-INF/test-context.xml";
}
