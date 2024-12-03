package io.github.desynq.commandsurvival.systems.market.manager;

import dev.latvian.mods.kubejs.core.PlayerKJS;
import io.github.desynq.commandsurvival.systems.market.item.MarketableItem;
import io.github.desynq.commandsurvival.systems.money.Money;
import io.github.desynq.commandsurvival.systems.money.MoneyManager;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

public class BuyOperation {
    private final Player player;
    private final @NotNull MarketableItem marketableItem;
    private final int amount;
    private final BeforeOperation before;
    private BuyResult buyResult;
    private AfterOperation after;
    private Money totalBuyPrice;

    private static class BeforeOperation {
        private Money buyPrice;
        private Money playerBalance;
        private double circulation;
    }

    private static class AfterOperation {
        private Money buyPrice;
        private Money playerBalance;
        private double circulation;
    }

    public BuyOperation(Player player, @NotNull MarketableItem marketableItem, int amount) {
        this.player = player;
        this.marketableItem = marketableItem;
        this.amount = amount;

        before = new BeforeOperation();
        initializeBeforeOperation();
        if (buyResult == BuyResult.NOT_BUYABLE || buyResult == BuyResult.NOT_AFFORDABLE) {
            return;
        }

        processAfterOperation();
        buyResult = BuyResult.SUCCESS;
    }

    private void initializeBeforeOperation() {
        marketableItem.getBuyPrice().ifPresentOrElse(
                buyPrice -> before.buyPrice = buyPrice,
                () -> buyResult = BuyResult.NOT_BUYABLE
        );
        if (buyResult == BuyResult.NOT_BUYABLE) return;

        before.playerBalance = MoneyManager.fromPlayer(player);
        before.circulation = marketableItem.getCirculation();
        totalBuyPrice = before.buyPrice.copy().multiply(amount);

        if (before.playerBalance.compareTo(totalBuyPrice) < 0) {
            buyResult = BuyResult.NOT_AFFORDABLE;
        }
    }

    private void processAfterOperation() {
        after = new AfterOperation();
        after.playerBalance = before.playerBalance.copy().subtract(totalBuyPrice);
        MoneyManager.applyToPlayer(player, after.playerBalance);
        marketableItem.addToCirculation(-amount);
        after.circulation = marketableItem.getCirculation();

        ((PlayerKJS) player).kjs$give(marketableItem.itemStack.copyWithCount(amount));
    }

    private void validateSuccess() {
        if (buyResult != BuyResult.SUCCESS) {
            throw new IllegalStateException("Operation was cancelled, cannot access post-operation data");
        }
    }

    public BuyResult getResult() {
        return buyResult;
    }

    public Money getTotalBuyPrice() {
        return totalBuyPrice;
    }

    public Money getBuyPriceBefore() {
        return before.buyPrice;
    }

    public Money getPlayerBalanceBefore() {
        return before.playerBalance;
    }

    public double getCirculationBefore() {
        return before.circulation;
    }

    public Money getBuyPriceAfter() {
        validateSuccess();
        return after.buyPrice;
    }

    public Money getPlayerBalanceAfter() {
        validateSuccess();
        return after.playerBalance;
    }

    public double getCirculationAfter() {
        validateSuccess();
        return after.circulation;
    }
}
