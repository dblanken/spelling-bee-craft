package com.spellingbeecraft.search;

import com.spellingbeecraft.config.DifficultyMode;

public class WordMatcher {

    public enum MatchType {
        NONE,
        FUZZY,
        EXACT
    }

    private static final double FUZZY_MATCH_THRESHOLD = 0.85;

    public boolean matches(String userInput, String itemName, DifficultyMode difficulty) {
        return matchType(userInput, itemName, difficulty) != MatchType.NONE;
    }

    /**
     * Returns the type of match: EXACT if all words match exactly (per difficulty rules),
     * FUZZY if items matched only via fuzzy logic (Easy mode), or NONE.
     */
    public MatchType matchType(String userInput, String itemName, DifficultyMode difficulty) {
        if (userInput == null || userInput.isBlank()) {
            return MatchType.EXACT;
        }

        String[] userWords = normalizeAndSplit(userInput);
        String[] itemWords = normalizeAndSplit(itemName);

        boolean anyFuzzy = false;

        for (String userWord : userWords) {
            if (userWord.isEmpty()) continue;
            MatchType bestMatch = MatchType.NONE;
            for (String itemWord : itemWords) {
                MatchType wt = wordMatchType(userWord, itemWord, difficulty);
                if (wt == MatchType.EXACT) {
                    bestMatch = MatchType.EXACT;
                    break;
                } else if (wt == MatchType.FUZZY) {
                    bestMatch = MatchType.FUZZY;
                }
            }
            if (bestMatch == MatchType.NONE) {
                return MatchType.NONE;
            }
            if (bestMatch == MatchType.FUZZY) {
                anyFuzzy = true;
            }
        }
        return anyFuzzy ? MatchType.FUZZY : MatchType.EXACT;
    }

    private String[] normalizeAndSplit(String input) {
        return input.replace('-', ' ').trim().split("\\s+");
    }

    private MatchType wordMatchType(String userWord, String itemWord, DifficultyMode difficulty) {
        return switch (difficulty) {
            case EASY -> fuzzyMatchType(userWord, itemWord);
            case MEDIUM -> userWord.equalsIgnoreCase(itemWord) ? MatchType.EXACT : MatchType.NONE;
            case HARD -> userWord.equals(itemWord) ? MatchType.EXACT : MatchType.NONE;
        };
    }

    private MatchType fuzzyMatchType(String userWord, String itemWord) {
        if (userWord.equalsIgnoreCase(itemWord)) {
            return MatchType.EXACT;
        }
        String a = userWord.toLowerCase();
        String b = itemWord.toLowerCase();

        // Allow single letter omission in the word (e.g. "zombe" for "zombie")
        // but NOT trailing omission (e.g. "zombi") — that's just an incomplete word
        if (a.length() == b.length() - 1 && isSingleDeletion(a, b)) {
            return MatchType.FUZZY;
        }

        // Standard Levenshtein similarity check
        int distance = levenshteinDistance(a, b);
        int maxLen = Math.max(a.length(), b.length());
        if (maxLen == 0) return MatchType.EXACT;
        double similarity = 1.0 - (double) distance / maxLen;
        return similarity >= FUZZY_MATCH_THRESHOLD ? MatchType.FUZZY : MatchType.NONE;
    }

    /**
     * Returns true if 'shorter' can be obtained by removing exactly one non-trailing
     * character from 'longer'. Rejects trailing deletions (those are just incomplete words).
     * Assumes shorter.length() == longer.length() - 1.
     */
    private boolean isSingleDeletion(String shorter, String longer) {
        int i = 0;
        int j = 0;
        boolean skipped = false;
        while (i < shorter.length() && j < longer.length()) {
            if (shorter.charAt(i) == longer.charAt(j)) {
                i++;
                j++;
            } else if (!skipped) {
                skipped = true;
                j++; // skip one character in the longer word
            } else {
                return false;
            }
        }
        // If skipped is false, all chars matched and the deletion is the trailing char — reject
        return skipped;
    }

    private int levenshteinDistance(String a, String b) {
        int m = a.length();
        int n = b.length();
        int[][] dp = new int[m + 1][n + 1];

        for (int i = 0; i <= m; i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= n; j++) {
            dp[0][j] = j;
        }

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                int cost = (a.charAt(i - 1) == b.charAt(j - 1)) ? 0 : 1;
                dp[i][j] = Math.min(
                    Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1),
                    dp[i - 1][j - 1] + cost
                );
            }
        }

        return dp[m][n];
    }
}
