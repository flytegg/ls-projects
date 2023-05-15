package cc.stormlabs.proximitychat.commands;

import cc.stormlabs.proximitychat.utils.CustomColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GiveMegaphoneCommand implements CommandExecutor {

    private final ItemStack megaphone;

    public GiveMegaphoneCommand(ItemStack megaphone) {
        this.megaphone = megaphone;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!sender.hasPermission("proximitychat.megaphone")) {
            sender.sendMessage(CustomColor.translate("&cYou aren't allowed to do that!"));
            return false;
        }

        if(args.length == 0) {

            if(!(sender instanceof Player)) {
                sender.sendMessage(CustomColor.translate("&eThis command is only for players!"));
                return false;
            }

            Player player = (Player) sender;

            player.getInventory().addItem(megaphone);
            player.sendMessage(CustomColor.translate("&7Congrats! &aYou &7now have a megaphone."));
            return true;

        } else if(args.length == 1) {

            Player target = Bukkit.getPlayer(args[0]);
            if(target == null) {
                sender.sendMessage(CustomColor.translate("&a" + args[0] + " &7is &cnot &7online."));
                return false;
            }

            if(((Player) sender).getUniqueId().equals(target.getUniqueId())) {
                ((Player) sender).performCommand("gmp");
                return true;
            }

            target.getInventory().addItem(megaphone);
            target.sendMessage(CustomColor.translate("&7Congrats! &aYou &7now have a megaphone."));
            sender.sendMessage(CustomColor.translate("&7Congrats! &a" + target.getName() + " &7now has a megaphone."));
            return true;

        } else {
            sender.sendMessage(CustomColor.translate("&7Invalid usage.. Try &e/gmp (player)&7."));
        }

        return false;
    }
}
