package com.example.library.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@ConfigurationProperties(prefix = "app.cache")
public class AppCacheProperties {

    private final List<String> cacheNames = new ArrayList<>();

    private final Map<String, CacheProperties> cashes = new HashMap<>();

    private CacheType cacheType;

    @Data
    public static class CacheProperties {
        private Duration expiry = Duration.ZERO;
    }

    public interface CacheNames {
        String DATABASE_BOOKS = "databaseBooks";
        String DATABASE_BOOKS_BY_ID = "databaseBooksById";
        String DATABASE_BOOKS_BY_TITLE_AND_AUTHOR = "databaseBooksByTitleAndAuthor";
        String DATABASE_BOOKS_BY_CATEGORY_NAME = "databaseBooksByCategoryName";
    }

    public enum CacheType {
        IN_MEMORY, REDIS
    }

}
