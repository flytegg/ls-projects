package com.tqqn.particles.menus;

import com.tqqn.particles.Particles;
import com.tqqn.particles.utils.Color;
import com.tqqn.particles.particles.LobbyParticles;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;

public class ParticleMenu {

    private Inventory inventory;

    private final HashMap<ItemStack, LobbyParticles> loadedParticlesMaterial = new HashMap<>();

    private final Particles plugin;

    /**
     * Main Constructor for class
     * @param plugin MainClass
     */
    public ParticleMenu(Particles plugin) {
        this.plugin = plugin;
    }

    /**
     * Method to add items to the private inventory variable.
     */
    private void addItemsToInventory() {
        for (LobbyParticles lobbyParticles : LobbyParticles.values()) {
            inventory.setItem(lobbyParticles.getSlot(), createGuiItem(lobbyParticles.getMenuItem(), lobbyParticles.getItemName()));
            loadedParticlesMaterial.put(createGuiItem(lobbyParticles.getMenuItem(),lobbyParticles.getItemName()), lobbyParticles);
        }
        inventory.setItem(inventory.getSize()-1, createGuiItem(Material.BARRIER,"&cTurn off Particle"));
    }

    /**
     * setups the inventory with desired slots and sizes.
     */
    public void setUpParticleMenu() {
        int size = plugin.getParticleManager().getParticlesMapSize()+1;

        size = (int) (9 * Math.ceil(size/9.0));
        this.inventory = Bukkit.createInventory(null, size, Color.translate("&6Particle Menu"));

        addItemsToInventory();
    }

    /**
     * ItemBuilder for custom name.
     * @param material Material
     * @param name String name
     * @return itemStack
     */
    public ItemStack createGuiItem(Material material, String name) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName(Color.translate(name));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    /**
     * Opens the private inventory variable for player.
     * @param player Player
     */
    public void openInventory(Player player) {
        player.openInventory(inventory);
    }

    /**
     * Checks if the itemstack is in the map.
     * @param itemStack ItemStack
     * @return true of false
     */
    public boolean doesMaterialExistInMap(ItemStack itemStack) {
        return loadedParticlesMaterial.containsKey(itemStack);
    }

    /**
     * Returns LobbyParticles that is with the itemstack key.
     * @param itemStack ItemStack
     * @return LobbyParticles
     */
    public LobbyParticles getLobbyParticlesFromMap(ItemStack itemStack) {
        return loadedParticlesMaterial.get(itemStack);
    }
}
