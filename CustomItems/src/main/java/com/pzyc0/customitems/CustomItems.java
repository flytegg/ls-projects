package com.pzyc0.customitems;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public final class CustomItems extends JavaPlugin {

    //File for the custom items and the yml file of it
    private File datafile;
    private YamlConfiguration ymlDataFile;

    @Override
    public void onEnable() {
        // Plugin startup logic

        getConfig().options().copyDefaults();
        saveDefaultConfig();

        datafile = new File(getDataFolder(), "customitems.yml");
        if(datafile.exists()){
            try {
                datafile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ymlDataFile = YamlConfiguration.loadConfiguration(datafile);
        try {
            ymlDataFile.save(datafile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //The manager that saves and loads ItemStacks
        ConfigManager manager = new ConfigManager(this);
        getCommand("getitem").setExecutor(new GetItemCommand(manager));
        getCommand("getitem").setTabCompleter(new GetItemCommand(manager));
        getCommand("saveitem").setExecutor(new SaveItemCommand(manager));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        try {
            ymlDataFile.save(datafile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public YamlConfiguration getYmlDataFile() {
        return ymlDataFile;
    }
    public File getDatafile() {
        return datafile;
    }
}
