package com.cf.backend.rest;

import com.cf.backend.domain.request.CrawlRequest;
import com.cf.backend.domain.response.CrawlCreationResponse;
import com.cf.backend.domain.response.CrawlResultResponse;
import com.cf.backend.service.CrawlService;
import com.cf.backend.service.impl.CrawlServiceImpl;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

public class CrawlResource {

    CrawlService crawlService = CrawlServiceImpl.getInstance();
    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public CrawlCreationResponse beginNewCrawling(String requestBody) {
        try {
            String keyword = gson.fromJson(requestBody, CrawlRequest.class).getKeyword();
            verifyKeywordLength(keyword);
            String crawlingProcessId = crawlService.beginNewCrawling(keyword);
            return new CrawlCreationResponse(crawlingProcessId);
        } catch (JsonSyntaxException ex) {
            throw new IllegalArgumentException("Não foi possível processar a requisição devido a um erro na leitura dos dados enviados");
        }
    }

    public CrawlResultResponse getCrawlInfo(String id) {
        return crawlService.getCrawlInfo(id);
    }

    private void verifyKeywordLength(String keyword) {
        if (keyword.length() < 4 || keyword.length() > 32) {
            throw new IllegalArgumentException("O termo de busca deve ter no mínimo 4 e no máximo 32 caracteres");
        }
    }

}
