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

        List<T> filtered = filterItems(items, nameExtractor, userInput, difficulty);
        if (filtered.isEmpty()) {
            return SearchResult.noMatches(userInput);
        }
        return SearchResult.hasMatches(filtered.size(), userInput);
    }
}
