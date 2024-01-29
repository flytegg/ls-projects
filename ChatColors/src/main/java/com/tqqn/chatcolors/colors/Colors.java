package com.tqqn.chatcolors.colors;

import org.bukkit.Material;

public enum Colors {

    NONE("", "None", Material.BARRIER),
    BLACK("<black>", "Black", Material.BLACK_WOOL),
    DARK_RED("<dark_red>", "Dark Red", Material.REDSTONE);

    private final String color;
    private final String prettyName;
    private final Material guiMaterial;
    Colors(String color, String prettyName, Material guiMaterial) {
        this.color = color;
        this.prettyName = prettyName;
        this.guiMaterial = guiMaterial;
    }

    public String getColor() {
        return color;
    }

    public String getPrettyName() {
        return prettyName;
    }

    public Material getGuiMaterial() {
        return guiMaterial;
    }

}

