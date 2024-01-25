package com.tqqn.chatcolors.data;

import com.tqqn.chatcolors.colors.Colors;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PluginPlayer {

    private final Player player;
    private final UUID uuid;
    private final String name;


    private Colors selectedColor;

    public PluginPlayer(Player player) {
        this.player = player;
        this.uuid = player.getUniqueId();
        this.name = player.getName();
        this.selectedColor = Colors.NONE;
    }

    public Player getPlayer() {
        return player;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public Colors getSelectedColor() {
        return selectedColor;
    }

    public void setSelectedColor(Colors selectedColor) {
        this.selectedColor = selectedColor;
    }
}
