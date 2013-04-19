package com.nedap.healthcare.kadasterbagclient.api;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * {@link AbstractKadasterBagClientRepositoryTest} with spring annotation,
 * transaction but without valid Kadaster Bag address.
 * 
 * @author Srdjan Radulovic
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/META-INF/missing-service-test-context.xml")
public abstract class AbstractSpringNoExternalServiceTest {

}
