package com.spellingbeecraft.search;

import com.spellingbeecraft.config.DifficultyMode;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ItemFilter {

    private final WordMatcher wordMatcher = new WordMatcher();

    public <T> List<T> filterItems(List<T> items, Function<T, String> nameExtractor,
                                    String userInput, DifficultyMode difficulty) {
        if (userInput == null || userInput.isBlank()) {
            return items;
        }

        return items.stream()
                .filter(item -> wordMatcher.matches(userInput, nameExtractor.apply(item), difficulty))
                .collect(Collectors.toList());
    }

    public <T> SearchResult filterAndReport(List<T> items, Function<T, String> nameExtractor,
                                             String userInput, DifficultyMode difficulty) {
        if (userInput == null || userInput.isBlank()) {
            return SearchResult.emptyQuery();
        }

        boolean anyFuzzy = false;
        List<T> filtered = new java.util.ArrayList<>();
        for (T item : items) {
            WordMatcher.MatchType mt = wordMatcher.matchType(userInput, nameExtractor.apply(item), difficulty);
            if (mt != WordMatcher.MatchType.NONE) {
                filtered.add(item);
                if (mt == WordMatcher.MatchType.FUZZY) {
                    anyFuzzy = true;
                }
            }
        }

        if (filtered.isEmpty()) {
            return SearchResult.noMatches(userInput);
        }
        if (anyFuzzy) {
            return SearchResult.fuzzyMatches(filtered.size(), userInput);
        }
        return SearchResult.hasMatches(filtered.size(), userInput);
    }
}
