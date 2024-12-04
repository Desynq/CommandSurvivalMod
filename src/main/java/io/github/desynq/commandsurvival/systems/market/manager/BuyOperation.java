package io.github.desynq.commandsurvival.systems.market.manager;

import dev.latvian.mods.kubejs.core.PlayerKJS;
import io.github.desynq.commandsurvival.systems.market.item.MarketableItem;
import io.github.desynq.commandsurvival.systems.money.Money;
import io.github.desynq.commandsurvival.systems.money.MoneyManager;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BuyOperation {
    private final @NotNull MarketableItem marketableItem;
    private final Player player;
    private final int amount;
    private final BuyResult buyResult;
    private @Nullable Money totalBuyPrice;

    private static final int BEFORE = 0;
    private static final int AFTER = 1;
    private static final int NUMBER_OF_STAGES = 2;

    private final Money[] buyPrice = new Money[NUMBER_OF_STAGES];
    private final Money[] playerBalance = new Money[NUMBER_OF_STAGES];
    private final double[] circulation = new double[NUMBER_OF_STAGES];
    private final Money[] sellPrice = new Money[NUMBER_OF_STAGES];

    public BuyOperation(Player player, @NotNull MarketableItem marketableItem, int amount) {
        this.marketableItem = marketableItem;
        this.player = player;
        this.amount = amount;
        buyResult = performBuyOperation();
    }

    private BuyResult performBuyOperation() {
        playerBalance[BEFORE] = MoneyManager.fromPlayer(player);
        circulation[BEFORE] = marketableItem.getCirculation();
        sellPrice[BEFORE] = marketableItem.getSellPrice();

        if (marketableItem.isNotBuyable()) {
            return BuyResult.NOT_BUYABLE;
        }

        buyPrice[BEFORE] = marketableItem.getBuyPrice().orElseThrow();
        totalBuyPrice = buyPrice[BEFORE].copy().multiply(amount);

        if (playerBalance[BEFORE].compareTo(totalBuyPrice) < 0) {
            return BuyResult.NOT_AFFORDABLE;
        }

        playerBalance[AFTER] = playerBalance[BEFORE].copy().subtract(totalBuyPrice);

        MoneyManager.applyToPlayer(player, playerBalance[AFTER]);
        marketableItem.addToCirculation(-amount);
        ((PlayerKJS) player).kjs$give(marketableItem.itemStack.copyWithCount(amount));

        circulation[AFTER] = marketableItem.getCirculation();
        buyPrice[AFTER] = marketableItem.getBuyPrice().orElseThrow();
        sellPrice[AFTER] = marketableItem.getSellPrice();
        return BuyResult.SUCCESS;
    }

    //------------------------------------------------------------------------------------------------------------------
    // GETTERS
    //------------------------------------------------------------------------------------------------------------------

    private void validateSuccess() {
        if (buyResult != BuyResult.SUCCESS) {
            throw new IllegalStateException("Operation was cancelled, cannot access post-operation data");
        }
    }

    private void validateBuyable() {
        if (buyResult != BuyResult.NOT_BUYABLE) {
            throw new IllegalStateException("Operation was cancelled, item does not have a buy price");
        }
    }

    public BuyResult getResult() {
        return buyResult;
    }

    public Money getTotalBuyPrice() {
        validateBuyable();
        return totalBuyPrice;
    }

    public Money getBuyPriceBefore() {
        validateBuyable();
        return buyPrice[BEFORE];
    }

    public Money getPlayerBalanceBefore() {
        return playerBalance[BEFORE];
    }

    public double getCirculationBefore() {
        return circulation[BEFORE];
    }

    public Money getSellPriceBefore() {
        return sellPrice[BEFORE];
    }

    public Money getBuyPriceAfter() {
        validateSuccess();
        return buyPrice[AFTER];
    }

    public Money getPlayerBalanceAfter() {
        validateSuccess();
        return playerBalance[AFTER];
    }

    public double getCirculationAfter() {
        validateSuccess();
        return circulation[AFTER];
    }

    public Money getSellPriceAfter() {
        validateSuccess();
        return sellPrice[AFTER];
    }
}
