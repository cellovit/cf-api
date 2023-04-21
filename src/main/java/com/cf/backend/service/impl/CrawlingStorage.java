package com.cf.backend.service.impl;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

public class CrawlingStorage {

    private static CrawlingStorage uniqueInstance;

    private CrawlingStorage() {
    }

    public static synchronized CrawlingStorage getInstance() {
        if (uniqueInstance == null)
            uniqueInstance = new CrawlingStorage();

        return uniqueInstance;
    }

    Map<String, CopyOnWriteArraySet<String>> runningAnalysisMap = new ConcurrentHashMap<>();

    public Set<String> getRunningUrlList(String crawlId) {
        return runningAnalysisMap.get(crawlId);
    }
    public void clearTempList(String crawlId) {
        runningAnalysisMap.remove(crawlId);
    }

}
