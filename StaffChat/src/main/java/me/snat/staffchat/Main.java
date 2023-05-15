package me.snat.staffchat;

import org.bukkit.plugin.java.JavaPlugin;
public final class Main extends JavaPlugin {

    private String messageLayout;

    @Override
    public void onEnable() {
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        messageLayout = getConfig().getString("message-layout");
        getCommand("staffchat").setExecutor(new StaffChat(this));
    }

    public String getMessageLayout() { return messageLayout; }
}
