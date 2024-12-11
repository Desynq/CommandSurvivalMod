package io.github.desynq.commandsurvival.commands.admin;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.selector.EntitySelector;
import org.jetbrains.annotations.NotNull;

public enum AdminCommandArgument {
    SECONDS("seconds",
            int.class,
            IntegerArgumentType.integer(1, 60)
    ),
    ENTITY("entity",
            EntitySelector.class,
            EntityArgument.entity()
    ),
    USERNAME("username",
            String.class,
            StringArgumentType.word()
    ),
    MONEY("money_amount",
            String.class,
            StringArgumentType.string()
    ),
    ;

    private final String argumentName;
    private final Class<?> objectClass;
    private final ArgumentType<?> argumentType;

    <T> AdminCommandArgument(String argumentName, Class<T> objectClass, ArgumentType<T> argumentType) {
        this.argumentName = argumentName;
        this.objectClass = objectClass;
        this.argumentType = argumentType;
    }

    public String getName() {
        return argumentName;
    }

    /**
     * Retrieves an argument from the given CommandContext based on the predefined argument name and
     * expected object class.
     *
     * @param <T>     the type of the argument to be retrieved
     * @param context the command context from which the argument is to be retrieved; must not be null
     * @return the argument of type T retrieved from the context
     * @throws IllegalArgumentException if the argument cannot be cast to the expected type
     */
    @SuppressWarnings("unchecked")
    public <T> T get(final @NotNull CommandContext<?> context) {
        return (T) context.getArgument(argumentName, objectClass);
    }

    public ArgumentType<?> argumentType() {
        return argumentType;
    }

    public RequiredArgumentBuilder<CommandSourceStack, ?> argumentBuilder() {
        return Commands.argument(getName(), argumentType());
    }
}
