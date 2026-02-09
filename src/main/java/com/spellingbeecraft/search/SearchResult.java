package com.spellingbeecraft.search;

public class SearchResult {

    public enum Status {
        EMPTY_QUERY,
        NO_MATCHES,
        FUZZY_MATCHES,
        HAS_MATCHES
    }

    private final Status status;
    private final int matchCount;
    private final String query;

    private SearchResult(Status status, int matchCount, String query) {
        this.status = status;
        this.matchCount = matchCount;
        this.query = query;
    }

    public static SearchResult emptyQuery() {
        return new SearchResult(Status.EMPTY_QUERY, 0, "");
    }

    public static SearchResult noMatches(String query) {
        return new SearchResult(Status.NO_MATCHES, 0, query);
    }

    public static SearchResult fuzzyMatches(int count, String query) {
        return new SearchResult(Status.FUZZY_MATCHES, count, query);
    }

    public static SearchResult hasMatches(int count, String query) {
        return new SearchResult(Status.HAS_MATCHES, count, query);
    }

    public Status getStatus() {
        return status;
    }

    public int getMatchCount() {
        return matchCount;
    }

    public String getQuery() {
        return query;
    }
}
