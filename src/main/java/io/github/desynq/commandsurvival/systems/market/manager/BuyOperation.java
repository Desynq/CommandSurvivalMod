package io.github.desynq.commandsurvival.systems.market.manager;

import dev.latvian.mods.kubejs.core.PlayerKJS;
import io.github.desynq.commandsurvival.systems.market.item.MarketableItem;
import io.github.desynq.commandsurvival.systems.money.MoneyManager;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

public final class BuyOperation extends Transaction<BuyOperation> {

    public BuyOperation(Player player, @NotNull MarketableItem marketableItem, int amount) {
        super(player, marketableItem, amount);
    }

    @Override
    protected BuyResult performTransaction() {
        initializeBeforeBuyOperation();
        if (marketableItem.isNotBuyable()) {
            return BuyResult.NOT_BUYABLE;
        }
        calculateTotalBuyPrice();
        if (isNotAffordable()) {
            return BuyResult.NOT_AFFORDABLE;
        }
        calculateFuturePlayerBalance();
        applyTransaction();
        recordFinalState();
        return BuyResult.SUCCESS;
    }

    private void initializeBeforeBuyOperation() {
        playerBalance[BEFORE] = MoneyManager.fromPlayer(player);
        circulation[BEFORE] = marketableItem.getCirculation();
        sellPrice[BEFORE] = marketableItem.getSellPrice();
    }

    private void calculateTotalBuyPrice() {
        buyPrice[BEFORE] = marketableItem.getBuyPrice().orElseThrow();
        totalTransactionPrice = buyPrice[BEFORE].copy().multiply(amount);
    }

    private boolean isNotAffordable() {
        return playerBalance[BEFORE].compareTo(totalTransactionPrice) < 0;
    }

    private void calculateFuturePlayerBalance() {
        playerBalance[AFTER] = playerBalance[BEFORE].copy().subtract(totalTransactionPrice);
    }

    private void applyTransaction() {
        MoneyManager.applyToPlayer(player, playerBalance[AFTER]);
        marketableItem.addToCirculation(-amount);
        ((PlayerKJS) player).kjs$give(marketableItem.itemStack.copyWithCount(amount));
    }

    private void recordFinalState() {
        circulation[AFTER] = marketableItem.getCirculation();
        buyPrice[AFTER] = marketableItem.getBuyPrice().orElseThrow();
        sellPrice[AFTER] = marketableItem.getSellPrice();
    }

    //------------------------------------------------------------------------------------------------------------------
    // GETTERS
    //------------------------------------------------------------------------------------------------------------------

    @Override
    public BuyResult getResult() {
        return (BuyResult) transactionResult;
    }
}
