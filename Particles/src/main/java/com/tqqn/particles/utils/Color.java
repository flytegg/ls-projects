package com.tqqn.particles.utils;

import org.bukkit.ChatColor;

public class Color {

    public static String translate(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }
}
