package com.spellingbeecraft;

import com.spellingbeecraft.config.ConfigManager;
import com.spellingbeecraft.config.DifficultyMode;
import com.spellingbeecraft.search.ItemFilter;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpellingBeeCraftMod implements ModInitializer {
    public static final String MOD_ID = "spellingbeecraft";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static ConfigManager configManager;
    private static final ItemFilter itemFilter = new ItemFilter();

    @Override
    public void onInitialize() {
        LOGGER.info("Spelling Bee Craft initializing");
        configManager = new ConfigManager();
        configManager.load();
    }

    public static ConfigManager getConfigManager() {
        return configManager;
    }

    public static ItemFilter getItemFilter() {
        return itemFilter;
    }

    public static boolean isEnabled() {
        return configManager.getConfig().isEnabled();
    }

    public static void setEnabled(boolean enabled) {
        configManager.getConfig().setEnabled(enabled);
        configManager.save();
    }

    public static DifficultyMode getDifficulty() {
        return configManager.getConfig().getDefaultDifficulty();
    }

    public static void setDifficulty(DifficultyMode difficulty) {
        configManager.getConfig().setDefaultDifficulty(difficulty);
        configManager.save();
    }

    public static DifficultyMode cycleDifficulty() {
        DifficultyMode next = getDifficulty().next();
        setDifficulty(next);
        return next;
    }
}
