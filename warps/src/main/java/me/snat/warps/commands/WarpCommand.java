package me.snat.warps.commands;

import me.snat.warps.Main;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class WarpCommand implements CommandExecutor {

    private Main main;

    public WarpCommand (Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        YamlConfiguration warps = main.getWarpManager().getWarps();

        Player player = (Player) sender;
        if (!(sender instanceof Player)) return false;

        if (args.length == 0) {
            player.sendMessage(ChatColor.RED + "Please specify a Warp!");
            player.sendMessage(ChatColor.GOLD + "Here are the available warps:");
            for(String warp : warps.getKeys(false)) {
                player.sendMessage(warp);
            }
            return true;
        }

        if (!warps.contains(args[0])) {
            player.sendMessage(ChatColor.RED + "That warp does not exist!");
            return true;
        }

        player.teleport(main.getWarpManager().getWarp(args[0]));
        player.sendMessage(ChatColor.GOLD + "You have been teleported to " + args[0] + ".");
        return true;
    }
}
