package com.cf.backend.domain.enums;

public enum CrawlStatus {
    ACTIVE("active"), DONE("done");

    private String statusText;

    private CrawlStatus(String statusText) {
        this.statusText = statusText;
    }

    public String getStatusText() {
        return statusText;
    }
}
