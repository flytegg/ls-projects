package com.panav.Invsee;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;


public class InvSee implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player player)) return true;
        if (args.length == 0) return true;

        if (!player.hasPermission("invsee.command")) {
            player.sendMessage(ChatColor.RED + "You don't have the permission invsee.command!");
            return true;
        }

        if (Bukkit.getPlayer(args[0]) == null){
            player.sendMessage(ChatColor.AQUA + "Target not found");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);

        player.openInventory(getInventory(target));
        return true;
    }


    public Inventory getInventory(Player target){
        Inventory inv = Bukkit.createInventory(null, 54, ChatColor.RED + target.getName() + "'s Inventory");

        ItemStack[] armour = target.getInventory().getArmorContents();
        ItemStack[] invContent = target.getInventory().getStorageContents();

        List<ItemStack> contents = new ArrayList<>(Arrays.asList(invContent));

        for (int i = 0; i < 9; i++){
            contents.add(new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
        }

        Collections.addAll(contents, armour);

        ItemStack[] cont = contents.toArray(new ItemStack[0]);

        inv.setContents(cont);
        return inv;
    }
}
