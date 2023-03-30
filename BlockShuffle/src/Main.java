package com.learnspigot.blockshuffle;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class Main extends JavaPlugin {

    private final List<Material> validBlocks = new ArrayList<>();
    // a list of valid blocks that players can stand on

    private final HashMap<UUID, Material> playerBlocks = new HashMap<>();
    // a list of players who have a pending block to be found

    private boolean running = false;
    // a boolean which dictates if the minigames is running or not

    @Override
    public void onEnable() {

        for (Material material : Material.values()) {
            if (material.isBlock()) validBlocks.add(material);
        }
        // add all valid blocks to the list

        getCommand("shuffle").setExecutor(new ShuffleCommand(this));
        // register the command class

        Bukkit.getScheduler().runTaskTimer(this, new Runnable() {

            private int secondsUntilNextRound = 5 * 60;
            // store the time until next round

            @Override
            public void run() {

                if (!isRunning()) { // if the minigame is not running, reset any data
                    if (!playerBlocks.isEmpty()) playerBlocks.clear();
                    secondsUntilNextRound = 5 * 60;
                    return;
                }

                if (secondsUntilNextRound == 5 * 60) { // if the round has just started
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        Material random = validBlocks.get(new Random().nextInt(validBlocks.size()));
                        playerBlocks.put(player.getUniqueId(), random);
                        player.sendMessage(ChatColor.AQUA + "You must find and stand on a " + random.name());
                        // assign the player a random block
                    }
                } else if (playerBlocks.isEmpty()) { // if everyone has found their block
                    secondsUntilNextRound = 5 * 60;
                    broadcast(ChatColor.GOLD + "Everyone has found their block!");
                    // end the game
                    return;
                }

                if (secondsUntilNextRound == 0) { // if the round is over
                    for (UUID uuid : playerBlocks.keySet()) {
                        if (Bukkit.getPlayer(uuid) == null) continue;
                        broadcast(ChatColor.RED + Bukkit.getPlayer(uuid).getName() + " has failed to find their block!");
                    }
                    setRunning(false);
                    // display the losers, and stop the game

                } else {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        if (playerBlocks.containsKey(player.getUniqueId())) { // if the player has a pending block
                            if (player.getLocation().getBlock().getRelative(0, -1, 0).getType() == playerBlocks.get(player.getUniqueId())) {
                                // if the player is standing on the block
                                broadcast(ChatColor.GREEN + player.getName() + " has found their block!");
                                playerBlocks.remove(player.getUniqueId());
                                // remove the pending block
                            }
                        }

                        if (secondsUntilNextRound <= 30 && !(secondsUntilNextRound % 5 != 0 && secondsUntilNextRound > 5)) {
                            broadcast(ChatColor.YELLOW + "You have " + secondsUntilNextRound + " seconds remaining!");
                            // display when there are 30, 25, 20, 15, 10, 5, 4, 3, 2 and 1 seconds left
                        }
                    }
                }

                secondsUntilNextRound--;
                // update the time
            }
        }, 20, 20);

    }

    public boolean isRunning() { return running; }
    public void setRunning(boolean running) { this.running = running; }

    private void broadcast(String msg) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(msg);
        }
    }
}
