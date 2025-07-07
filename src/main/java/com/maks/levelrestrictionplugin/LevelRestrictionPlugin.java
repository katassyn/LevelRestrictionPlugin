package com.maks.levelrestrictionplugin;

import org.bukkit.plugin.java.JavaPlugin;

public class LevelRestrictionPlugin extends JavaPlugin {

    private ConfigManager configManager;

    @Override
    public void onEnable() {
        // Inicjalizacja ConfigManager
        configManager = new ConfigManager(this);

        // Rejestracja listenerów
        getServer().getPluginManager().registerEvents(new ArmorEquipListener(configManager), this);
        getServer().getPluginManager().registerEvents(new AttackListener(configManager), this);
        getServer().getPluginManager().registerEvents(new WeaponUseListener(configManager), this);
        // Opcjonalnie: CheckItemsOnJoinListener - jeśli chcesz sprawdzać przy logowaniu
        // getServer().getPluginManager().registerEvents(new CheckItemsOnJoinListener(configManager), this);

        getLogger().info("LevelRestrictionPlugin has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("LevelRestrictionPlugin has been disabled!");
    }
}
