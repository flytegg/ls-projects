package me.snat.staffchat;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static me.snat.staffchat.ChatUtils.color;

public class StaffChat implements CommandExecutor {

    private Main main;

    public StaffChat(Main main) {
        this.main = main;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        String name = sender instanceof Player ? ((Player)sender).getDisplayName() : "Server";
        if (args.length < 1) {
            sender.sendMessage("Please provide a message.");
            return true;
        }

        String message = String.join(" ", args);
        message = color(main.getMessageLayout()
                .replace("{PLAYER}", name)
                .replace("{MESSAGE}", message));

        Bukkit.broadcast(message, "staffchat.use");

        return true;
    }
}
