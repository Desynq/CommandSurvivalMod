package io.github.desynq.commandsurvival.commands.market;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.github.desynq.commandsurvival.systems.market.item.MarketableItemInstancesManager;
import net.minecraft.commands.CommandSourceStack;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class MarketCommand {

    // market item <item_category> <item_name> info
    // market item <item_category> <item_name> sell all
    // market item <item_category> <item_name> sell <amount>
    // market item <item_category> <item_name> buy <amount>
    // market item <item_category> <item_name> estimate <day>
    private static final RequiredArgumentBuilder<CommandSourceStack, String> ITEM_COMMAND =
            argument("item_name", StringArgumentType.string())
                    .suggests(MarketCommand::suggestItemName)
                    .then(literal("sell")
                            .then(literal("all")
                                    .executes(cc ->
                                            new MarketItemSellExecutor(cc).getResult())
                            )
                            .then(argument("amount", IntegerArgumentType.integer(1))
                                    .executes(cc ->
                                            new MarketItemSellExecutor(cc).getResult())
                            )
                    )
                    .then(literal("buy")
                            .then(argument("amount", IntegerArgumentType.integer(1))
                                    .executes(cc ->
                                            new MarketItemBuyExecutor(cc).getResult())
                            )
                    )
                    .then(literal("estimate")
                            .then(argument("days", IntegerArgumentType.integer(0))
                                    .executes(MarketItemEstimateExecutor::execute)
                            )
                    )
                    .then(literal("info")
                            //.executes()
                    );

    public static void register(@NotNull CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(literal("market")
                .requires(CommandSourceStack::isPlayer)
                .then(literal("item")
                        .then(argument("item_category", StringArgumentType.string())
                                .suggests(MarketCommand::suggestItemCategory)
                                .then(ITEM_COMMAND)
                        )
                )
        );
    }


    private static CompletableFuture<Suggestions> suggestItemCategory(CommandContext<CommandSourceStack> cc,
                                                                      @NotNull SuggestionsBuilder builder) {
        MarketableItemInstancesManager.getCategories().forEach(builder::suggest);
        return builder.buildFuture();
    }

    private static CompletableFuture<Suggestions> suggestItemName(CommandContext<CommandSourceStack> cc,
                                                                  @NotNull SuggestionsBuilder builder) {
        String category = StringArgumentType.getString(cc, "item_category");
        MarketableItemInstancesManager.getNamesFromCategory(category).forEach(builder::suggest);
        return builder.buildFuture();
    }
}
