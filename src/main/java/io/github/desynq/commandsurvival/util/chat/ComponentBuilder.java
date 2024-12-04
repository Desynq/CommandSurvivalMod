package io.github.desynq.commandsurvival.util.chat;

import io.github.desynq.commandsurvival.systems.money.Money;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

public class ComponentBuilder {
    private final List<MutableComponent> components = new ArrayList<>();
    private Style defaultStyle = Style.EMPTY;

    public ComponentBuilder() {}

    public ComponentBuilder(ChatFormatting... defaultFormats) {
        defaultStyle.applyFormats(defaultFormats);
    }

    public ComponentBuilder(Style defaultStyle) {
        this.defaultStyle = defaultStyle;
    }

    public ComponentBuilder(@NotNull UnaryOperator<Style> modifyFunc) {
        modifyFunc.apply(defaultStyle);
    }

    public ComponentBuilder next(String s, ChatFormatting... formats) {
        components.add(Component.literal(s).withStyle(defaultStyle).withStyle(formats));
        return this;
    }

    public ComponentBuilder next(int i, ChatFormatting... formats) {
        next(String.valueOf(i), formats);
        return this;
    }

    public ComponentBuilder next(double d, ChatFormatting... formats) {
        next(String.valueOf(d), formats);
        return this;
    }

    public ComponentBuilder next(@NotNull Money money, ChatFormatting... formats) {
        next(money.getDollarString(), formats);
        return this;
    }


    /**
     * Does not use default styling
     */
    public ComponentBuilder next(Component component, ChatFormatting... formats) {
        components.add(((MutableComponent) component).withStyle(formats));
        return this;
    }



    public MutableComponent build(String translationKey) {
        if (components.isEmpty()) {
            throw new IllegalArgumentException("No components added");
        }
        return Component.translatable(translationKey, components.toArray());
    }

    public MutableComponent build() {
        if (components.isEmpty()) {
            throw new IllegalArgumentException("No components added");
        }
        MutableComponent base = components.get(0);
        for (int i = 1; i < components.size(); i++) {
            base.append(components.get(i));
        }
        return base;
    }
}
