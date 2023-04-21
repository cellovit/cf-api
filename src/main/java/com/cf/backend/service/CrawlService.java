package com.cf.backend.service;

import com.cf.backend.domain.response.CrawlResultResponse;

public interface CrawlService {

    String beginNewCrawling(String keyword);

    CrawlResultResponse getCrawlInfo(String id);

}
