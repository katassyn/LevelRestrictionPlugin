package com.maks.levelrestrictionplugin;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class WeaponUseListener implements Listener {

    private final ConfigManager configManager;

    public WeaponUseListener(ConfigManager configManager) {
        this.configManager = configManager;
    }

    @EventHandler
    public void onPlayerUseItem(PlayerInteractEvent event) {
        // Sprawdzamy tylko prawoklik (używanie przedmiotu)
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (item == null) {
            return;
        }

        // Sprawdzamy tylko narzędzia/broń które można "użyć" prawym przyciskiem
        if (!isUsableWeapon(item)) {
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

    private boolean isUsableWeapon(ItemStack item) {
        if (item == null) return false;
        String typeName = item.getType().name();
        
        // Broń którą można "użyć" prawym przyciskiem
        return typeName.equals("BOW") || typeName.equals("CROSSBOW") || 
               typeName.equals("TRIDENT") || typeName.equals("SHIELD") ||
               typeName.endsWith("_SPAWN_EGG") || typeName.equals("FISHING_ROD") ||
               typeName.equals("FLINT_AND_STEEL") || typeName.endsWith("_BUCKET");
    }
}