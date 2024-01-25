package com.tqqn.chatcolors.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class ChatUtils {

    public static Component format(String message) {
        MiniMessage miniMessage = MiniMessage.builder().build();
        return miniMessage.deserialize(message);
    }
}
