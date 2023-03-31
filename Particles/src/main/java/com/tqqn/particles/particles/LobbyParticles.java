package com.tqqn.particles.particles;

import org.bukkit.Material;
import org.bukkit.Particle;

public enum LobbyParticles {

    PARTICLE_LAVADRIP(Particle.DRIP_LAVA, 1, Material.LAVA_BUCKET, 0, "&6&lDrip-Lava Particle", "lobbyplugin.lavadrip"),
    PARTICLE_HEART(Particle.HEART,1, Material.RED_DYE, 1,"&c&lHeart Particle", "lobbyplugin.heart"),
    PARTICLE_ANGRY(Particle.VILLAGER_ANGRY,1,Material.DIAMOND_SWORD,2,"&4&lAngry Particle", "lobbyplugin.angry"),
    PARTICLE_ASH(Particle.ASH,1,Material.BLACK_DYE,3,"&8&lAsh Particle", "lobbyplugin.ash"),
    PARTICLE_CLOUD(Particle.CLOUD,1,Material.GRAY_DYE,4,"&f&lCloud Particle", "lobbyplugin.cloud"),
    PARTICLE_CRIT(Particle.CRIT,1,Material.GOLDEN_SWORD,5,"&7&lCrit Particle", "lobbyplugin.crit"),
    PARTICLE_CRIT_MAGIC(Particle.CRIT_MAGIC,1,Material.POTION,6,"&5&lCrit-Magic Particle", "lobbyplugin.crit.magic"),
    PARTICLE_ENCHANTMENT(Particle.ENCHANTMENT_TABLE,1,Material.ENCHANTED_BOOK,7,"&b&lEnchantment Particle", "lobbyplugin.enchantment"),
    PARTICLE_NOTE(Particle.NOTE,1,Material.NOTE_BLOCK,8,"&2&lNote Particle", "lobbyplugin.note");

    private final Particle particle;
    private final int count;
    private final Material menuItem;
    private final int slot;
    private final String itemName;
    private final String permission;

    /**
     * LobbyParticles Enum
     * @param particle Particle
     * @param count int
     * @param menuItem Material
     * @param slot int
     * @param itemName String
     * @param permission String
     */
    LobbyParticles(Particle particle, int count, Material menuItem, int slot, String itemName, String permission) {
        this.particle = particle;
        this.count = count;
        this.menuItem = menuItem;
        this.slot = slot;
        this.itemName = itemName;
        this.permission = permission;
    }

    public Particle getParticle() {
        return particle;
    }
    public int getCount() {
        return count;
    }
    public Material getMenuItem() {
        return menuItem;
    }
    public int getSlot() {
        return slot;
    }
    public String getItemName() {
        return itemName;
    }
    public String getPermission() {
        return permission;
    }
}
