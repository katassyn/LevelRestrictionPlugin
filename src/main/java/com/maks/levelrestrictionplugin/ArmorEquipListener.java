package com.maks.levelrestrictionplugin;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.ItemStack;

public class ArmorEquipListener implements Listener {

    private final ConfigManager configManager;

    public ArmorEquipListener(ConfigManager configManager) {
        this.configManager = configManager;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        // Sprawdź, czy kliknął gracz
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getWhoClicked();

        // Sprawdź, czy kliknięcie dotyczy zakładania zbroi
        if (event.getSlotType() == InventoryType.SlotType.ARMOR || event.getSlotType() == InventoryType.SlotType.QUICKBAR || event.getSlotType() == InventoryType.SlotType.CONTAINER) {
            ItemStack cursorItem = event.getCursor();
            ItemStack currentItem = event.getCurrentItem();

            // Zakładanie zbroi przez przeciąganie
            if (cursorItem != null && isArmor(cursorItem)) {
                if (isRestrictedItem(player, cursorItem)) {
                    event.setCancelled(true);
                    sendMessage(player, cursorItem);
                }
            }

            // Zakładanie zbroi przez kliknięcie na slot zbroi
            if (currentItem != null && isArmor(currentItem) && event.getClick().isShiftClick()) {
                if (isRestrictedItem(player, currentItem)) {
                    event.setCancelled(true);
                    sendMessage(player, currentItem);
                }
            }
        }

        // Używanie klawiszy numerycznych
        if (event.getClick() == ClickType.NUMBER_KEY) {
            ItemStack hotbarItem = player.getInventory().getItem(event.getHotbarButton());
            if (hotbarItem != null && isArmor(hotbarItem)) {
                if (isRestrictedItem(player, hotbarItem)) {
                    event.setCancelled(true);
                    sendMessage(player, hotbarItem);
                }
            }
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        // Sprawdź, czy przeciąganie dotyczy slotów zbroi
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getWhoClicked();

        for (int slot : event.getRawSlots()) {
            if (slot >= 5 && slot <= 8) { // Sloty zbroi
                ItemStack draggedItem = event.getOldCursor();
                if (draggedItem != null && isArmor(draggedItem)) {
                    if (isRestrictedItem(player, draggedItem)) {
                        event.setCancelled(true);
                        sendMessage(player, draggedItem);
                    }
                }
            }
        }
    }

    private boolean isArmor(ItemStack item) {
        if (item == null) {
            return false;
        }
        String typeName = item.getType().name();
        return typeName.endsWith("_HELMET") || typeName.endsWith("_CHESTPLATE") || typeName.endsWith("_LEGGINGS") || typeName.endsWith("_BOOTS");
    }

    private boolean isRestrictedItem(Player player, ItemStack item) {
        for (RestrictedItem restrictedItem : configManager.getRestrictedItems()) {
            if (restrictedItem.matches(item)) {
                int requiredLevel = restrictedItem.getRequiredLevel();
                int playerLevel = player.getLevel();
                return playerLevel < requiredLevel;
            }
        }
        return false;
    }

    private void sendMessage(Player player, ItemStack item) {
        int requiredLevel = 0;
        for (RestrictedItem restrictedItem : configManager.getRestrictedItems()) {
            if (restrictedItem.matches(item)) {
                requiredLevel = restrictedItem.getRequiredLevel();
                break;
            }
        }
        String message = configManager.getMessage().replace("%level%", String.valueOf(requiredLevel));
        player.sendMessage(message);
    }
}
