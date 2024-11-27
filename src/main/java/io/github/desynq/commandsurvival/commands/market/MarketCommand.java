package io.github.desynq.commandsurvival.commands.market;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class MarketCommand {

    // market item <item_category> <item_name>
    // market item <item_category> <item_name> sell all
    // market item <item_category> <item_name> sell <amount>
    // market item <item_category> <item_name> buy <amount>
    // market item <item_category> <item_name> estimate <day>
    private static final RequiredArgumentBuilder<CommandSourceStack, String> ITEM_COMMAND =
            argument("item_name", StringArgumentType.string())
                    //.suggests()
                    //.executes()
                    .then(literal("sell")
                            .then(literal("all")
                                    //.executes()
                            )
                            .then(argument("amount", IntegerArgumentType.integer(0))
                                    //.executes()
                            )
                    )
                    .then(literal("buy")
                            .then(argument("amount", IntegerArgumentType.integer(0))
                                    //.executes()
                            )
                    )
                    .then(literal("estimate")
                            .then(argument("day", IntegerArgumentType.integer(0))
                                    //.executes()
                            )
                    );

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(literal("market")
                .then(literal("item")
                        .then(argument("item_category", StringArgumentType.string())
                                .then(ITEM_COMMAND)
                        )
                )
        );
    }
}
