package me.superpenguin.grapplinghooks;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class GrapplingHook implements Listener, CommandExecutor {

    GrapplingHooksMain plugin;

    public GrapplingHook(GrapplingHooksMain plugin){
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
        plugin.getCommand("grapplinghook").setExecutor(this);
    }

    // Arbritrary value determining how much power the grappling hook has
    private final int POWER = 3;

    private final ArrayList<UUID> grappling = new ArrayList<>();

    // If you don't want a cooldown, you can just remove the 3 cooldown parts :) (1)
    private final int COOLDOWN_SECONDS = 3;
    private final Cache<UUID, Long> onCooldown = CacheBuilder.newBuilder().expireAfterWrite(COOLDOWN_SECONDS, TimeUnit.SECONDS).build();


    @EventHandler
    public void onFishingRodUse(PlayerFishEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        // Called when they cast the fishing rod
        if (event.getState() == PlayerFishEvent.State.FISHING) {
            // Ensure they are using the grappling hook
            if (event.getHand() == null) return;
            ItemStack inHand = event.getPlayer().getInventory().getItem(event.getHand());
            if (inHand == null || !inHand.hasItemMeta() || !inHand.getItemMeta().hasLocalizedName() || !inHand.getItemMeta().getLocalizedName().equals("GrapplingHook")) return;

            // Check if they are on cooldown and return if they are. (2)
            long timeUntilUse = onCooldown.getIfPresent(uuid) == null ? 0 : onCooldown.getIfPresent(uuid) - System.currentTimeMillis();
            if (timeUntilUse > 0) {
                event.setCancelled(true);
                event.getPlayer().sendMessage("You cannot use this for another " + Math.round(timeUntilUse / 1000.0) + " seconds");
                return;
            }

            // Add them to the list so we know they're grappling
            grappling.add(uuid);

        // When they reel in a fishing rod, check that they are grappling.
        } else if (event.getState() == PlayerFishEvent.State.REEL_IN && grappling.contains(uuid)) {
            // Get a vector pointing from the player to the fishing hook and add it to the players existing velocity.
            Vector additionalVelocity = event.getHook().getLocation().subtract(event.getPlayer().getLocation()).toVector().normalize().multiply(POWER);
            event.getPlayer().setVelocity(event.getPlayer().getVelocity().add(additionalVelocity));
            grappling.remove(uuid);

            // Put the player on a cooldown (3)
            onCooldown.put(uuid, System.currentTimeMillis() + (COOLDOWN_SECONDS * 1000));

        } else if (grappling.contains(uuid)) {
            // they probably caught a fish, let's remove them from the list :)
            grappling.remove(uuid);
        }
    }

    public static ItemStack getItem() {
        ItemStack item = new ItemStack(Material.FISHING_ROD);
        ItemMeta meta = item.getItemMeta();

        // Customise your item here
        meta.setDisplayName("Grappling Hook");
        meta.setLocalizedName("GrapplingHook"); // <-- Identifier for the item
        meta.addEnchant(Enchantment.DURABILITY, 1, false);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.setLore(Arrays.asList("This is a grappling hook"));

        item.setItemMeta(meta);
        return item;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // When your function doesn't specify a return type you can use "return sender.sendMessage..."
        if (!sender.hasPermission("grapplinghook.give")) {
            sender.sendMessage("You do not have permission for this");
            return true;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage("You must be a player");
            return true;
        }
        // You can specify more conditions here if you wish to extend the command to support giving it to other players.

        ((Player) sender).getInventory().addItem(getItem());
        sender.sendMessage("Recived a Grappling Hook!");
        return true;
    }

}
