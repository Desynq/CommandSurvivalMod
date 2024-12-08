package io.github.desynq.commandsurvival.commands.market;

import com.mojang.brigadier.context.CommandContext;
import org.jetbrains.annotations.NotNull;

public enum MarketCommandArgument {
    ITEM_CATEGORY("item_category", String.class),
    ITEM_NAME("item_name", String.class),
    AMOUNT("amount", int.class),
    ;

    private final String argumentName;
    private final Class<?> clazz;

    MarketCommandArgument(String argumentName, Class<?> clazz) {
        this.argumentName = argumentName;
        this.clazz = clazz;
    }

    public String getName() {
        return argumentName;
    }

    @SuppressWarnings("unchecked")
    public <V> V get(final @NotNull CommandContext<?> context) {
        return context.getArgument(argumentName, (Class<V>) clazz);
    }
}
