package com.spellingbeecraft.config;

public class ModConfig {
    private boolean enabled = true;
    private DifficultyMode defaultDifficulty = DifficultyMode.MEDIUM;
    private boolean showStatusMessages = true;
    private boolean enableColorFeedback = true;
    private SearchMode searchMode = SearchMode.CONTINUOUS;
    private double fuzzyMatchThreshold = 0.85;

    public enum SearchMode {
        ON_ENTER,
        CONTINUOUS
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public DifficultyMode getDefaultDifficulty() {
        return defaultDifficulty;
    }

    public void setDefaultDifficulty(DifficultyMode defaultDifficulty) {
        this.defaultDifficulty = defaultDifficulty;
    }

    public boolean isShowStatusMessages() {
        return showStatusMessages;
    }

    public void setShowStatusMessages(boolean showStatusMessages) {
        this.showStatusMessages = showStatusMessages;
    }

    public boolean isEnableColorFeedback() {
        return enableColorFeedback;
    }

    public void setEnableColorFeedback(boolean enableColorFeedback) {
        this.enableColorFeedback = enableColorFeedback;
    }

    public SearchMode getSearchMode() {
        return searchMode;
    }

    public void setSearchMode(SearchMode searchMode) {
        this.searchMode = searchMode;
    }

    public double getFuzzyMatchThreshold() {
        return fuzzyMatchThreshold;
    }

    public void setFuzzyMatchThreshold(double fuzzyMatchThreshold) {
        this.fuzzyMatchThreshold = fuzzyMatchThreshold;
    }
}
