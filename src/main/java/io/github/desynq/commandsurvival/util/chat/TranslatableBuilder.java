package io.github.desynq.commandsurvival.util.chat;

import io.github.desynq.commandsurvival.systems.money.Money;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.ArrayList;
import java.util.List;

public class TranslatableBuilder {
    private final List<MutableComponent> components = new ArrayList<>();

    public TranslatableBuilder() {}

    public TranslatableBuilder next(String s, ChatFormatting... formats) {
        components.add(Component.literal(s).withStyle(formats));
        return this;
    }

    public TranslatableBuilder next(int i, ChatFormatting... formats) {
        next(String.valueOf(i), formats);
        return this;
    }

    public TranslatableBuilder next(double d, ChatFormatting... formats) {
        next(String.valueOf(d), formats);
        return this;
    }

    public TranslatableBuilder next(Money money, ChatFormatting... formats) {
        next(money.getDollarString(), formats);
        return this;
    }

    public MutableComponent build(String translationKey) {
        return Component.translatable(translationKey, components.toArray());
    }
}
