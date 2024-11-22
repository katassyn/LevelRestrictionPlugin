package com.maks.levelrestrictionplugin;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class AttackListener implements Listener {

    private final ConfigManager configManager;

    public AttackListener(ConfigManager configManager) {
        this.configManager = configManager;
    }

    @EventHandler
    public void onPlayerAttack(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getDamager();

        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        if (itemInHand == null) {
            return;
        }

      ///  Bukkit.getLogger().info("Player " + player.getName() + " attacked with item: " + itemInHand.getType());

        for (RestrictedItem restrictedItem : configManager.getRestrictedItems()) {
            if (restrictedItem.matches(itemInHand)) {
                int requiredLevel = restrictedItem.getRequiredLevel();
                int playerLevel = player.getLevel();

                if (playerLevel < requiredLevel) {
                    event.setCancelled(true);
                    String message = configManager.getMessage().replace("%level%", String.valueOf(requiredLevel));
                    player.sendMessage(message);
                    Bukkit.getLogger().info("Action cancelled. Player level: " + playerLevel + ", Required level: " + requiredLevel);
                    return;
                }
            }
        }
    }

}
