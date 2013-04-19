package com.nedap.healthcare.kadasterbagclient.api;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 * Abstract controller test, should be extended by all tests that test any of {@link AbstractController} subtipes.
 * 
 * @author Dusko Vesin
 */
@MockWebApplication(name = "some-controller", locations = AbstractWebTests.TEST_CONTEXT)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = MockWebApplicationContextLoader.class)
public abstract class AbstractWebTests {

    // Necessary constant tweak
    static final String TEST_CONTEXT = AbstractSpringTest.TEXT_CONTEXT;

    @Autowired
    private DispatcherServlet servlet;

    protected ObjectMapper mapper;
    protected MockHttpServletRequest mockRequest;
    protected MockHttpServletResponse mockResponse;

    @Before
    public void setUp() throws ServletException, IOException {
        mapper = new ObjectMapper();

        mockRequest = new MockHttpServletRequest();
        mockRequest.setAttribute(HandlerMapping.INTROSPECT_TYPE_LEVEL_MAPPING, true);

        mockResponse = new MockHttpServletResponse();
    }

    protected String processRequest(final MediaType mediaType, final Object requestBody,
            final MockHttpServletRequest request, final MockHttpServletResponse response) throws IOException,
            JsonGenerationException, JsonMappingException, ServletException, UnsupportedEncodingException {

        if (requestBody != null) {
            request.setContentType(mediaType.toString());
            request.addHeader("Content-Type", MediaType.APPLICATION_JSON.toString());
            request.setContent(getMapper().writeValueAsString(requestBody).getBytes());
        }

        getServlet().service(request, response);
        final String results = response.getContentAsString().trim();
        return results;
    }

    public DispatcherServlet getServlet() {
        return servlet;
    }

    public ObjectMapper getMapper() {
        return mapper;
    }

}
