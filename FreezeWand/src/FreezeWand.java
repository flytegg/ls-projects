package com.learnspigot.freezewand;

import com.learnspigot.freezewand.profile.Profile;
import com.learnspigot.freezewand.profile.ProfileHandler;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public final class FreezeWand extends JavaPlugin implements Listener {

    // Declare profile handler
    private ProfileHandler profileHandler;

    @Override
    public void onEnable() {
        // Instantiate profile handler
        profileHandler = new ProfileHandler();

        // Register the event listeners in this class
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onPlayerInteractAtEntity(@NotNull final PlayerInteractAtEntityEvent e) {
        // If the entity interacted with wasn't a player,
        // the player wasn't holding a stick,
        // or if the hand wasn't the main hand,
        // return
        if (!(e.getRightClicked() instanceof Player) ||
                e.getPlayer().getInventory().getItemInMainHand().getType() != Material.STICK ||
                e.getHand() != EquipmentSlot.HAND) {
            return;
        }

        // Attempt to get both players' profiles
        final Optional<Profile> optionalProfile = profileHandler.findByUniqueId(e.getPlayer().getUniqueId());
        final Optional<Profile> optionalTarget = profileHandler.findByUniqueId(e.getRightClicked().getUniqueId());

        // If either profile isn't present, return
        if (!optionalProfile.isPresent() || !optionalTarget.isPresent()) {
            return;
        }

        // Get the profile from the optional
        final Profile profile = optionalProfile.get();
        final Profile target = optionalTarget.get();

        // Set the target's frozen state to the opposite
        target.setFrozen(!target.isFrozen());

        // Send the target and profile a message to update them on the target's frozen status
        target.sendMessage("&eYou have been " + (target.isFrozen() ? "" : "un") + "frozen!");
        profile.sendMessage("&eYou have " + (target.isFrozen() ? "" : "un") + "frozen " + target.getName() + "!");
    }

    @EventHandler
    public void onPlayerMove(@NotNull final PlayerMoveEvent e) {
        // Null check for the to location
        if (e.getTo() == null) {
            return;
        }

        // If the x, y and z coordinates are the same, return
        if (e.getFrom().getX() == e.getTo().getX() && e.getFrom().getY() == e.getTo().getY() && e.getFrom().getZ() == e.getTo().getZ()) {
            return;
        }

        // Attempt to get profile
        final Optional<Profile> optionalProfile = profileHandler.findByUniqueId(e.getPlayer().getUniqueId());

        // If profile isn't present, return
        if (!optionalProfile.isPresent()) {
            return;
        }

        // Get the profile from the optional
        final Profile profile = optionalProfile.get();

        // If the profile is frozen, cancel the player's movement
        if (profile.isFrozen()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerJoin(@NotNull final PlayerJoinEvent e) {
        // Check if profile exists for player
        if (profileHandler.exists(e.getPlayer().getUniqueId())) {
            // If it does, and the profile isn't null, update their name
            profileHandler.findByUniqueId(e.getPlayer().getUniqueId()).ifPresent(
                    profile -> profile.setName(e.getPlayer().getName())
            );
        } else {
            // If they don't exist already, register a profile for them
            profileHandler.register(e.getPlayer().getUniqueId(), e.getPlayer().getName());
        }
    }

}
