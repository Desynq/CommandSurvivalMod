package io.github.desynq.commandsurvival.util.chat;

import io.github.desynq.commandsurvival.systems.money.Money;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
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

    /**
     * - {@code RED, itemName, GRAY} will make itemName have the color gray despite the
     * default style color being RED<br>
     * - {@code RED, "Specified item"} will make the string have the color red as the
     * default style was defined as RED
     */
    public ComponentBuilder(Object... args) {
        Arrays.stream(args).forEach(this::manageObject);
    }

    /**
     * Builds a {@link MutableComponent} by processing the provided arguments.
     *
     * @param args the objects to be used in creating the component; can include strings, styles, or components
     * @return a {@link MutableComponent} constructed from the specified arguments
     * @throws IllegalStateException if no components are added during the build process
     */
    public static MutableComponent build(Object... args) {
        return new ComponentBuilder(args).build();
    }

    private void manageObject(Object object) {
        if (object instanceof ChatFormatting format) {
            applyFormatToLastComponent(format);
        }
        else if (object instanceof Style style) {
            applyStyleToLastComponent(style);
        }
        else if (object instanceof Component) {
            addComponent((MutableComponent) object);
        }
        else {
            addStringWithDefaultStyle(String.valueOf(object));
        }
    }

    /**
     * Applies the specified chat format to the last component in the list of components.
     * If the component list is empty, the format is applied to the default style instead.
     *
     * @param format the chat format to apply to the last component or the default style
     */
    private void applyFormatToLastComponent(ChatFormatting format) {
        if (components.isEmpty()) {
            defaultStyle.applyFormat(format);
            return;
        }
        components.get(components.size() - 1).withStyle(format);
    }

    private void applyStyleToLastComponent(Style style) {
        if (components.isEmpty()) {
            defaultStyle = style.applyTo(defaultStyle);
            return;
        }
        components.get(components.size() - 1).withStyle(style);
    }

    private void addComponent(MutableComponent component) {
        components.add(component);
    }

    private void addStringWithDefaultStyle(String string) {
        components.add(Component.literal(string).withStyle(defaultStyle));
    }

    //------------------------------------------------------------------------------------------------------------------
    // Builder Methods
    //------------------------------------------------------------------------------------------------------------------

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
        next(money.toString(), formats);
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
            throw new IllegalStateException("No components added");
        }
        return Component.translatable(translationKey, components.toArray());
    }

    public MutableComponent build() {
        if (components.isEmpty()) {
            throw new IllegalStateException("No components added");
        }
        MutableComponent base = components.get(0);
        for (int i = 1; i < components.size(); i++) {
            base.append(components.get(i));
        }
        return base;
    }
}
