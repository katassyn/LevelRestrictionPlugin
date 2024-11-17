package com.maks.levelrestrictionplugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class RestrictedItem {

    private final Material material;
    private final String displayName;
    private final int requiredLevel;

    public RestrictedItem(String materialName, String displayName, int requiredLevel) {
        this.material = Material.matchMaterial(materialName);
        if (this.material == null) {
            throw new IllegalArgumentException("Invalid material name: " + materialName);
        }
        this.displayName = ChatColor.translateAlternateColorCodes('&', displayName);
        this.requiredLevel = requiredLevel;
    }


    public boolean matches(ItemStack item) {
        if (item == null || item.getType() != material) {
            return false;
        }
        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasDisplayName()) {
            return false;
        }
        String itemDisplayName = ChatColor.stripColor(meta.getDisplayName());
        String expectedDisplayName = ChatColor.stripColor(displayName);
        boolean matches = itemDisplayName.equals(expectedDisplayName);
        // Wiadomość debugująca
        if (matches) {
            Bukkit.getLogger().info("Item matched: " + itemDisplayName);
        } else {
            Bukkit.getLogger().info("Item did not match. Expected: " + expectedDisplayName + ", Found: " + itemDisplayName);
        }
        return matches;
    }



    public int getRequiredLevel() {
        return requiredLevel;
    }
}
