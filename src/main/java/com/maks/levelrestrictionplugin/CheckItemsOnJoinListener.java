package com.maks.levelrestrictionplugin;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

public class CheckItemsOnJoinListener implements Listener {

    private final ConfigManager configManager;

    public CheckItemsOnJoinListener(ConfigManager configManager) {
        this.configManager = configManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        ItemStack[] contents = player.getInventory().getContents();

        // Sprawdzamy wszystkie przedmioty w ekwipunku
        for (ItemStack item : contents) {
            if (item == null) continue;

            for (RestrictedItem restrictedItem : configManager.getRestrictedItems()) {
                if (restrictedItem.matches(item)) {
                    int requiredLevel = restrictedItem.getRequiredLevel();
                    int playerLevel = player.getLevel();

                    // Jeżeli gracz ma za niski poziom -> usuwamy przedmiot z ekwipunku
                    if (playerLevel < requiredLevel) {
                        player.getInventory().remove(item);
                        String message = configManager.getMessage().replace("%level%", String.valueOf(requiredLevel));
                        player.sendMessage(message);
                        break; // Przechodzimy do następnego przedmiotu
                    }
                }
            }
        }
    }
}
