package com.tqqn.chatcolors.listeners;

import com.tqqn.chatcolors.PlayerManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    private final PlayerManager playerManager;

    public JoinListener(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        playerManager.loadPluginPlayer(event.getPlayer());
    }
}
