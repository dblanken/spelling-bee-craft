package com.spellingbeecraft.search;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SearchResultTest {

    @Test
    void emptyQueryHasCorrectDefaults() {
        SearchResult result = SearchResult.emptyQuery();
        assertEquals(SearchResult.Status.EMPTY_QUERY, result.getStatus());
        assertEquals(0, result.getMatchCount());
        assertEquals("", result.getQuery());
    }

    @Test
    void noMatchesPreservesQuery() {
        SearchResult result = SearchResult.noMatches("diamond");
        assertEquals(SearchResult.Status.NO_MATCHES, result.getStatus());
        assertEquals(0, result.getMatchCount());
        assertEquals("diamond", result.getQuery());
    }

    @Test
    void fuzzyMatchesPreservesCountAndQuery() {
        SearchResult result = SearchResult.fuzzyMatches(3, "zombe");
        assertEquals(SearchResult.Status.FUZZY_MATCHES, result.getStatus());
        assertEquals(3, result.getMatchCount());
        assertEquals("zombe", result.getQuery());
    }

    @Test
    void hasMatchesPreservesCountAndQuery() {
        SearchResult result = SearchResult.hasMatches(5, "oak");
        assertEquals(SearchResult.Status.HAS_MATCHES, result.getStatus());
        assertEquals(5, result.getMatchCount());
        assertEquals("oak", result.getQuery());
    }
}
