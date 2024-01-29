package com.tqqn.chatcolors.listeners;

import com.tqqn.chatcolors.PlayerManager;
import com.tqqn.chatcolors.data.PluginPlayer;
import com.tqqn.chatcolors.utils.ChatUtils;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ChatListener implements Listener {

    private final PlayerManager playerManager;

    public ChatListener(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    @EventHandler
    public void onAsyncChat(AsyncChatEvent event) {
        event.setCancelled(true);
        PluginPlayer pluginPlayer = playerManager.getPluginPlayer(event.getPlayer().getUniqueId());

        Bukkit.getOnlinePlayers().forEach(players -> players.sendMessage(ChatUtils.format("<white>" + pluginPlayer.getName() + ": " + pluginPlayer.getSelectedColor().getColor() + ((TextComponent)event.message()).content())));
    }
}
