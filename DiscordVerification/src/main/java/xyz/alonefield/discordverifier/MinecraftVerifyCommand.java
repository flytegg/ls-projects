package xyz.alonefield.discordverifier;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.alonefield.discordverifier.api.DiscordVerifierAPI;

import java.util.concurrent.CompletableFuture;

final class MinecraftVerifyCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be run by a player");
            return false;
        }
        final Player player = (Player) sender;
        if (hasCode(player)) {
            player.sendMessage(DiscordVerifierAPI.cc(DiscordVerifier.getInstance().getConfig().getString("messages.has-code")));
            return false;
        }
        final CompletableFuture<String> codeTask = DiscordVerifierAPI.generateCode(DiscordVerifier.getInstance().getConfig().getInt("code-length"));
        codeTask.thenRun(() -> {
           String code = DiscordVerifierAPI.get(codeTask);
            DiscordVerifier.getDiscordCodes().put(player.getUniqueId(), Pair.of(code, DiscordVerifier.getInstance().getConfig().getInt("code-timeout")));
            sendCodeMessage(player);
        });
        return true;
    }


    private boolean hasCode(Player player) {
        return DiscordVerifier.getDiscordCodes().get(player.getUniqueId()) != null;
    }

    private void sendCodeMessage(Player player) {
        int time = DiscordVerifier.getDiscordCodes().get(player.getUniqueId()).getRight();
        String code = DiscordVerifier.getDiscordCodes().get(player.getUniqueId()).getLeft();
        String rawMsg = DiscordVerifierAPI.cc(DiscordVerifier.getInstance().getConfig().getString("messages.code-generated"))
                .replace("{code}", code)
                .replace("{time}", String.valueOf(time));
        ComponentBuilder builder = new ComponentBuilder(rawMsg);
        if (DiscordVerifier.getInstance().getConfig().getBoolean("should-minecraft-chat-copyable")) {
            builder.event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, code));
        }
        player.spigot().sendMessage(builder.create());
    }

}
