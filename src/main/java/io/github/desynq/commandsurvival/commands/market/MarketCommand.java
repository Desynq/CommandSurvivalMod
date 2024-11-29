package io.github.desynq.commandsurvival.commands.market;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.github.desynq.commandsurvival.systems.economy.MarketableItem;
import net.minecraft.commands.CommandSourceStack;

import java.util.concurrent.CompletableFuture;

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
                    .suggests(MarketCommand::suggestItemName)
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
                            .then(argument("days", IntegerArgumentType.integer(0))
                                    .executes(EstimateSubcommand::execute)
                            )
                    );

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(literal("market")
                .then(literal("item")
                        .then(argument("item_category", StringArgumentType.string())
                                .suggests(MarketCommand::suggestItemCategory)
                                .then(ITEM_COMMAND)
                        )
                )
        );
    }



    private static CompletableFuture<Suggestions> suggestItemCategory(CommandContext<CommandSourceStack> command, SuggestionsBuilder builder) {
        MarketableItem.getCategories().forEach(builder::suggest);
        return builder.buildFuture();
    }

    private static CompletableFuture<Suggestions> suggestItemName(CommandContext<CommandSourceStack> command, SuggestionsBuilder builder) {
        String category = StringArgumentType.getString(command, "item_category");
        MarketableItem.getNamesFromCategory(category).forEach(builder::suggest);
        return builder.buildFuture();
    }
}
