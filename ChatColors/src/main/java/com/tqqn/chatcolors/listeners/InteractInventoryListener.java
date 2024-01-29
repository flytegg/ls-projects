package com.tqqn.chatcolors.listeners;

import com.tqqn.chatcolors.PlayerManager;
import com.tqqn.chatcolors.colors.Colors;
import com.tqqn.chatcolors.data.PluginPlayer;
import com.tqqn.chatcolors.utils.ChatUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class InteractInventoryListener implements Listener {

    private final PlayerManager playerManager;

    public InteractInventoryListener(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    @EventHandler
    public void onInteract(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (!(event.getView().getOriginalTitle().equalsIgnoreCase("§cChatColor menu"))) return;
        event.setCancelled(true);

        if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) return;

        PluginPlayer pluginPlayer = playerManager.getPluginPlayer(player.getUniqueId());

        if (event.getCurrentItem().getType() == pluginPlayer.getSelectedColor().getGuiMaterial()) {
            player.sendMessage(ChatUtils.format("<red>You have already selected this."));
            playerManager.closeInventory(pluginPlayer);
            return;
        }

        if (playerManager.getPlugin().getLoadedColors().containsKey(event.getCurrentItem().getType())) {
            Colors color = playerManager.getPlugin().getLoadedColors().get(event.getCurrentItem().getType());
            pluginPlayer.setSelectedColor(color);
            player.sendMessage(ChatUtils.format("<red>You have selected " + color.getColor() + color.getPrettyName() + "<red>!"));
            playerManager.closeInventory(pluginPlayer);
        }



    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (playerManager.existPlayerInventory(playerManager.getPluginPlayer(event.getPlayer().getUniqueId()))) return;
        playerManager.closeInventory(playerManager.getPluginPlayer(event.getPlayer().getUniqueId()));
    }
}
