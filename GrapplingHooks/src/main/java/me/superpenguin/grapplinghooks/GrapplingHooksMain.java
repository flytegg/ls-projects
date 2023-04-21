package me.superpenguin.grapplinghooks;

import org.bukkit.plugin.java.JavaPlugin;

public class GrapplingHooksMain extends JavaPlugin {

    @Override
    public void onEnable() {
        new GrapplingHook(this);
    }

}
