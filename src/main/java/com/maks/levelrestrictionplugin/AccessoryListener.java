package com.maks.levelrestrictionplugin;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class AccessoryListener implements Listener {

    private final ConfigManager configManager;

    public AccessoryListener(ConfigManager configManager) {
        this.configManager = configManager;
    }

    // Obsługa użycia akcesoriów przez interakcję (PPM)
    @EventHandler
    public void onAccessoryUse(PlayerInteractEvent event) {
        if (!event.getAction().isRightClick()) {
            return;
        }

        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (item == null) {
            return;
        }

        // Sprawdź, czy przedmiot jest akcesorium i czy jest ograniczony
        if (isRestrictedAccessory(player, item)) {
            event.setCancelled(true);
            sendMessage(player, item);
        }
    }

    // Obsługa zakładania akcesoriów przez ekwipunek
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getWhoClicked();

        // Sprawdź, czy kliknięcie dotyczy ekwipunku gracza
        if (event.getInventory().getType() != InventoryType.PLAYER) {
            return;
        }

        ItemStack cursorItem = event.getCursor();
        ItemStack currentItem = event.getCurrentItem();

        // Zakładanie akcesorium przez przeciąganie na slot akcesorium
        if (event.getSlot() == getAccessorySlot(player)) {
            if (cursorItem != null && isRestrictedAccessory(player, cursorItem)) {
                event.setCancelled(true);
                sendMessage(player, cursorItem);
            }
        }

        // Zakładanie akcesorium przez shift-kliknięcie
        if (event.getClick().isShiftClick()) {
            if (currentItem != null && isRestrictedAccessory(player, currentItem)) {
                // Sprawdź, czy przedmiot trafi do slotu akcesorium
                if (willGoToAccessorySlot(player, currentItem)) {
                    event.setCancelled(true);
                    sendMessage(player, currentItem);
                }
            }
        }

        // Używanie klawiszy numerycznych
        if (event.getClick() == ClickType.NUMBER_KEY) {
            ItemStack hotbarItem = player.getInventory().getItem(event.getHotbarButton());
            if (hotbarItem != null && isRestrictedAccessory(player, hotbarItem)) {
                if (event.getSlot() == getAccessorySlot(player)) {
                    event.setCancelled(true);
                    sendMessage(player, hotbarItem);
                }
            }
        }
    }

    // Obsługa przeciągania przedmiotów do slotu akcesorium
    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getWhoClicked();

        int accessorySlot = getAccessorySlot(player);

        // Sprawdź, czy przeciąganie dotyczy slotu akcesorium
        if (event.getInventorySlots().contains(accessorySlot)) {
            ItemStack draggedItem = event.getOldCursor();
            if (draggedItem != null && isRestrictedAccessory(player, draggedItem)) {
                event.setCancelled(true);
                sendMessage(player, draggedItem);
            }
        }
    }

    // Metoda sprawdzająca, czy przedmiot jest ograniczonym akcesorium
    private boolean isRestrictedAccessory(Player player, ItemStack item) {
        for (RestrictedItem restrictedItem : configManager.getRestrictedItems()) {
            if (restrictedItem.matches(item)) {
                int requiredLevel = restrictedItem.getRequiredLevel();
                int playerLevel = player.getLevel();
                return playerLevel < requiredLevel;
            }
        }
        return false;
    }

    // Metoda wysyłająca wiadomość do gracza
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

    // Metoda zwracająca indeks slotu akcesorium (dostosuj do swojego pluginu)
    private int getAccessorySlot(Player player) {
        // Jeśli Twój plugin korzysta ze standardowego ekwipunku, musisz określić indeks slotu akcesorium
        // Przykładowo, jeśli używasz dodatkowego ekwipunku z pluginu, może to być slot poza standardowym zakresem
        // Zastąp poniższy kod odpowiednim dla Twojego pluginu

        // Przykład: zwróć slot 45 (pierwszy slot w dolnym rzędzie ekwipunku)
        return 45;
    }

    // Metoda sprawdzająca, czy przedmiot trafi do slotu akcesorium
    private boolean willGoToAccessorySlot(Player player, ItemStack item) {
        // Implementacja zależy od tego, jak Twój plugin obsługuje zakładanie akcesoriów
        // Jeśli akcesoria trafiają do określonego slotu, możesz sprawdzić, czy jest wolny itp.

        // Przykład:
        return true; // Zmień implementację zgodnie z potrzebami
    }
}
