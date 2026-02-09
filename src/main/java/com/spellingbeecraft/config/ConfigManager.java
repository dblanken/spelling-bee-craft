package com.spellingbeecraft.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.spellingbeecraft.SpellingBeeCraftMod;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConfigManager {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String CONFIG_FILE = "spellingbeecraft.json";

    private ModConfig config;
    private final Path configPath;

    public ConfigManager() {
        this.configPath = FabricLoader.getInstance().getConfigDir().resolve(CONFIG_FILE);
        this.config = new ModConfig();
    }

    public void load() {
        if (Files.exists(configPath)) {
            try {
                String json = Files.readString(configPath);
                ModConfig loaded = GSON.fromJson(json, ModConfig.class);
                if (loaded != null) {
                    this.config = loaded;
                }
            } catch (Exception e) {
                SpellingBeeCraftMod.LOGGER.warn("Failed to load config, using defaults", e);
                this.config = new ModConfig();
            }
        } else {
            save();
        }
    }

    public void save() {
        try {
            Files.createDirectories(configPath.getParent());
            Files.writeString(configPath, GSON.toJson(config));
        } catch (IOException e) {
            SpellingBeeCraftMod.LOGGER.error("Failed to save config", e);
        }
    }

    public ModConfig getConfig() {
        return config;
    }
}
