package com.maks.levelrestrictionplugin;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class ArmorEquipListener implements Listener {

    private final ConfigManager configManager;

    public ArmorEquipListener(ConfigManager configManager) {
        this.configManager = configManager;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getWhoClicked();

        // Sprawdzamy tylko gdy kliknięcie dotyczy slotu zbroi (sloty 5-8)
        if (event.getSlotType() == InventoryType.SlotType.ARMOR) {
            ItemStack cursorItem = event.getCursor();

            // Przypadek 1: Gracz próbuje położyć przedmiot ze swojego kursora na slot zbroi
            if (cursorItem != null && !cursorItem.getType().isAir() && isArmor(cursorItem)) {
                if (cannotEquip(player, cursorItem)) {
                    event.setCancelled(true);
                    sendMessage(player, cursorItem);
                    return;
                }
            }
        }

        // Przypadek 2: Shift+klik na zbroję w ekwipunku (automatyczne zakładanie)
        if (event.getClick().isShiftClick() && event.getCurrentItem() != null) {
            ItemStack clickedItem = event.getCurrentItem();

            if (isArmor(clickedItem) && event.getSlotType() != InventoryType.SlotType.ARMOR) {
                // Sprawdź czy slot docelowy jest wolny
                int armorSlot = getArmorSlot(clickedItem);
                if (armorSlot != -1 && player.getInventory().getItem(armorSlot) == null) {
                    if (cannotEquip(player, clickedItem)) {
                        event.setCancelled(true);
                        sendMessage(player, clickedItem);
                        return;
                    }
                }
            }
        }

        // Przypadek 3: Używanie klawiszy numerycznych na slotach zbroi
        if (event.getClick() == ClickType.NUMBER_KEY && event.getSlotType() == InventoryType.SlotType.ARMOR) {
            ItemStack hotbarItem = player.getInventory().getItem(event.getHotbarButton());
            if (hotbarItem != null && isArmor(hotbarItem)) {
                if (cannotEquip(player, hotbarItem)) {
                    event.setCancelled(true);
                    sendMessage(player, hotbarItem);
                    return;
                }
            }
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getWhoClicked();

        // Sprawdzamy tylko gdy przeciąganie obejmuje sloty zbroi
        for (int slot : event.getRawSlots()) {
            if (slot >= 5 && slot <= 8) { // Sloty zbroi w ekwipunku gracza
                ItemStack draggedItem = event.getOldCursor();
                if (draggedItem != null && isArmor(draggedItem)) {
                    if (cannotEquip(player, draggedItem)) {
                        event.setCancelled(true);
                        sendMessage(player, draggedItem);
                        return;
                    }
                }
            }
        }
    }

    private boolean isArmor(ItemStack item) {
        if (item == null) return false;
        String typeName = item.getType().name();
        return typeName.endsWith("_HELMET") || typeName.endsWith("_CHESTPLATE") || 
               typeName.endsWith("_LEGGINGS") || typeName.endsWith("_BOOTS") ||
               typeName.equals("ELYTRA") || typeName.equals("TURTLE_HELMET");
    }

    private int getArmorSlot(ItemStack item) {
        if (item == null) return -1;
        String typeName = item.getType().name();

        if (typeName.endsWith("_HELMET") || typeName.equals("TURTLE_HELMET")) return 39;
        if (typeName.endsWith("_CHESTPLATE") || typeName.equals("ELYTRA")) return 38;
        if (typeName.endsWith("_LEGGINGS")) return 37;
        if (typeName.endsWith("_BOOTS")) return 36;

        return -1;
    }

    private boolean cannotEquip(Player player, ItemStack item) {
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
        for (RestrictedItem restrictedItem : configManager.getRestrictedItems()) {
            if (restrictedItem.matches(item)) {
                int requiredLevel = restrictedItem.getRequiredLevel();
                String message = configManager.getMessage().replace("%level%", String.valueOf(requiredLevel));
                player.sendMessage(message);
                return;
            }
        }
    }
}
