package com.cf.backend.service.impl;

import com.cf.backend.config.AppBaseConfig;
import com.cf.backend.util.WebPageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

public class AsyncCrawlRunner {

    Set<String> visitedLinks = new HashSet<>();
    Set<String> urlList = Collections.synchronizedSet(new HashSet<>());
    CrawlingStorage storage = CrawlingStorage.getInstance();
    private static final Logger LOG = LoggerFactory.getLogger(AsyncCrawlRunner.class);

    public CompletableFuture<Set<String>> runAsyncCrawling(String crawlId, String keyword) {
        storage.runningAnalysisMap.put(crawlId, new CopyOnWriteArraySet<>());
        return CompletableFuture.supplyAsync(() ->
                this.getPagesWhereKeywordExists(crawlId, AppBaseConfig.getBaseUrl(), keyword, AppBaseConfig.getMaxResults()));
    }

    public Set<String> getPagesWhereKeywordExists(String crawlId, String currentUrl, String searchKeyword, long maxResults) {

        if (urlList.size() < maxResults || maxResults < 0) {
            String pageContent = WebPageUtils.getPageContent(currentUrl).toString();
            visitedLinks.add(currentUrl);
            LOG.info(String.format("Buscando termo '%s' na página %s", searchKeyword, currentUrl));

            if (WebPageUtils.verifyIfPageContainsKeyword(pageContent, searchKeyword)) {
                LOG.info(String.format("O termo '%s' foi encontrado na página %s", searchKeyword, currentUrl));
                urlList.add(currentUrl);
                storage.runningAnalysisMap.get(crawlId).add(currentUrl);
            }

            List<String> validUrlStringList = WebPageUtils.extractAnchorLinks(pageContent).stream()
                    .filter(WebPageUtils::verifyValidAbsoluteUrl)
                    .map(WebPageUtils::verifyPathUrl)
                    .collect(Collectors.toList());

            validUrlStringList.removeAll(visitedLinks);

            for (String url : validUrlStringList) {
                CompletableFuture.runAsync(() ->
                        getPagesWhereKeywordExists(crawlId, url, searchKeyword, maxResults)).join();
            }
        }

        return urlList;
    }

}
