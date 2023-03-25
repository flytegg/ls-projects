package ls.project.chatgames;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class ChatGames implements CommandExecutor {

    private Main main;

    public ChatGames(Main main){
        this.main = main;
    }

    public String answer;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player player)) return false;

        player.sendMessage(ChatColor.YELLOW + "Chat Games started!");
        List<ConfigurationSection> keys = main.getKeys();

        Bukkit.getScheduler().scheduleSyncRepeatingTask(main, () -> {

            //getting a random field
            int rand = ThreadLocalRandom.current().nextInt(keys.size());
            ConfigurationSection sec = keys.get(rand);

            //broadcasting question and setting answer to a variable
            Bukkit.broadcastMessage(sec.getString("question"));
            answer = sec.getString("answer");

        }, 0L, 6000L);



        //getting the task id to use it for cancelling in future
        Bukkit.getScheduler().scheduleSyncRepeatingTask(main, () -> {

            answer = null;   //ending the task by nullifying the current answer
            Bukkit.broadcastMessage("Current task set to null");

        }, 1200L, 1200L);


        return false;
    }

    public String getAnswer(){return answer;}

}
