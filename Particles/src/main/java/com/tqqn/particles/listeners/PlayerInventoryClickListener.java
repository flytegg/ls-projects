package com.tqqn.particles.listeners;

import com.tqqn.particles.Particles;
import com.tqqn.particles.particles.LobbyParticles;
import com.tqqn.particles.utils.Color;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class PlayerInventoryClickListener implements Listener {

    private final Particles plugin;

    public PlayerInventoryClickListener(Particles plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        //If the entity who clicked is not a player, return.
        if (!(event.getWhoClicked() instanceof Player player)) return;

        //If the inventory is not the Particle inventory/menu return if it is, cancel the event to prevent players from taking stuff out of the menu.;
        if (!(event.getView().getTitle().equalsIgnoreCase(Color.translate("&6Particle Menu")))) return;
        event.setCancelled(true);

        //If the clicked item is the Turn off Particle item, remove the particle from the player.
        if (event.getCurrentItem().getType() == Material.BARRIER && event.getCurrentItem().getItemMeta().getDisplayName().equals(Color.translate("&cTurn off Particle"))) {
            if (plugin.getParticleManager().doesPlayerParticleExist(player)) {
                plugin.getParticleManager().removeParticleFromPlayer(player);
                player.sendMessage(Color.translate("&cYou disabled your particle."));
                closePlayerInventory(player);
            } else {
                player.sendMessage(Color.translate("&cYou don't have a particle enabled."));
            }
            return;
        }

        //Check if the clicked item is in the loaded MaterialMap for the particles.
        if (!plugin.getParticleMenu().doesMaterialExistInMap(event.getCurrentItem())) return;

        //Remove the particle from a player if the player has one equipped.
        plugin.getParticleManager().removeParticleFromPlayer(player);

        LobbyParticles lobbyParticles = plugin.getParticleMenu().getLobbyParticlesFromMap(event.getCurrentItem());

        //If player has permission for that specific particle, give the particle.
        if (player.hasPermission(lobbyParticles.getPermission())) {

            plugin.getParticleManager().addParticleToPlayer(player, lobbyParticles);

            player.sendMessage(Color.translate("&6You enabled the &c" + lobbyParticles.getItemName() + " &6particle."));
        } else {
            player.sendMessage(Color.translate("&cYou don't have permission to use this command."));
        }
        closePlayerInventory(player);
    }

    /**
     * Async Scheduler to close the inventory after one tick.
     * @param player Player
     */
    private void closePlayerInventory(Player player) {
        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> {
            player.closeInventory();
        }, 1L);
    }
}
