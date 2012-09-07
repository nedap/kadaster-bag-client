package com.nedap.healthcare.kadasterbagclient.api;

import javax.servlet.ServletException;

import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.SourceFilteringListener;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.MergedContextConfiguration;
import org.springframework.test.context.support.AbstractContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * TODO - add comments
 * 
 * @author Dusko Vesin
 */
public class MockWebApplicationContextLoader extends AbstractContextLoader {

    MockWebApplication configuration;
    static MockServletContext servletContext;
    static MockServletConfig servletConfig;
    static XmlWebApplicationContext webApplicationContext;

    @Override
    public ApplicationContext loadContext(final MergedContextConfiguration mergedConfig) throws Exception {
        servletConfig = new MockServletConfig(servletContext, configuration.name());
        servletContext = new MockServletContext(configuration.webapp(), new FileSystemResourceLoader());
        webApplicationContext = new XmlWebApplicationContext();
        webApplicationContext.getEnvironment().setActiveProfiles(mergedConfig.getActiveProfiles());
        servletContext
                .setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, webApplicationContext);
        webApplicationContext.setServletConfig(servletConfig);
        webApplicationContext.setConfigLocations(configuration.locations());

        prepareWebApplicationContext();

        return webApplicationContext;
    }

    @Override
    public ApplicationContext loadContext(final String... locations) throws Exception {
        servletConfig = new MockServletConfig(servletContext, configuration.name());
        servletContext = new MockServletContext(configuration.webapp(), new FileSystemResourceLoader());
        webApplicationContext = new XmlWebApplicationContext();
        servletContext
                .setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, webApplicationContext);
        webApplicationContext.setServletConfig(servletConfig);
        webApplicationContext.setConfigLocations(locations);
        prepareWebApplicationContext();
        return webApplicationContext;
    }

    private void prepareWebApplicationContext() throws ServletException {
        @SuppressWarnings("serial") final DispatcherServlet dispatcherServlet = new DispatcherServlet() {
            @Override
            protected WebApplicationContext createWebApplicationContext(final ApplicationContext parent) {
                return webApplicationContext;
            }
        };
        webApplicationContext.addBeanFactoryPostProcessor(new BeanFactoryPostProcessor() {
            @Override
            public void postProcessBeanFactory(final ConfigurableListableBeanFactory beanFactory) {
                beanFactory.registerResolvableDependency(DispatcherServlet.class, dispatcherServlet);
            }
        });
        webApplicationContext.addApplicationListener(new SourceFilteringListener(webApplicationContext,
                new ApplicationListener<ContextRefreshedEvent>() {
                    @Override
                    public void onApplicationEvent(final ContextRefreshedEvent event) {
                        dispatcherServlet.onApplicationEvent(event);
                    }
                }));
        webApplicationContext.refresh();
        webApplicationContext.registerShutdownHook();
        dispatcherServlet.setContextConfigLocation("");
        dispatcherServlet.init(servletConfig);
    }

    @Override
    protected String[] generateDefaultLocations(final Class<?> clazz) {
        extractConfiguration(clazz);
        return super.generateDefaultLocations(clazz);
    }

    @Override
    protected String[] modifyLocations(final Class<?> clazz, final String... locations) {
        extractConfiguration(clazz);
        return super.modifyLocations(clazz, locations);
    }

    private void extractConfiguration(final Class<?> clazz) {
        configuration = AnnotationUtils.findAnnotation(clazz, MockWebApplication.class);
        if (configuration == null) {
            throw new IllegalArgumentException("Test class " + clazz.getName()
                    + " must be annotated @MockWebApplication.");
        }
    }

    @Override
    protected String getResourceSuffix() {
        return "-context.xml";
    }
}
