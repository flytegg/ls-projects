package com.tqqn.particles;

import com.tqqn.particles.commands.OpenParticleMenuCommand;
import com.tqqn.particles.listeners.OnLeaveListener;
import com.tqqn.particles.listeners.PlayerInventoryClickListener;
import com.tqqn.particles.managers.ParticleManager;
import com.tqqn.particles.menus.ParticleMenu;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Particles extends JavaPlugin {

    private final ParticleManager particleManager;
    private final ParticleMenu particleMenu;

    public Particles() {
        this.particleManager = new ParticleManager(this);
        this.particleMenu = new ParticleMenu(this);
    }

    @Override
    public void onEnable() {
        registerCommands();
        registerEvents();
        particleManager.addParticleNamesToArray();

        particleMenu.setUpParticleMenu();
        Bukkit.getLogger().info("Plugin has been enabled.");
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info("Plugin has been disabled.");
    }

    /**
     * Returns the ParticleManager.
     * @return ParticleManager
     */
    public ParticleManager getParticleManager() {
        return this.particleManager;
    }

    /**
     * Returns the ParticleMenu.
     * @return ParticleMenu Object.
     */
    public ParticleMenu getParticleMenu() {
        return this.particleMenu;
    }

    /**
     * Register the commands.
     */
    public void registerCommands() {
        this.getCommand("particle").setExecutor(new OpenParticleMenuCommand(this));
    }

    /**
     * Register the events.
     */
    public void registerEvents() {
        PluginManager pluginManager = Bukkit.getServer().getPluginManager();
        pluginManager.registerEvents(new PlayerInventoryClickListener(this), this);
        pluginManager.registerEvents(new OnLeaveListener(this), this);
    }
}
