package com.cf.backend.service.impl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import com.cf.backend.domain.response.CrawlResultResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class CrawlServiceImplTest {

    @Mock
    CrawlingStorage storage = CrawlingStorage.getInstance();

    @InjectMocks
    CrawlServiceImpl service = CrawlServiceImpl.getInstance();

    private static String crawlId;

    @BeforeAll
    static void setupTests() {
        crawlId = UUID.randomUUID().toString();
    }

    @Test
    void shouldBeginNewCrawlingAndReturnAnId() {
        String crawlId = service.beginNewCrawling("teste");

        assertThat(service.futureMapList.containsKey(crawlId), is(true));
        assertThat(crawlId, is(notNullValue()));
    }

    @Test
    void whenSubmitAnExistentIdWithActiveAnalisysShouldGetCrawlInfoAndThenReturnstorageResponse() {
        service.futureMapList.put(crawlId, new CompletableFuture<>());
        CrawlResultResponse response = service.getCrawlInfo(crawlId);
//        verify(storage).getRunningRandomIdList(crawlId);
        assertThat(response, is(notNullValue()));
    }

    @Test
    void whenSubmitAnExistentIdShouldGetCrawlInfoAndThenReturnValidResponse() {
        service.futureMapList.put(crawlId,
                CompletableFuture.completedFuture(Collections.emptySet()));
        CrawlResultResponse response = service.getCrawlInfo(crawlId);
        assertThat(response, is(notNullValue()));
    }

    @Test
    void whenCrawlIdNotFoundGetCrawlInfoShouldThrowIllegalArgumentException() {
        String nonexistentId = UUID.randomUUID().toString();

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            service.getCrawlInfo(nonexistentId);
        });
    }

}
