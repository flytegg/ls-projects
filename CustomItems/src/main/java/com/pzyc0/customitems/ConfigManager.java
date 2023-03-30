package com.pzyc0.customitems;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ConfigManager {
    private final CustomItems mainInstance;

    private final HashMap<String, ItemStack> customItems;

    public ConfigManager(CustomItems mainInstance){
        this.mainInstance = mainInstance;
        customItems = new HashMap<>();
        loadItemStacks();
    }
    public void saveItem(ItemStack itemStack, String itemName){
        //Save the Item via the command
        mainInstance.getYmlDataFile().set(itemName, itemStack);
        customItems.put(itemName, itemStack);
    }
    public ItemStack getItem(String name){
        //Get the ItemStack with the name, if it doesn't find anything, it returns null because of the HashMap
        return customItems.get(name);
    }

    private void loadItemStacks(){
        //Add all the ItemStacks in the Config and the custom file to the HashMap
        //'final' means that the value of the Variable won't and can't change
        for(String itemStackName : mainInstance.getYmlDataFile().getConfigurationSection("").getKeys(false)){
            customItems.put(itemStackName, mainInstance.getYmlDataFile().getItemStack(itemStackName));
        }
        for(String itemStackFromConfigName : mainInstance.getConfig().getConfigurationSection("items").getKeys(false)){
            final ItemStack itemStack = new ItemStack(Material.AIR);
            final FileConfiguration config = mainInstance.getConfig();
            final String itemPath = "items."+itemStackFromConfigName;
            if(config.getString(itemPath+".material") != null) {
                itemStack.setType(Material.valueOf(config.getString(itemPath+ ".material")));
            }
            if(config.getInt(itemPath+".amount") != 0 && config.getInt(itemPath+".amount") < 65){
                itemStack.setAmount(mainInstance.getConfig().getInt(itemPath+".amount"));
            }
            if(config.getConfigurationSection(itemPath+".meta") != null) {
                final ItemMeta itemMeta = itemStack.getItemMeta();
                if(config.getString(itemPath+".meta.displayname") != null) {
                    itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', config.getString(itemPath+".meta.displayname")));
                }
                final List<String> loreList = new ArrayList<>();
                for(String lore : config.getStringList(itemPath+".meta.lore")){
                    loreList.add(ChatColor.translateAlternateColorCodes('&', lore));
                }
                itemMeta.setLore(loreList);
                itemMeta.setUnbreakable(config.getBoolean(itemPath+".meta.unbreakable"));
                itemStack.setItemMeta(itemMeta);
            }
            customItems.put(itemStackFromConfigName, itemStack);
        }
    }
    public void saveFile(){
        try {
            mainInstance.getYmlDataFile().save(mainInstance.getDatafile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String[] getItemsAsArray(){
        return customItems.keySet().toArray(new String[0]);
    }
}
