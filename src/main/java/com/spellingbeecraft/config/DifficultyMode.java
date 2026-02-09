package com.spellingbeecraft.config;

public enum DifficultyMode {
    EASY("Easy", "Fuzzy matching (small typos allowed)"),
    MEDIUM("Medium", "Exact words, case-insensitive"),
    HARD("Hard", "Exact words, case-sensitive");

    private final String displayName;
    private final String description;

    DifficultyMode(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public DifficultyMode next() {
        DifficultyMode[] values = values();
        return values[(ordinal() + 1) % values.length];
    }
}
