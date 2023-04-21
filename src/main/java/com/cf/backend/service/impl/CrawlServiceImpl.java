package com.cf.backend.service.impl;

import java.util.*;
import java.util.concurrent.*;

import com.cf.backend.domain.response.CrawlResultResponse;
import com.cf.backend.service.CrawlService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CrawlServiceImpl implements CrawlService {

    private static CrawlServiceImpl uniqueInstance;

    private CrawlServiceImpl() {
    }

    public static synchronized CrawlServiceImpl getInstance() {
        if (uniqueInstance == null)
            uniqueInstance = new CrawlServiceImpl();

        return uniqueInstance;
    }

    CrawlingStorage storage = CrawlingStorage.getInstance();
    Map<String, CompletableFuture<Set<String>>> futureMapList = new ConcurrentHashMap<>();
    private static final Logger LOG = LoggerFactory.getLogger(CrawlServiceImpl.class);

    @Override
    public String beginNewCrawling(String keyword) {
        String randomId = UUID.randomUUID().toString().substring(0, 8);
        LOG.info("### INICIANDO PROCESSO DE CRAWLING");
        AsyncCrawlRunner asyncCrawlRunner = new AsyncCrawlRunner();
        futureMapList.put(randomId, asyncCrawlRunner.runAsyncCrawling(randomId, keyword));
        return randomId;
    }

    @Override
    public CrawlResultResponse getCrawlInfo(String id) {
        CompletableFuture<Set<String>> crawlResultCf = Optional.ofNullable(futureMapList.get(id))
                .orElseThrow(() -> new IllegalArgumentException("Não foi possível encontrar análise com este id"));
        LOG.info(String.format("A análise %s foi encontrada e está %s", id, (crawlResultCf.isDone() ? "concluída" : "em andamento")));

        Set<String> urlList;

        if (crawlResultCf.isDone()) {
            storage.clearTempList(id);
            urlList = crawlResultCf.join();
        } else {
            urlList = storage.getRunningUrlList(id);
        }

        CrawlResultResponse result = new CrawlResultResponse(id, crawlResultCf, urlList);
        return result;
    }

}