package io.github.desynq.commandsurvival.util.chat;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.Arrays;

public class ComponentHelper {

    public static MutableComponent translatable(
            String translationKey,
            StyledComponent... components
    ) {
        return Component.translatable(
                translationKey,
                Arrays
                        .stream(components)
                        .map(StyledComponent::get)
                        .toArray()
        );
    }

    public static StyledComponent styled(String string, ChatFormatting... formats) {
        return new StyledComponent(string, formats);
    }
}
