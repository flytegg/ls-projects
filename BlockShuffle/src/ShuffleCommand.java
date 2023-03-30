package com.learnspigot.blockshuffle;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ShuffleCommand implements CommandExecutor {

    private final Main main;

    public ShuffleCommand(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        main.setRunning(!main.isRunning());
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (main.isRunning()) {
                player.sendMessage(ChatColor.GREEN + "The block shuffle has started!");
            } else {
                player.sendMessage(ChatColor.RED + "The block shuffle has stopped!");
            }
        }

        return false;
    }

}
