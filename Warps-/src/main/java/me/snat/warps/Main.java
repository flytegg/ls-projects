package me.snat.warps;

import me.snat.warps.commands.DeleteWarpCommand;
import me.snat.warps.commands.SetWarpCommand;
import me.snat.warps.commands.WarpCommand;
import me.snat.warps.managers.WarpManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    private WarpManager warpManager;

    @Override
    public void onEnable() {
        getCommand("warp").setExecutor(new WarpCommand(this));
        getCommand("setwarp").setExecutor(new SetWarpCommand(this));
        getCommand("delwarp").setExecutor(new DeleteWarpCommand(this));
        
        warpManager = new WarpManager(this);
    }

    public WarpManager getWarpManager() { return warpManager; }

}
