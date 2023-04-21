package com.cf.backend;

import static spark.Spark.get;
import static spark.Spark.post;

import com.cf.backend.config.AppBaseConfig;
import com.cf.backend.rest.CrawlResource;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Main {

    public static void main(String[] args) {

        AppBaseConfig.initEnvVariables();
        CrawlResource resource = new CrawlResource();

        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();

        get("/crawl/:id", (req, res) -> {
            res.type("application/json");
            return gson.toJson(resource.getCrawlInfo(req.params("id")));
        });

        post("/crawl", (req, res) -> {
            res.type("application/json");
            return gson.toJson(resource.beginNewCrawling(req.body()));
        });
    }
}
