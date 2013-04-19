package com.nedap.healthcare.kadasterbagclient.api;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * {@link AbstractKadasterBagClientRepositoryTest} with spring annotation and with transaction suppert.
 * 
 * @author Dusko Vesin
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(AbstractSpringTest.TEXT_CONTEXT)
public abstract class AbstractSpringTest {
	public static final String TEXT_CONTEXT = "/META-INF/service-test-context.xml";
}
