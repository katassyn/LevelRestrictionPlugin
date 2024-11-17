package com.maks.levelrestrictionplugin;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class ConfigManager {

    private final LevelRestrictionPlugin plugin;
    private final List<RestrictedItem> restrictedItems;
    private final String message;

    public ConfigManager(LevelRestrictionPlugin plugin) {
        this.plugin = plugin;
        this.restrictedItems = new ArrayList<>();
        plugin.saveDefaultConfig();
        loadConfig();
        this.message = plugin.getConfig().getString("message", "You must be at least level %level% to use this item!");
    }

    public void loadConfig() {
        FileConfiguration config = plugin.getConfig();
        ConfigurationSection itemsSection = config.getConfigurationSection("items");
        if (itemsSection != null) {
            for (String key : itemsSection.getKeys(false)) {
                ConfigurationSection itemSection = itemsSection.getConfigurationSection(key);
                if (itemSection != null) {
                    String id = itemSection.getString("id");
                    String displayName = itemSection.getString("display_name");
                    int level = itemSection.getInt("lv");
                    if (id != null && displayName != null) {
                        RestrictedItem restrictedItem = new RestrictedItem(id, displayName, level);
                        restrictedItems.add(restrictedItem);
                        // Dodajemy wiadomość debugującą
                        plugin.getLogger().info("Loaded restricted item: " + key + " (Material: " + id + ", DisplayName: " + displayName + ", Level: " + level + ")");
                    } else {
                        plugin.getLogger().warning("Invalid item configuration for: " + key);
                    }
                }
            }
        } else {
            plugin.getLogger().warning("No items section found in config.yml!");
        }
    }


    public List<RestrictedItem> getRestrictedItems() {
        return restrictedItems;
    }

    public String getMessage() {
        return message;
    }
}
