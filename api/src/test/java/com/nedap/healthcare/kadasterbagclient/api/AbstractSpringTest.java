package com.nedap.healthcare.kadasterbagclient.api;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * {@link AbstractKadasterBagClientRepositoryTest} with spring annotation and with transaction suppert.
 * 
 * @author Dusko Vesin
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(AbstractKadasterBagClientRepositoryTest.TEXT_CONTEXT)
@Transactional
public abstract class AbstractSpringTest extends AbstractKadasterBagClientRepositoryTest {

}
