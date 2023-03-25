package me.snat.warps.managers;

import me.snat.warps.Main;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class WarpManager {

    private Main main;
    private File file;
    private YamlConfiguration warps;


    public WarpManager(Main main) {
        this.main = main;
        this.file = new File(main.getDataFolder(), "warps.yml");

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("Could not load warps.yml");
            }
        }

        warps = YamlConfiguration.loadConfiguration(file);
    }


    public void saveWarps() {
        try {
            warps.save(file);
        } catch (IOException e) {
            System.out.println("Could not save warps.yml");
        }
    }

    public void setWarp(String name, Location location) {
        warps.set(name + ".location", location);
    }

    public Location getWarp(String name) {
        return warps.getLocation(name + ".location");
    }

    public YamlConfiguration getWarps() { return warps; }
}
