package me.snat.warps.commands;

import me.snat.warps.Main;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class SetWarpCommand implements CommandExecutor {

    private Main main;

    public SetWarpCommand(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        YamlConfiguration warps = main.getWarpManager().getWarps();

        Player player = (Player) sender;
        if (!(sender instanceof Player)) return false;

        if (args.length == 0) {
            player.sendMessage(ChatColor.RED + "Please specify a name for the warp.");
            return true;
        }

        if (warps.contains(args[0])) {
            player.sendMessage(ChatColor.RED + "A warp with that name already exists.");
            return true;
        }

        Location loc = player.getLocation();

        main.getWarpManager().setWarp(args[0], loc);
        main.getWarpManager().saveWarps();

        player.sendMessage(ChatColor.GOLD + "Warp " + args[0] + " has been created!" );

        return true;
    }
}
