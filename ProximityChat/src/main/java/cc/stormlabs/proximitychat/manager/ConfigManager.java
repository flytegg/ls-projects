package cc.stormlabs.proximitychat.manager;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class ConfigManager {

    private final FileConfiguration configuration;

    public ConfigManager(JavaPlugin plugin) {
        this.configuration = plugin.getConfig();
        this.configuration.options().copyDefaults(true);
        plugin.saveDefaultConfig();
    }

    public int getTalkRange() {
        return this.configuration.getInt("talk-range");
    }

    // value gets added to the default talk range
    public int getMegaphoneRange() {
        return getTalkRange() + this.configuration.getInt("megaphone-range");
    }

    public boolean showPlayerDistance() {
        return this.configuration.getBoolean("show-distance");
    }

    public boolean allowGlobalChat() {
        return this.configuration.getBoolean("allow-global-chat");
    }

}
