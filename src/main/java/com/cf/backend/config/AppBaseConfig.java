package com.cf.backend.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Optional;

public class AppBaseConfig {
    private static final Logger LOG = LoggerFactory.getLogger(AppBaseConfig.class);

    private static final String BASE_URL_ENV_STRING = "BASE_URL";
    private static final String MAX_RESULTS_ENV_STRING = "MAX_RESULTS";

    private static String baseUrl;
    private static int maxResults;

    public static void initEnvVariables() {
        baseUrl = setupBaseUrl();
        maxResults = setupMaxResult();
    }

    private static String setupBaseUrl() {
        Optional<String> baseUrlOptional = Optional.ofNullable(System.getenv(BASE_URL_ENV_STRING));

        if (baseUrlOptional.isPresent()) {
            try {
                URI uri1 = new URL(baseUrlOptional.get()).toURI();
                if (uri1.isAbsolute())
                    return uri1.toString();
                catchBaseUrlError();
            } catch (MalformedURLException | URISyntaxException e) {
                catchBaseUrlError();
            }
        } else {
            catchBaseUrlError();
        }
        throw catchBaseUrlError();
    }

    private static IllegalArgumentException catchBaseUrlError() {
        String errorMessage = "Ocorreu um erro ao validar a URL";
        LOG.error(errorMessage);
        System.exit(500);
        return new IllegalArgumentException(errorMessage);
    }

    private static int setupMaxResult() {

        Optional<String> maxResultsOptional = Optional.ofNullable(System.getenv(MAX_RESULTS_ENV_STRING));

        if (maxResultsOptional.isPresent()) {
            try {
                return Integer.valueOf(maxResultsOptional.get());
            } catch (NumberFormatException ex) {
                return -1;
            }
        } else {
            return -1;
        }
    }

    public static String getBaseUrl() {
        return baseUrl;
    }

    public static int getMaxResults() {
        return maxResults;
    }
}
