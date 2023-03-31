package com.tqqn.particles.managers;

import com.tqqn.particles.Particles;
import com.tqqn.particles.particles.LobbyParticles;
import com.tqqn.particles.tasks.PlayParticleRunnable;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class ParticleManager {

    private final Particles plugin;

    private final HashMap<UUID, LobbyParticles> playerLobbyParticles = new HashMap<>();

    private final ArrayList<String> customParticles = new ArrayList<>();

    private final HashMap<UUID, PlayParticleRunnable> playParticleRunnableHashMap = new HashMap<>();

    public ParticleManager(Particles plugin) {
        this.plugin = plugin;
    }

    /**
     * Adding Particle names to Array
     */
    public void addParticleNamesToArray() {
        for (LobbyParticles lobbyParticles : LobbyParticles.values()) {
            customParticles.add(lobbyParticles.name());
        }
    }

    /**
     * Checks if the Player has a particle.
     * @param player Player
     * @return true or false
     */
    public boolean doesPlayerParticleExist(Player player) {
        return playerLobbyParticles.containsKey(player.getUniqueId());
    }

    /**
     * Add a particle to the player.
     * @param player Player
     * @param lobbyParticles Particle
     */
    public void addParticleToPlayer(Player player, LobbyParticles lobbyParticles) {

        playerLobbyParticles.remove(player.getUniqueId());

        if (playParticleRunnableHashMap.containsKey(player.getUniqueId())) {
            playParticleRunnableHashMap.get(player.getUniqueId()).cancel();
            playParticleRunnableHashMap.remove(player.getUniqueId());
        }

        PlayParticleRunnable playParticleRunnable = new PlayParticleRunnable(plugin.getParticleManager(), lobbyParticles, player);
        playParticleRunnable.runTaskTimerAsynchronously(plugin, 0, 10L);

        playParticleRunnableHashMap.put(player.getUniqueId(), playParticleRunnable);
        playerLobbyParticles.put(player.getUniqueId(), lobbyParticles);
    }

    /**
     * Remove the equipped particle from the player.
     * @param player Player
     */
    public void removeParticleFromPlayer(Player player) {
        playerLobbyParticles.remove(player.getUniqueId());

        if (!playParticleRunnableHashMap.containsKey(player.getUniqueId())) return;

        playParticleRunnableHashMap.get(player.getUniqueId()).cancel();
        playParticleRunnableHashMap.remove(player.getUniqueId());
    }

    /**
     * Returns the size of LoadedParticles
     * @return int size
     */
    public int getParticlesMapSize() {
        return customParticles.size();
    }
}
