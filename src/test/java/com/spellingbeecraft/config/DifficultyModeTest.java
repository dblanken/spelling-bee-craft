package com.spellingbeecraft.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DifficultyModeTest {

    @Test
    void nextCyclesEasyToMedium() {
        assertEquals(DifficultyMode.MEDIUM, DifficultyMode.EASY.next());
    }

    @Test
    void nextCyclesMediumToHard() {
        assertEquals(DifficultyMode.HARD, DifficultyMode.MEDIUM.next());
    }

    @Test
    void nextCyclesHardToEasy() {
        assertEquals(DifficultyMode.EASY, DifficultyMode.HARD.next());
    }

    @Test
    void displayNameIsNonNullForAllValues() {
        for (DifficultyMode mode : DifficultyMode.values()) {
            assertNotNull(mode.getDisplayName());
        }
    }

    @Test
    void descriptionIsNonNullForAllValues() {
        for (DifficultyMode mode : DifficultyMode.values()) {
            assertNotNull(mode.getDescription());
        }
    }
}
