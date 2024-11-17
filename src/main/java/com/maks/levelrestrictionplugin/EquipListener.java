package com.maks.levelrestrictionplugin;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class EquipListener implements Listener {

    private final ConfigManager configManager;

    public EquipListener(ConfigManager configManager) {
        this.configManager = configManager;
    }

    @EventHandler
    public void onPlayerEquip(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (item == null) {
            return;
        }

        for (RestrictedItem restrictedItem : configManager.getRestrictedItems()) {
            if (restrictedItem.matches(item)) {
                int requiredLevel = restrictedItem.getRequiredLevel();
                int playerLevel = player.getLevel();

                if (playerLevel < requiredLevel) {
                    event.setCancelled(true);
                    String message = configManager.getMessage().replace("%level%", String.valueOf(requiredLevel));
                    player.sendMessage(message);
                    return;
                }
            }
        }
    }
}
