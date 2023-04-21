package me.superpenguin.grapplinghooks

import org.bukkit.plugin.java.JavaPlugin

class GrapplingHooksMain: JavaPlugin() {

    override fun onEnable() {
        GrapplingHook(this)
    }
}