package ls.project.welcome;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.meta.FireworkMeta;

public class JoinEvent implements Listener {

    //Main listener class

    /*Comprises off
      - Message sent on first join
      - Message sent on join
      - Title message
      - Customisable firework
      - broadcast
     */
    private final Main main = Main.instance;

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player player = e.getPlayer();
        firstJoin(player);
        welcomeMessage(player);
        titleWelcome(player);
        Firework(player);
        broadcast(player);
    }


    //special message for first join
    public void firstJoin(Player player){
        if (!main.getConfig().getBoolean("FirstJoin.Toggle")) return;  //checking for the toggle
        if (player.hasPlayedBefore()) return;
        String message = main.getConfig().getString("FirstJoin.Message");  //getting the message

        if (message == null) return; //message null check
        String HexColour = message.substring(0, 7); //getting hex colour code from message, hex colours always take 7 characters including the #
        String message1 = message.substring(7);

        player.sendMessage(ChatColor.of(HexColour) + message1);
    }


    //regular join messages
    public void welcomeMessage(Player player){

        if (!main.getConfig().getBoolean("Join.Toggle")) return;  //checking for the toggle
        String message = main.getConfig().getString("Join.Message");  //getting the message

        if (message == null) return; //message null check
        String HexColour = message.substring(0, 7); //getting hex colour code from message, hex colours always take 7 characters including the #
        String message1 = message.substring(7);

        player.sendMessage(ChatColor.of(HexColour) + message1);
    }


    //Title message
    public void titleWelcome(Player player){
        if (!main.getConfig().getBoolean("Title.Toggle")) return;  //checking for the toggle
        String title = main.getConfig().getString("Title.Message");
        String subtitle = main.getConfig().getString("Title.Subtitle");

        if (title == null) return;
        String HexColour1 = title.substring(0, 7); //getting hex colour code from message, hex colours always take 7 characters including the #
        String message1 = title.substring(7);

        String HexColour2 = subtitle.substring(0, 7); //getting hex colour code from message, hex colours always take 7 characters including the #
        String message2 = subtitle.substring(7);

        player.sendTitle(ChatColor.of(HexColour1) + message1, ChatColor.of(HexColour2) + message2);
    }


    //Firework
    public void Firework(Player player){
        if (!main.getConfig().getBoolean("Firework.toggle")) return;

        Firework firework = player.getWorld().spawn(player.getLocation(), Firework.class);
        FireworkMeta meta = firework.getFireworkMeta();

        meta.addEffect(FireworkEffect.builder().withColor(Color.RED).withColor(Color.AQUA).with(FireworkEffect.Type.BURST).withFlicker().build());
        meta.setPower(2);
        firework.setFireworkMeta(meta);
    }


    //Auto broadcast
    public void broadcast(Player player){
        if (!main.getConfig().getBoolean("Broadcast.toggle")) return;

        String message = main.getConfig().getString("Broadcast.message");
        if (message == null) return; //message null check
        String HexColour = message.substring(0, 7); //getting hex colour code from message, hex colours always take 7 characters including the #
        String message1 = message.substring(7);

        String broadcast = message1.replace("{player}", player.getName());
        Bukkit.broadcastMessage(ChatColor.of(HexColour) + broadcast);
    }
}
