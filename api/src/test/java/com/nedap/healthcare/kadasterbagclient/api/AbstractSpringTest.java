package com.nedap.healthcare.kadasterbagclient.api;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * TODO - add comments
 * 
 * @author Dusko Vesin
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(AbstractGeoCodingRepositoryTest.TEXT_CONTEXT)
@Transactional
public abstract class AbstractSpringTest extends AbstractGeoCodingRepositoryTest {

}
