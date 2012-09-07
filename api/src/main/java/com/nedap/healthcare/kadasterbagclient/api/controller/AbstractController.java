package com.nedap.healthcare.kadasterbagclient.api.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.nedap.healthcare.kadasterbagclient.api.exception.FaildCommunicationWithServer;
import com.nedap.healthcare.kadasterbagclient.api.exception.UnExistingLocation;

/**
 * Abstract controller, defines global behavior of all controllers in the application with focus on error handling.
 * 
 * @author Dusko Vesin
 */
public class AbstractController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractController.class);

    /**
     * Method that handle {@link UnExistingLocation} exception. As response status {@link HttpStatus#NOT_FOUND} is set.
     * 
     * @param exception
     *            thats's received
     */
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(UnExistingLocation.class)
    public void nonExistingLocationException(final UnExistingLocation exception) {

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Not existing location requested with zipcode {} and house number {} ", exception.getZipCode(),
                    exception.getHouseNumber());
        }

    }

    /**
     * Method that handle {@link FaildCommunicationWithServer} exception. As response status
     * {@link HttpStatus#INTERNAL_SERVER_ERROR} is set.
     * 
     * @param exception
     *            that's received
     */
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(FaildCommunicationWithServer.class)
    public void faildCommunicationWithServer(final FaildCommunicationWithServer exception) {

        if (LOGGER.isErrorEnabled()) {
            LOGGER.error("Error ocured in process of fetching information from web service", exception);
        }

    }
}
