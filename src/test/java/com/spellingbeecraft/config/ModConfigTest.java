package com.spellingbeecraft.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ModConfigTest {

    private ModConfig config;

    @BeforeEach
    void setUp() {
        config = new ModConfig();
    }

    @Test
    void defaultEnabledIsTrue() {
        assertTrue(config.isEnabled());
    }

    @Test
    void defaultDifficultyIsMedium() {
        assertEquals(DifficultyMode.MEDIUM, config.getDefaultDifficulty());
    }

    @Test
    void defaultSearchModeIsContinuous() {
        assertEquals(ModConfig.SearchMode.CONTINUOUS, config.getSearchMode());
    }

    @Test
    void defaultShowStatusMessagesIsTrue() {
        assertTrue(config.isShowStatusMessages());
    }

    @Test
    void defaultEnableColorFeedbackIsTrue() {
        assertTrue(config.isEnableColorFeedback());
    }

    @Test
    void defaultFuzzyMatchThreshold() {
        assertEquals(0.85, config.getFuzzyMatchThreshold());
    }

    @Test
    void setEnabledRoundTrip() {
        config.setEnabled(false);
        assertFalse(config.isEnabled());
    }

    @Test
    void setDefaultDifficultyRoundTrip() {
        config.setDefaultDifficulty(DifficultyMode.HARD);
        assertEquals(DifficultyMode.HARD, config.getDefaultDifficulty());
    }

    @Test
    void setSearchModeRoundTrip() {
        config.setSearchMode(ModConfig.SearchMode.ON_ENTER);
        assertEquals(ModConfig.SearchMode.ON_ENTER, config.getSearchMode());
    }

    @Test
    void setShowStatusMessagesRoundTrip() {
        config.setShowStatusMessages(false);
        assertFalse(config.isShowStatusMessages());
    }

    @Test
    void setEnableColorFeedbackRoundTrip() {
        config.setEnableColorFeedback(false);
        assertFalse(config.isEnableColorFeedback());
    }

    @Test
    void setFuzzyMatchThresholdRoundTrip() {
        config.setFuzzyMatchThreshold(0.75);
        assertEquals(0.75, config.getFuzzyMatchThreshold());
    }
}
