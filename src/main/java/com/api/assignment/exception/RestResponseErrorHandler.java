package com.api.assignment.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

@Component
public class RestResponseErrorHandler implements ResponseErrorHandler {

    private static final Logger logger = LoggerFactory.getLogger(RestResponseErrorHandler.class);

    @Override
    public boolean hasError(ClientHttpResponse httpResponse)
            throws IOException {

        return (
                httpResponse.getStatusCode().series() == HttpStatus.Series.CLIENT_ERROR
                        || httpResponse.getStatusCode().series() == HttpStatus.Series.SERVER_ERROR);
    }

    @Override
    public void handleError(ClientHttpResponse httpResponse)
            throws IOException {

        if (httpResponse.getStatusCode().series() == HttpStatus.Series.CLIENT_ERROR) {
            logger.info("4xx series Error!" + httpResponse.getStatusCode());
        }
        if (httpResponse.getStatusCode().series() == HttpStatus.Series.SERVER_ERROR) {
            logger.info("5xx series Error!" + httpResponse.getStatusCode());
        }
    }

}

