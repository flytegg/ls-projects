package com.tqqn.particles.tasks;

import com.tqqn.particles.managers.ParticleManager;
import com.tqqn.particles.particles.LobbyParticles;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayParticleRunnable extends BukkitRunnable {

    private final LobbyParticles lobbyParticles;
    private final Player player;
    private final ParticleManager particleManager;

    public PlayParticleRunnable(ParticleManager particleManager, LobbyParticles lobbyParticles, Player player) {
        this.particleManager = particleManager;
        this.lobbyParticles = lobbyParticles;
        this.player = player;
    }

    /**
     * Runnable that will check/give the particle if the player still has the particle equipped. If not remove/cancel it.
     */
    @Override
    public void run() {

        if (!(particleManager.doesPlayerParticleExist(player))) {
            cancel();
        }

        Location playerLocation = new Location(player.getWorld(), player.getLocation().getX(), player.getLocation().getY()+1.5, player.getLocation().getZ());
        player.spawnParticle(lobbyParticles.getParticle(), playerLocation, lobbyParticles.getCount());
    }
}
