package com.spellingbeecraft.search;

import com.spellingbeecraft.config.DifficultyMode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static com.spellingbeecraft.search.WordMatcher.MatchType.*;
import static org.junit.jupiter.api.Assertions.*;

class WordMatcherTest {

    private WordMatcher matcher;

    @BeforeEach
    void setUp() {
        matcher = new WordMatcher();
    }

    // MEDIUM mode tests
    @Test
    void mediumSingleWordMatchesFirstWord() {
        assertTrue(matcher.matches("oak", "Oak Planks", DifficultyMode.MEDIUM));
    }

    @Test
    void mediumTwoWordsExactMatch() {
        assertTrue(matcher.matches("oak stairs", "Oak Stairs", DifficultyMode.MEDIUM));
    }

    @Test
    void mediumWordsInDifferentOrder() {
        assertTrue(matcher.matches("stairs oak", "Oak Stairs", DifficultyMode.MEDIUM));
    }

    @Test
    void mediumPartialWordDoesNotMatch() {
        assertFalse(matcher.matches("oak sta", "Oak Stairs", DifficultyMode.MEDIUM));
    }

    @Test
    void mediumSubsetWordsMatch() {
        assertTrue(matcher.matches("zombie egg", "Zombie Spawn Egg", DifficultyMode.MEDIUM));
    }

    @Test
    void mediumSubsetWordsReverseOrder() {
        assertTrue(matcher.matches("egg zombie", "Zombie Spawn Egg", DifficultyMode.MEDIUM));
    }

    // EASY mode tests
    @Test
    void easyTrailingLetterMissingDoesNotMatch() {
        // "zombi" is just an incomplete word — force the kid to finish typing
        assertFalse(matcher.matches("zombi", "Zombie Spawn Egg", DifficultyMode.EASY));
    }

    @Test
    void easyMissingMiddleLetterMatches() {
        // "zombe" is "zombie" with the 'i' removed
        assertTrue(matcher.matches("zombe", "Zombie Spawn Egg", DifficultyMode.EASY));
    }

    @Test
    void easyMissingFirstLetterMatches() {
        // "ombie" is "zombie" with the 'z' removed
        assertTrue(matcher.matches("ombie", "Zombie Spawn Egg", DifficultyMode.EASY));
    }

    @Test
    void easyBadSpellingDoesNotMatch() {
        // "zambie" has a wrong letter (substitution), not a missing letter
        assertFalse(matcher.matches("zambie", "Zombie Spawn Egg", DifficultyMode.EASY));
    }

    // HARD mode tests
    @Test
    void hardExactCaseMatches() {
        assertTrue(matcher.matches("Zombie", "Zombie Spawn Egg", DifficultyMode.HARD));
    }

    @Test
    void hardWrongCaseDoesNotMatch() {
        assertFalse(matcher.matches("zombie", "Zombie Spawn Egg", DifficultyMode.HARD));
    }

    // Empty input tests
    @Test
    void emptyInputMatchesAnything() {
        assertTrue(matcher.matches("", "Oak Planks", DifficultyMode.MEDIUM));
        assertTrue(matcher.matches("", "Oak Planks", DifficultyMode.EASY));
        assertTrue(matcher.matches("", "Oak Planks", DifficultyMode.HARD));
    }

    @Test
    void nullInputMatchesAnything() {
        assertTrue(matcher.matches(null, "Oak Planks", DifficultyMode.MEDIUM));
    }

    @Test
    void blankInputMatchesAnything() {
        assertTrue(matcher.matches("   ", "Oak Planks", DifficultyMode.MEDIUM));
    }

    // Hyphen normalization
    @Test
    void mediumHyphenNormalization() {
        assertTrue(matcher.matches("cave spider", "Cave-Spider", DifficultyMode.MEDIUM));
    }

    @Test
    void mediumHyphenInUserInput() {
        assertTrue(matcher.matches("cave-spider", "Cave Spider", DifficultyMode.MEDIUM));
    }

    @Nested
    class MatchTypeReturnValues {

        @Test
        void mediumExactMatchReturnsExact() {
            assertEquals(EXACT, matcher.matchType("oak", "Oak Planks", DifficultyMode.MEDIUM));
        }

        @Test
        void mediumPartialWordReturnsNone() {
            assertEquals(NONE, matcher.matchType("oa", "Oak Planks", DifficultyMode.MEDIUM));
        }

        @Test
        void easyExactMatchReturnsExact() {
            assertEquals(EXACT, matcher.matchType("zombie", "Zombie Spawn Egg", DifficultyMode.EASY));
        }

        @Test
        void easySingleDeletionReturnsFuzzy() {
            assertEquals(FUZZY, matcher.matchType("zombe", "Zombie Spawn Egg", DifficultyMode.EASY));
        }

        @Test
        void easyBadSpellingReturnsNone() {
            assertEquals(NONE, matcher.matchType("zambie", "Zombie Spawn Egg", DifficultyMode.EASY));
        }

        @Test
        void hardExactCaseReturnsExact() {
            assertEquals(EXACT, matcher.matchType("Zombie", "Zombie Spawn Egg", DifficultyMode.HARD));
        }

        @Test
        void hardWrongCaseReturnsNone() {
            assertEquals(NONE, matcher.matchType("zombie", "Zombie Spawn Egg", DifficultyMode.HARD));
        }

        @Test
        void emptyInputReturnsExact() {
            assertEquals(EXACT, matcher.matchType("", "Oak Planks", DifficultyMode.MEDIUM));
        }
    }

    @Nested
    class EasyModeLevenshteinPath {

        @Test
        void longWordSingleSubstitutionFuzzyMatches() {
            // "enchantmant" vs "enchantment" — distance 1, similarity ~0.91
            assertTrue(matcher.matches("enchantmant", "Enchantment Table", DifficultyMode.EASY));
            assertEquals(FUZZY, matcher.matchType("enchantmant", "Enchantment Table", DifficultyMode.EASY));
        }

        @Test
        void shortWordSingleSubstitutionDoesNotMatch() {
            // "oat" vs "oak" — distance 1, similarity ~0.67, below threshold
            assertFalse(matcher.matches("oat", "Oak Planks", DifficultyMode.EASY));
        }
    }

    @Nested
    class EdgeCases {

        @Test
        void singleCharMatchesSingleCharItemWord() {
            assertTrue(matcher.matches("a", "A", DifficultyMode.MEDIUM));
        }

        @Test
        void singleCharDoesNotMatchDifferentChar() {
            assertFalse(matcher.matches("a", "B", DifficultyMode.MEDIUM));
        }

        @Test
        void extraSpacesInInput() {
            assertTrue(matcher.matches("  oak   planks  ", "Oak Planks", DifficultyMode.MEDIUM));
        }

        @Test
        void queryWithMoreWordsThanItemName() {
            assertFalse(matcher.matches("oak planks extra", "Oak Planks", DifficultyMode.MEDIUM));
        }

        @Test
        void duplicateQueryWordMatchesItemWithWordOnce() {
            // each user word independently finds a match — "oak" matches "Oak" both times
            assertTrue(matcher.matches("oak oak", "Oak Planks", DifficultyMode.MEDIUM));
        }
    }
}
