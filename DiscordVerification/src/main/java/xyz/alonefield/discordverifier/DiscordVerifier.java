package xyz.alonefield.discordverifier;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.alonefield.discordverifier.api.DiscordVerifierAPI;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class DiscordVerifier extends JavaPlugin {

    private static DiscordVerifier instance;
    private static Connection dbConnection;
    private static final Map<UUID, Pair<String, Integer>> discordCodes = new ConcurrentHashMap<>();
    private static JDA discordClient;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        if (instance == null) instance = this;
        if (discordClient == null) discordClient = JDABuilder.create(getConfig().getString("discord.token"), GatewayIntent.getIntents(GatewayIntent.ALL_INTENTS)).addEventListeners(new DiscordEvents()).build();

        discordClient.upsertCommand(
                Commands.slash(
                        "verify",
                                "Verify your Minecraft account with your Discord account"
                        ).addOption(OptionType.STRING, "code", "The code you received in-game", true)
                ).queue();
        setupLoop();
        setupDatabase();
        getCommand("verify").setExecutor(new MinecraftVerifyCommand());
    }


    private void setupLoop() {
        getLogger().info("Starting data loop");
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> {
            discordCodes.forEach((uuid, data) -> {
                if (data.getRight() <= 0) {
                    discordCodes.remove(uuid);
                    final Player player = Bukkit.getPlayer(uuid);
                    if (player != null && player.isOnline() && player.isValid()) {
                        player.sendMessage(DiscordVerifierAPI.cc(getConfig().getString("messages.code-expired")));
                    }
                }
                else {
                    discordCodes.put(uuid, Pair.of(data.getLeft(), data.getRight() - 1));
                }
            });
        }, 0, 20);
        getLogger().info("Data loop started");
    }

    private void setupDatabase() {
        try {
            getDatabaseConnection().prepareStatement("""
                CREATE TABLE IF NOT EXISTS DATA(
                ID INT(36) NOT NULL,
                SOMETHING VARCHAR(32) NOT NULL,
                PRIMARY KEY(ID)
            )
            """);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static DiscordVerifier getInstance() {
        return instance;
    }

    public static Map<UUID, Pair<String, Integer>> getDiscordCodes() {
        return discordCodes;
    }

    public static JDA getDiscordClient() {
        return discordClient;
    }

    public static Connection getDatabaseConnection() {
        if (dbConnection == null) {
            try {
                Class.forName("org.sqlite.JDBC");
                dbConnection = DriverManager.getConnection("jdbc:sqlite:" + instance.getDataFolder().getAbsolutePath() + "/database.db");
            }
            catch (Exception e) {
                instance.getLogger().severe("Failed to connect to database");
                e.printStackTrace();
            }
        }
        return dbConnection;
    }
    }
