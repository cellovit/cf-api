package com.cf.backend.domain.response;

public class CrawlCreationResponse {

    private String id;

    public CrawlCreationResponse(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
