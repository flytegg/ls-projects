package com.pzyc0.customitems;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SaveItemCommand implements CommandExecutor {
    private final ConfigManager manager;

    public SaveItemCommand(ConfigManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) return false;
        Player player = (Player) sender;
        if(args.length != 1) {
            player.sendMessage(ChatColor.RED+"You need to specify a name for the Item! The name can't have any spaces!");
            return false;
        }
        if(player.getInventory().getItemInMainHand().getType() == Material.AIR){
            player.sendMessage(ChatColor.RED+"You need to have the Item you want to save in you main hand!");
            return false;
        }

        manager.saveItem(player.getInventory().getItemInMainHand(), args[0]);
        player.sendMessage(ChatColor.GREEN+"Saved the Item!");
        manager.saveFile();

        return false;
    }
}
