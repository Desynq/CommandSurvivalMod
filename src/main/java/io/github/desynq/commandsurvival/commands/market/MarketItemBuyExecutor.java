package io.github.desynq.commandsurvival.commands.market;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.desynq.commandsurvival.commands.PlayerCommandExecutor;
import io.github.desynq.commandsurvival.systems.market.manager.BuyOperation;
import io.github.desynq.commandsurvival.systems.market.item.MarketableItem;
import io.github.desynq.commandsurvival.systems.market.item.MarketableItemInstancesManager;
import io.github.desynq.commandsurvival.systems.money.Money;
import io.github.desynq.commandsurvival.util.chat.ComponentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.HoverEvent;
import org.jetbrains.annotations.NotNull;

import static net.minecraft.ChatFormatting.*;

public class MarketItemBuyExecutor extends PlayerCommandExecutor {
    private final int amount;
    private final String itemCategory;
    private final String itemName;
    private BuyOperation buyOperation;

    public MarketItemBuyExecutor(CommandContext<CommandSourceStack> command) throws CommandSyntaxException {
        super(command);
        amount = IntegerArgumentType.getInteger(command, "amount");
        itemCategory = StringArgumentType.getString(command, "item_category");
        itemName = StringArgumentType.getString(command, "item_name");

        MarketableItemInstancesManager.getFromName(itemName).ifPresentOrElse(
                this::handleBuying,
                this::handleInvalidMarketableItem
        );
    }

    private void handleBuying(@NotNull MarketableItem marketableItem) {
        buyOperation = new BuyOperation(executor, marketableItem, amount);
        switch (buyOperation.getResult()) {
            case SUCCESS -> handleSuccess();
            case NOT_BUYABLE -> handleNotBuyable();
            case NOT_AFFORDABLE -> handleNotAffordable();
        }
    }

    private void handleSuccess() {
        Money totalBuyPrice = buyOperation.getTotalBuyPrice();
        String circulationBefore = String.format("%.3f", buyOperation.getCirculationBefore());
        String circulationAfter = String.format("%.3f", buyOperation.getCirculationAfter());
        Money buyPriceBefore = buyOperation.getBuyPriceBefore();
        Money buyPriceAfter = buyOperation.getBuyPriceAfter();
        Money sellPriceBefore = buyOperation.getSellPriceBefore();
        Money sellPriceAfter = buyOperation.getSellPriceAfter();
        Money playerBalanceBefore = buyOperation.getPlayerBalanceBefore();
        Money playerBalanceAfter = buyOperation.getPlayerBalanceAfter();

        // - Circulation: 0.000 -> 64.000
        // - Buy Price: $1.00 -> $2.00
        // - Sell Price: $0.50 -> $1.00
        // - Balance: $1000.00 -> $999.00
        HoverEvent hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(WHITE)
                .next("- Circulation: ").next(circulationBefore, DARK_GRAY).next(" -> ").next(circulationAfter, GRAY)
                .next("\n- Buy Price: ").next(buyPriceBefore, DARK_GRAY).next(" -> ").next(buyPriceAfter, GRAY)
                .next("\n- Sell Price: ").next(sellPriceBefore, DARK_GRAY).next(" -> ").next(sellPriceAfter, GRAY)
                .next("\n- Balance: ").next(playerBalanceBefore, DARK_GRAY).next(" -> ").next(playerBalanceAfter, GRAY)
                .build()
        );

        // Bought 64x 'diamond' for $1.00
        executor.sendSystemMessage(new ComponentBuilder(WHITE)
                .next("Bought ").next(amount, YELLOW).next("x '").next(itemName, YELLOW).next("' for ").next(totalBuyPrice, DARK_GREEN)
                .build()
                .withStyle(style -> style.withHoverEvent(hoverEvent))
        );
    }

    private void handleInvalidMarketableItem() {
        executor.sendSystemMessage(new ComponentBuilder()
                .next(itemName, BLUE)
                .next(itemCategory, BLUE)
                .build("commands.market.estimate.failure.invalid_marketable_item")
        );
    }

    private void handleNotBuyable() {
        // Item 'diamond' is not buyable.
        executor.sendSystemMessage(new ComponentBuilder(RED)
                .next("Item '").next(itemName, YELLOW).next("' is not buyable.")
                .build()
        );
    }

    private void handleNotAffordable() {
        Money buyPrice = buyOperation.getBuyPriceBefore();
        Money totalBuyPrice = buyOperation.getTotalBuyPrice();
        Money balance = buyOperation.getPlayerBalanceBefore();
        Money missing = totalBuyPrice.copy().subtract(balance);
        // * Item costs $100.00 per unit, $500.00 total (5 units).
        // * Balance is $400.00, missing $100.00.
        HoverEvent hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(GRAY)
                .next("* Item costs ")
                .next(buyPrice, DARK_GREEN)
                .next(" per unit, ")
                .next(totalBuyPrice, DARK_GREEN)
                .next(" total (")
                .next(amount, YELLOW)
                .next(" units).")
                .next("\n* Balance is ")
                .next(balance, DARK_GREEN)
                .next(", missing ")
                .next(missing, RED)
                .next(".")
                .build()
        );
        // Could not afford item 'diamond'.
        executor.sendSystemMessage(new ComponentBuilder(WHITE)
                .next("Could not afford item '").next(itemName, YELLOW).next("'.")
                .build()
                .withStyle(style -> style.withHoverEvent(hoverEvent))
        );
    }
}
