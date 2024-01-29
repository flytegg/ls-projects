package com.tqqn.chatcolors.listeners;

import com.tqqn.chatcolors.PlayerManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitListener implements Listener {

    private final PlayerManager playerManager;
    public QuitListener(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        playerManager.unLoadPluginPlayer(event.getPlayer());
    }
}
