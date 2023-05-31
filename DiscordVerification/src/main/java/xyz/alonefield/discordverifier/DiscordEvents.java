package xyz.alonefield.discordverifier;

import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.alonefield.discordverifier.api.AsyncPlayerVerifyEvent;
import xyz.alonefield.discordverifier.api.DiscordVerifierAPI;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

final class DiscordEvents extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (!event.getName().equals("verify")) return;
        if (!event.isFromGuild()) return;
        String channelId = DiscordVerifier.getInstance().getConfig().getString("discord.command-channel");
        if (channelId.isEmpty()) {
            attemptVerify(event);
            return;
        }
        if (event.getChannel().getId().equals(channelId)) {
            attemptVerify(event);
            return;
        }
        event.reply("Invalid channel to verify in.").queue();
    }

    private void attemptVerify(SlashCommandInteractionEvent e) {
        if (!e.isFromGuild()) {
            e.reply("This command can only be used in a guild").queue();
            return;
        }
        e.deferReply(true).queue();
        // Check the code argument
        OptionMapping codeOption = e.getOption("code");
        if (codeOption == null) {
            e.getHook().editOriginal("You must provide a code!").queue(); // Impossible to reach
            return;
        }
        String code = codeOption.getAsString();
        AtomicBoolean failed = new AtomicBoolean(true);
        // Check the code
        DiscordVerifier.getDiscordCodes().forEach((uuid, data) -> {
            boolean caseSensitive = DiscordVerifier.getInstance().getConfig().getBoolean("should-code-be-case-sensitive");
            if (!caseSensitive) {
                if (data.getLeft().equalsIgnoreCase(code)) {
                    setSuccessful(e, code, failed, uuid);
                }
                return;
            }
            if (data.getLeft().equals(code)) {
                setSuccessful(e, code, failed, uuid);
            }
        });
        if (failed.get()) {
            e.getHook().editOriginal(DiscordVerifier.getInstance().getConfig().getString("messages.code-invalid")).queue();
            return;
        }
    }

    private void setSuccessful(SlashCommandInteractionEvent e, String code, AtomicBoolean failed, UUID uuid) {
        e.getHook().editOriginal(DiscordVerifier.getInstance().getConfig().getString("messages.verification-successful-discord")).queue();
        DiscordVerifier.getDiscordCodes().remove(uuid);
        final Player player = Bukkit.getPlayer(uuid);
        failed.set(false);
        attemptSendMCMessage(uuid);
        Bukkit.getScheduler().runTaskAsynchronously(DiscordVerifier.getInstance(), () -> {Bukkit.getPluginManager().callEvent(new AsyncPlayerVerifyEvent(uuid, e.getId(), code));});


        Role given = Objects.requireNonNull(e.getGuild()).getRoleById(DiscordVerifier.getInstance().getConfig().getString("discord.role-given"));
        Role removed = e.getGuild().getRoleById(DiscordVerifier.getInstance().getConfig().getString("discord.role-removed"));
        String name = player.getName();

        if (given != null) {
            e.getGuild().addRoleToMember(Objects.requireNonNull(e.getMember()), given).queue();
        }
        if (removed != null) {
            e.getGuild().removeRoleFromMember(Objects.requireNonNull(e.getMember()), removed).queue();
        }
        if (DiscordVerifier.getInstance().getConfig().getBoolean("discord.sync-name")){
            Objects.requireNonNull(Objects.requireNonNull(e.getMember()).modifyNickname(name)).queue();
        }

        List<String> commands = DiscordVerifier.getInstance().getConfig().getStringList("Minecraft.Command");

        Bukkit.getScheduler().runTask(DiscordVerifier.getInstance(), () -> {
            for (String cmd : commands) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replace("{player}", name));
            }
        });
        //Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("{player}", name));
    }

    private void attemptSendMCMessage(@NotNull UUID uuid) {
        final Player player = DiscordVerifier.getInstance().getServer().getPlayer(uuid);
        if (player == null || !player.isOnline() || !player.isValid()) return;
        player.sendMessage(DiscordVerifierAPI.cc(DiscordVerifier.getInstance().getConfig().getString("messages.verification-successful-mc")));
    }

}
