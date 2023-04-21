package com.cf.backend.domain.response;

import com.cf.backend.domain.enums.CrawlStatus;

import java.util.Set;
import java.util.concurrent.CompletableFuture;


public class CrawlResultResponse {

    private String id;
    private Set<String> urls;
    private String status;

    public CrawlResultResponse(String id, CompletableFuture<Set<String>> cf, Set<String> urlList) {
        this.id = id;
        this.status = cf.isDone() ?
                CrawlStatus.DONE.getStatusText() :
                CrawlStatus.ACTIVE.getStatusText();
        this.urls = urlList;
    }

    public void addToResult(String stringToAdd) {
        this.urls.add(stringToAdd);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Set<String> getUrls() {
        return urls;
    }

    public void setUrls(Set<String> urls) {
        this.urls = urls;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CrawlResultResponse)) return false;

        CrawlResultResponse that = (CrawlResultResponse) o;

        if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) return false;
        if (getUrls() != null ? !getUrls().equals(that.getUrls()) : that.getUrls() != null) return false;
        return getStatus() == that.getStatus();
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getUrls() != null ? getUrls().hashCode() : 0);
        result = 31 * result + (getStatus() != null ? getStatus().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CrawlResultResponse{" +
                "id='" + id + '\'' +
                ", urls=" + urls +
                ", status='" + status + '\'' +
                '}';
    }
}
