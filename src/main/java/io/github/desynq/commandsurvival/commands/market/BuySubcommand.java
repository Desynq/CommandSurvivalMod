package io.github.desynq.commandsurvival.commands.market;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import io.github.desynq.commandsurvival.commands.PlayerCommandExecutor;
import io.github.desynq.commandsurvival.systems.market.manager.BuyOperation;
import io.github.desynq.commandsurvival.systems.market.manager.MarketManager;
import io.github.desynq.commandsurvival.systems.market.item.MarketableItem;
import io.github.desynq.commandsurvival.systems.market.item.MarketableItemInstancesManager;
import io.github.desynq.commandsurvival.systems.money.Money;
import io.github.desynq.commandsurvival.systems.money.MoneyManager;
import io.github.desynq.commandsurvival.util.chat.ComponentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static net.minecraft.ChatFormatting.*;

public class BuySubcommand extends PlayerCommandExecutor {
    private final int amount;
    private final String itemCategory;
    private final String itemName;
    private BuyOperation buyOperation;

    public BuySubcommand(CommandContext<CommandSourceStack> command) {
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
            case SUCCESS -> printPurchase();
            case NOT_BUYABLE -> handleNotBuyable();
            case NOT_AFFORDABLE -> handleNotAffordable();
        }
    }

    private void printPurchase() {
        // Bought 64x 'diamond' for $1.00
        // - Circulation: 0.000 -> 64.000
        // - Price: $1.00 -> $2.00
        // - Balance: $1000.00 -> $999.00
        executor.sendSystemMessage(new ComponentBuilder(WHITE)
                .next("Bought ")
                .next(amount, YELLOW)
                .next("x '")
                .next(itemName, YELLOW)
                .next("' for ")
                .next(buyOperation.getTotalBuyPrice(), DARK_GREEN)
                .next("\n- Circulation: ")
                .next(String.format("%.3f", buyOperation.getCirculationBefore()), DARK_GRAY)
                .next(" -> ")
                .next(String.format("%.3f", buyOperation.getCirculationAfter()), GRAY)
                .next("\n- Price: ")
                .next(buyOperation.getBuyPriceBefore(), DARK_GRAY)
                .next(" -> ")
                .next(buyOperation.getBuyPriceAfter(), GRAY)
                .next("\n- Balance: ")
                .next(buyOperation.getPlayerBalanceBefore(), DARK_GRAY)
                .next(" -> ")
                .next(buyOperation.getPlayerBalanceAfter(), GRAY)
                .build()
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
    }

    private void handleNotAffordable() {
    }
}
