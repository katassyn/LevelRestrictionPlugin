package com.maks.levelrestrictionplugin;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class CheckItemsOnJoinListener implements Listener {

    private final ConfigManager configManager;

    public CheckItemsOnJoinListener(ConfigManager configManager) {
        this.configManager = configManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerInventory inventory = player.getInventory();

        // Sprawdzamy tylko sloty zbroi (nie usuwamy przedmiotów, tylko zdejmujemy)
        ItemStack[] armorContents = inventory.getArmorContents();
        boolean armorRemoved = false;

        for (int i = 0; i < armorContents.length; i++) {
            ItemStack item = armorContents[i];
            if (item == null) continue;

            for (RestrictedItem restrictedItem : configManager.getRestrictedItems()) {
                if (restrictedItem.matches(item)) {
                    int requiredLevel = restrictedItem.getRequiredLevel();
                    int playerLevel = player.getLevel();

                    if (playerLevel < requiredLevel) {
                        // Przenosimy przedmiot do głównego ekwipunku zamiast usuwać
                        if (inventory.firstEmpty() != -1) {
                            inventory.addItem(item);
                            armorContents[i] = null;
                            armorRemoved = true;
                        } else {
                            // Jeśli ekwipunek jest pełny, upuszczamy przedmiot na ziemię
                            player.getWorld().dropItemNaturally(player.getLocation(), item);
                            armorContents[i] = null;
                            armorRemoved = true;
                        }
                        String message = configManager.getMessage().replace("%level%", String.valueOf(requiredLevel));
                        player.sendMessage(message);
                        break;
                    }
                }
            }
        }

        if (armorRemoved) {
            inventory.setArmorContents(armorContents);
        }

        // Sprawdzamy broń w ręce
        ItemStack mainHand = inventory.getItemInMainHand();
        if (mainHand != null) {
            for (RestrictedItem restrictedItem : configManager.getRestrictedItems()) {
                if (restrictedItem.matches(mainHand)) {
                    int requiredLevel = restrictedItem.getRequiredLevel();
                    int playerLevel = player.getLevel();

                    if (playerLevel < requiredLevel && isWeapon(mainHand)) {
                        // Przenosimy broń do innego slotu
                        if (inventory.firstEmpty() != -1) {
                            inventory.setItemInMainHand(null);
                            inventory.addItem(mainHand);
                        } else {
                            // Jeśli ekwipunek jest pełny, zamieniamy z pierwszym slotem
                            ItemStack firstSlot = inventory.getItem(0);
                            inventory.setItem(0, mainHand);
                            inventory.setItemInMainHand(firstSlot);
                        }
                        String message = configManager.getMessage().replace("%level%", String.valueOf(requiredLevel));
                        player.sendMessage(message);
                        break;
                    }
                }
            }
        }
    }

    private boolean isWeapon(ItemStack item) {
        if (item == null) return false;
        String typeName = item.getType().name();
        return typeName.endsWith("_SWORD") || typeName.endsWith("_AXE") || 
               typeName.endsWith("_PICKAXE") || typeName.endsWith("_SHOVEL") || 
               typeName.endsWith("_HOE") || typeName.equals("TRIDENT") ||
               typeName.equals("BOW") || typeName.equals("CROSSBOW");
    }
}
