package com.tqqn.chatcolors;

import com.tqqn.chatcolors.data.PluginPlayer;
import com.tqqn.chatcolors.gui.ChatColorGUI;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class PlayerManager {

    private final ChatColors plugin;
    private final HashMap<UUID, PluginPlayer> loadedPlayers;
    private final HashMap<UUID, ChatColorGUI> openInventories;

    public PlayerManager(ChatColors plugin) {
        this.plugin = plugin;
        this.loadedPlayers = new HashMap<>();
        this.openInventories = new HashMap<>();
    }

    public ChatColors getPlugin() {
        return plugin;
    }

    public PluginPlayer getPluginPlayer(UUID uuid) {
        return loadedPlayers.get(uuid);
    }

    public void loadPluginPlayer(Player player) {
        if (loadedPlayers.containsKey(player.getUniqueId())) return;
        loadedPlayers.put(player.getUniqueId(), new PluginPlayer(player));
    }

    public void unLoadPluginPlayer(Player player) {
        loadedPlayers.remove(player.getUniqueId());
    }

    public void openInventory(PluginPlayer pluginPlayer) {
        openInventories.put(pluginPlayer.getUuid(), new ChatColorGUI(pluginPlayer));
        openInventories.get(pluginPlayer.getUuid()).openInventory();
        new ChatColorGUI(pluginPlayer).openInventory();
    }

    public void closeInventory(PluginPlayer pluginPlayer) {
        openInventories.get(pluginPlayer.getUuid()).closeInventory();
        openInventories.remove(pluginPlayer.getUuid());
    }

    public boolean existPlayerInventory(PluginPlayer player) {
        return openInventories.containsKey(player.getUuid());
    }
}
