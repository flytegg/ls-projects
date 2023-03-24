package cc.stormlabs.proximitychat;

import cc.stormlabs.proximitychat.commands.GiveMegaphoneCommand;
import cc.stormlabs.proximitychat.commands.GlobalChatCommand;
import cc.stormlabs.proximitychat.events.AsyncPlayerChat;
import cc.stormlabs.proximitychat.manager.ConfigManager;
import cc.stormlabs.proximitychat.utils.CustomColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;
import java.util.Objects;

public final class ProximityChat extends JavaPlugin {

    private ConfigManager configManager;

    @Override
    public void onEnable() {
        configManager = new ConfigManager(this);
        Bukkit.getPluginManager().registerEvents(new AsyncPlayerChat(this), this);
        Objects.requireNonNull(getCommand("gmp")).setExecutor(new GiveMegaphoneCommand(getMegaphone()));
        Objects.requireNonNull(getCommand("gc")).setExecutor(new GlobalChatCommand(getConfigManager()));
    }

    @Override
    public void onDisable() {

    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public ItemStack getMegaphone() {
        ItemStack megaphone = new ItemStack(Material.STICK);
        ItemMeta megaphoneMeta = megaphone.getItemMeta();

        if(megaphoneMeta != null) {
            megaphoneMeta.setDisplayName(CustomColor.translate("&a&lMegaphone"));
            megaphoneMeta.setLore(Collections.singletonList(CustomColor.translate("&7Increases your talk rang to &e"
                            + getConfigManager().getMegaphoneRange()
                            + " &7blocks!")));
            megaphone.setItemMeta(megaphoneMeta);
        }

        return megaphone;
    }

}
