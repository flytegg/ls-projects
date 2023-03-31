package com.tqqn.particles.listeners;


import com.tqqn.particles.Particles;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class OnLeaveListener implements Listener {

    private final Particles plugin;

    public OnLeaveListener(Particles plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void OnLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        plugin.getParticleManager().removeParticleFromPlayer(player);
    }
}
