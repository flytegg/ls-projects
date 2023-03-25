package ls.project.chatgames;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class Main extends JavaPlugin {

    private final List<ConfigurationSection> keys = new ArrayList<>();

    public ChatGames games;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        games = new ChatGames(this);

        getCommand("games").setExecutor(games);
        Bukkit.getPluginManager().registerEvents(new ChatListener(this), this);

        //Retrieving the number of questions, will use it in future to get the path of new questions and answers
        ConfigurationSection questionSection = getConfig().getConfigurationSection("Questions");

        for (String questionNumber : questionSection.getKeys(false)) {

            ConfigurationSection question = questionSection.getConfigurationSection(questionNumber);

            keys.add(question);
        }

    }

    public ChatGames getGames(){ return games; }

    public List<ConfigurationSection> getKeys(){ return keys;}
}
