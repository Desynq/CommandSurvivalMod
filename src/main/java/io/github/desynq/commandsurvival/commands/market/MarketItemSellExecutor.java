package io.github.desynq.commandsurvival.commands.market;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.desynq.commandsurvival.CommandSurvival;
import io.github.desynq.commandsurvival.systems.market.item.MarketableItem;
import io.github.desynq.commandsurvival.systems.market.item.MarketableItemInstancesManager;
import io.github.desynq.commandsurvival.systems.market.manager.SellOperation;
import io.github.desynq.commandsurvival.util.chat.ComponentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import org.jetbrains.annotations.NotNull;

import static net.minecraft.ChatFormatting.*;

public class MarketItemSellExecutor extends MarketItemTransactionExecutor {
    private int amount;
    private SellOperation sellOperation;

    public MarketItemSellExecutor(CommandContext<CommandSourceStack> cc) throws CommandSyntaxException {
        super(cc);
        try {
            amount = MarketCommandArgument.AMOUNT.get(cc);
        }
        catch (IllegalArgumentException e) {
            amount = Integer.MAX_VALUE;
        }

        MarketableItemInstancesManager.getFromName(itemName).ifPresentOrElse(
                this::handleSelling,
                this::handleInvalidMarketableItem
        );
    }

    private void handleSelling(@NotNull MarketableItem marketableItem) {
        try {
            sellOperation = new SellOperation(executor, marketableItem, amount).executeTransaction();
            switch (sellOperation.getResult()) {
                case NEGATIVE_AMOUNT_SPECIFIED -> handleNegativeAmountSpecified();
                case ZERO_AMOUNT_SPECIFIED -> handleZeroAmountSpecified();
                case NO_ITEMS -> handleNoItems();
                case SUCCESS -> handleSuccess();
            }
        } catch (Exception e) {
            CommandSurvival.LOGGER.error("Error while processing transaction: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    private void handleNegativeAmountSpecified() {
        // Cannot specify a negative amount to sell
        messageSelf(RED, "Cannot specify a negative amount to sell.");
    }

    private void handleZeroAmountSpecified() {
        // Cannot specify to sell zero items
        messageSelf(RED, "Cannot specify to sell zero items.");
    }

    private void handleNoItems() {
        // Could not process sell transaction. No items to sell.
        messageSelf(RED, "Could not process sell transaction. No items to sell.");
    }

    private void handleSuccess() {
        // - Circulation: 0.000 -> 64.000
        // - Buy Price: $1.00 -> $2.00
        // - Sell Price: $0.50 -> $1.00
        // - Balance: $1000.00 -> $999.99
        // - Inventory: 64 -> 32
        Style hoverTextStyle = Style.EMPTY.withColor(WHITE);
        MutableComponent hoverText = ComponentBuilder.build(
                hoverTextStyle, "- Circulation: ", sellOperation.getCirculationBefore(), DARK_GRAY, " -> ", sellOperation.getCirculationAfter(), GRAY,
                !sellOperation.isBuyable() ?
                        "" :
                        ComponentBuilder.build(hoverTextStyle, "\n- Buy Price: ", sellOperation.getBuyPriceBefore(), DARK_GRAY, " -> ", sellOperation.getBuyPriceAfter(), GRAY),
                "\n- Sell Price: ", sellOperation.getSellPriceBefore(), DARK_GRAY, " -> ", sellOperation.getSellPriceAfter(), GRAY,
                "\n- Balance: ", sellOperation.getPlayerBalanceBefore(), DARK_GRAY, " -> ", sellOperation.getPlayerBalanceAfter(), GRAY,
                "\n- Inventory: ", sellOperation.getAmountInInventoryBeforeTransaction(), DARK_GRAY, " -> ", sellOperation.getAmountLeftInInventory(), GRAY
        );

        Style style = Style.EMPTY
                .withColor(GREEN)
                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverText));
        // Successfully sold 64x 'diamond'.
        messageSelf(style, "Successfully sold ", sellOperation.getAmountSold(), "x '", itemName, "'.");
    }
}
