package com.tqqn.chatcolors.commands;

import com.tqqn.chatcolors.PlayerManager;
import com.tqqn.chatcolors.gui.ChatColorGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ChooseChatColorCommand implements CommandExecutor {

    private final PlayerManager playerManager;

    public ChooseChatColorCommand(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player player)) return true;
        playerManager.openInventory(playerManager.getPluginPlayer(player.getUniqueId()));
        return true;
    }
}
