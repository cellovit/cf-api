package com.cf.backend.util;

import com.cf.backend.config.AppBaseConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class WebPageUtils {

    private static final Logger LOG = LoggerFactory.getLogger(WebPageUtils.class);

    public static String getPageContent(String pageUrl) {

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(pageUrl))
                .GET()
                .build();
        try {
            HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body().toString();
        } catch (IOException | InterruptedException e) {
            LOG.error("Erro ao processar conteúdo da página " + pageUrl);
            throw new RuntimeException(e);
        }
    }

    public static boolean verifyValidAbsoluteUrl(String link) {
        URI uri = null;
        try {
            uri = new URI(link);
            return uri.isAbsolute() ? link.startsWith(AppBaseConfig.getBaseUrl()) : true;
        } catch (URISyntaxException e) {
            LOG.error("Ocorreu um erro ao definir a url " + link);
            return false;
        }
    }

    public static String verifyPathUrl(String relativeLinkString) {
        final String relativePrefix = "../";
        if (relativeLinkString.startsWith(relativePrefix))
            return AppBaseConfig.getBaseUrl() + relativeLinkString.replace(relativePrefix, "");
        return AppBaseConfig.getBaseUrl() + relativeLinkString;
    }

    public static List<String> extractAnchorLinks(String string) {
        List<String> anchorLinkList = new ArrayList<>();
        final String TAG = "href=\"";
        final int TAG_LENGTH = TAG.length();
        int startIndex = 0, endIndex = 0;
        String nextSubstring = "";

        do {
            startIndex = string.indexOf(TAG);
            if (startIndex != -1) {
                nextSubstring = string.substring(startIndex + TAG_LENGTH);
                endIndex = nextSubstring.indexOf("\"");
                if (endIndex != -1) {
                    String link = nextSubstring.substring(0, endIndex);
                    anchorLinkList.add(link);
                }
                string = nextSubstring;
            }
        } while (startIndex != -1 && endIndex != -1);

        return anchorLinkList;
    }

    public static boolean verifyIfPageContainsKeyword(String pageContent, String keyword) {
        return Pattern.compile(Pattern.quote(keyword), Pattern.CASE_INSENSITIVE).matcher(pageContent).find();
    }

}
