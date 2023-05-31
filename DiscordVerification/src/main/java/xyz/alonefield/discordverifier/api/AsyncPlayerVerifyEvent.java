package xyz.alonefield.discordverifier.api;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

public class AsyncPlayerVerifyEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final UUID player;
    private final String codeUsed;
    private final String discordId;

    public AsyncPlayerVerifyEvent(UUID player, String discordId, String codeUsed) {
        super(true); // Async!!!!
        this.player = player;
        this.codeUsed = codeUsed;
        this.discordId = discordId;
    }

    public UUID getPlayer() {
        return player;
    }

    public String getCodeUsed() {
        return codeUsed;
    }

    public String getDiscordId() {
        return discordId;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
