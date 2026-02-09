package com.spellingbeecraft.search;

import com.spellingbeecraft.config.DifficultyMode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

class ItemFilterTest {

    private ItemFilter filter;
    private List<String> items;
    private final Function<String, String> identity = s -> s;

    @BeforeEach
    void setUp() {
        filter = new ItemFilter();
        items = List.of(
                "Oak Planks",
                "Oak Stairs",
                "Oak Door",
                "Birch Planks",
                "Stone Bricks",
                "Zombie Spawn Egg",
                "Cave-Spider Spawn Egg"
        );
    }

    @Test
    void emptyQueryReturnsAllItems() {
        List<String> result = filter.filterItems(items, identity, "", DifficultyMode.MEDIUM);
        assertEquals(items.size(), result.size());
    }

    @Test
    void nullQueryReturnsAllItems() {
        List<String> result = filter.filterItems(items, identity, null, DifficultyMode.MEDIUM);
        assertEquals(items.size(), result.size());
    }

    @Test
    void filtersBySingleWord() {
        List<String> result = filter.filterItems(items, identity, "oak", DifficultyMode.MEDIUM);
        assertEquals(3, result.size());
        assertTrue(result.contains("Oak Planks"));
        assertTrue(result.contains("Oak Stairs"));
        assertTrue(result.contains("Oak Door"));
    }

    @Test
    void filtersByTwoWords() {
        List<String> result = filter.filterItems(items, identity, "oak planks", DifficultyMode.MEDIUM);
        assertEquals(1, result.size());
        assertEquals("Oak Planks", result.get(0));
    }

    @Test
    void noMatchesReturnsEmptyList() {
        List<String> result = filter.filterItems(items, identity, "diamond", DifficultyMode.MEDIUM);
        assertTrue(result.isEmpty());
    }

    @Test
    void partialWordDoesNotMatch() {
        List<String> result = filter.filterItems(items, identity, "oa", DifficultyMode.MEDIUM);
        assertTrue(result.isEmpty());
    }

    @Test
    void filterAndReportEmptyQuery() {
        SearchResult result = filter.filterAndReport(items, identity, "", DifficultyMode.MEDIUM);
        assertEquals(SearchResult.Status.EMPTY_QUERY, result.getStatus());
    }

    @Test
    void filterAndReportNoMatches() {
        SearchResult result = filter.filterAndReport(items, identity, "diamond", DifficultyMode.MEDIUM);
        assertEquals(SearchResult.Status.NO_MATCHES, result.getStatus());
        assertEquals("diamond", result.getQuery());
    }

    @Test
    void filterAndReportHasMatches() {
        SearchResult result = filter.filterAndReport(items, identity, "oak", DifficultyMode.MEDIUM);
        assertEquals(SearchResult.Status.HAS_MATCHES, result.getStatus());
        assertEquals(3, result.getMatchCount());
    }

    @Test
    void subsetMatchingWorks() {
        List<String> result = filter.filterItems(items, identity, "zombie egg", DifficultyMode.MEDIUM);
        assertEquals(1, result.size());
        assertEquals("Zombie Spawn Egg", result.get(0));
    }

    @Test
    void hyphenNormalizationInFilter() {
        List<String> result = filter.filterItems(items, identity, "cave spider", DifficultyMode.MEDIUM);
        assertEquals(1, result.size());
        assertEquals("Cave-Spider Spawn Egg", result.get(0));
    }
}
