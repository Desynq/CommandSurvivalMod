package io.github.desynq.commandsurvival.util.chat;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ComponentInserter {
    private final List<String> insertions;
    private int currentIndex = 0;

    public ComponentInserter(String... insertions) {
        this.insertions = new ArrayList<>();
        this.insertions.addAll(Arrays.asList(insertions));
    }

    public MutableComponent next(ChatFormatting... formats) {
        if (currentIndex >= insertions.size()) {
            throw new IndexOutOfBoundsException("No more insertions available");
        }
        return Component.literal(insertions.get(currentIndex++)).withStyle(formats);
    }
}
