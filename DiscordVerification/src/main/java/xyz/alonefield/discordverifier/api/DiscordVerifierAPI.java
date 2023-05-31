package xyz.alonefield.discordverifier.api;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.bukkit.ChatColor;
import xyz.alonefield.discordverifier.DiscordVerifier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * The API for DiscordVerifier
 */
public final class DiscordVerifierAPI {

    private DiscordVerifierAPI() {} // Prevents instantiation
    private static final String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final CompletableFuture<Void> VOID_FUTURE = CompletableFuture.completedFuture(null);

    /**
     * Colorizes a string using the {@link ChatColor#translateAlternateColorCodes(char, String)} method
     * @param message The message to colorize
     * @return The colorized message
     */
    public static @NotNull String cc(@NotNull String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    private static @NotNull CompletableFuture<String> generateString(int length) {
        CompletableFuture<String> future = new CompletableFuture<>();
        CompletableFuture.runAsync(() -> {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < length; i++) {
                sb.append(characters.charAt((int) (Math.random() * characters.length())));
            }
            if (DiscordVerifier.getDiscordCodes().containsValue(sb.toString())) {
                generateNumber(length).thenAccept(future::complete);
                return;
            }
            future.complete(sb.toString());
        });
        return future;
    }

    private static @NotNull CompletableFuture<String> generateNumber(int length) {
        CompletableFuture<String> future = new CompletableFuture<>();
        CompletableFuture.runAsync(() -> {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < length; i++) {
                sb.append(characters.charAt((int) (Math.random() * characters.length())));
            }
            if (DiscordVerifier.getDiscordCodes().containsValue(sb.toString())) {
                generateNumber(length).thenAccept(future::complete);
                return;
            }
            future.complete(sb.toString());
        });
        return future;
    }

    /**
     * Generates a future that will complete with a random string of the specified length
     * @param length The length of the code
     * @return The generated code as a future
     */
    public static @NotNull CompletableFuture<String> generateCode(int length) {
        if (DiscordVerifier.getInstance().getConfig().getBoolean("code-numbers-only")) {
            return generateNumber(length);
        }
        return generateString(length);
    }

    /**
     * Saves a UUID and a discord ID to the database. This method is asynchronous
     * @param uuid The UUID of the player
     * @param discordId The discord ID of the player
     * @return A {@link CompletableFuture} that completes when the operation is done
     */
    public static CompletableFuture<Void> savePlayer(final @NotNull UUID uuid, @NotNull String discordId) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        Bukkit.getScheduler().runTaskAsynchronously(DiscordVerifier.getInstance(), () -> {
            try {
                Connection connection = DiscordVerifier.getDatabaseConnection();
                PreparedStatement statement = connection.prepareStatement("INSERT INTO discord_verifier (uuid, discord_id) VALUES (?, ?) ON DUPLICATE KEY UPDATE discord_id = ?");
                statement.setString(1, uuid.toString());
                statement.setString(2, discordId);
                statement.setString(3, discordId);
                statement.execute();
                future.complete(null);
            }
            catch (SQLException e) {
                e.printStackTrace();
                future.completeExceptionally(e);
                DiscordVerifier.getInstance().getLogger().severe("Failed to save player " + uuid + " to database");
            }
        });
        return future;
    }

    /**
     * Checks if a player is verified. This method is asynchronous
     * @param uuid The UUID of the player
     * @return A {@link CompletableFuture} that completes when the operation is done. The result is true if the player is verified, false otherwise
     */
    public static CompletableFuture<Boolean> isPlayerVerified(final @NotNull UUID uuid) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        Bukkit.getScheduler().runTaskAsynchronously(DiscordVerifier.getInstance(), () -> {
            try {
                Connection connection = DiscordVerifier.getDatabaseConnection();
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM discord_verifier WHERE uuid = ?");
                statement.setString(1, uuid.toString());
                future.complete(statement.executeQuery().next());
            }
            catch (SQLException e) {
                e.printStackTrace();
                future.completeExceptionally(e);
                DiscordVerifier.getInstance().getLogger().severe("Failed to check if player " + uuid + " is verified");
            }
        });
        return future;
    }

    /**
     * Removes a player from the database. This method is asynchronous
     * @param uuid The UUID of the player
     * @return A {@link CompletableFuture} that completes when the operation is done
     */
    public static CompletableFuture<Void> removePlayer(final @NotNull UUID uuid) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        Bukkit.getScheduler().runTaskAsynchronously(DiscordVerifier.getInstance(), () -> {
            try {
                Connection connection = DiscordVerifier.getDatabaseConnection();
                PreparedStatement statement = connection.prepareStatement("DELETE FROM discord_verifier WHERE uuid = ?");
                statement.setString(1, uuid.toString());
                statement.execute();
                future.complete(null);
            }
            catch (SQLException e) {
                e.printStackTrace();
                future.completeExceptionally(e);
                DiscordVerifier.getInstance().getLogger().severe("Failed to remove player " + uuid + " from database");
            }
        });
        return future;
    }


    /**
     * Gets the discord ID of a player. This method is asynchronous
     * @param uuid The UUID of the player
     * @return A {@link CompletableFuture} that completes when the operation is done. The result is the discord ID of the player, or null if the player is not verified
     */
    public static CompletableFuture<String> getDiscordId(final @NotNull UUID uuid) {
        CompletableFuture<String> future = new CompletableFuture<>();
        Bukkit.getScheduler().runTaskAsynchronously(DiscordVerifier.getInstance(), () -> {
            try {
                Connection connection = DiscordVerifier.getDatabaseConnection();
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM discord_verifier WHERE uuid = ?");
                statement.setString(1, uuid.toString());
                if (statement.executeQuery().next()) {
                    future.complete(statement.getResultSet().getString("discord_id"));
                } else {
                    future.complete(null);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                future.completeExceptionally(e);
                DiscordVerifier.getInstance().getLogger().severe("Failed to get discord ID of player " + uuid);
            }
        });
        return future;
    }

    public static <T> T get(@NotNull CompletableFuture<T> future) {
        try {
            return future.get();
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
