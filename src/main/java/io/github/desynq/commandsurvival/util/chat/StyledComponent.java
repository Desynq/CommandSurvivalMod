package io.github.desynq.commandsurvival.util.chat;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public record StyledComponent(String string, ChatFormatting... formats) {

    public MutableComponent get() {
        return Component.literal(string).withStyle(formats);
    }
}
