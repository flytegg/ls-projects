package ls.project.chatgames;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;


public class ChatListener implements Listener {

    private final Main main;

    public ChatListener(Main main){
        this.main = main;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {

        String message = e.getMessage();

        ItemStack reward = new ItemStack(Material.DIAMOND);

        if (message.equalsIgnoreCase(main.getGames().getAnswer())){   //checking if the message sent by player is the same as the answer
            e.getPlayer().getInventory().addItem(reward);
            Bukkit.broadcastMessage(ChatColor.AQUA + e.getPlayer().getName() + " Has answered the question!");

            main.getGames().answer = null; //setting the answer to null
        }

    }
}
