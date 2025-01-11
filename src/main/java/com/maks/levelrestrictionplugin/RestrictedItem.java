package com.maks.levelrestrictionplugin;

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
        // Porównujemy wyłącznie tekst pozbawiony kolorów,
        // aby uniknąć problemów z różnymi kodami kolorów.
        String itemDisplayName = ChatColor.stripColor(meta.getDisplayName());
        String expectedDisplayName = ChatColor.stripColor(displayName);

        return itemDisplayName.equals(expectedDisplayName);
    }

    public int getRequiredLevel() {
        return requiredLevel;
    }
}
