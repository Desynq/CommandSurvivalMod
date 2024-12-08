package io.github.desynq.commandsurvival.systems.market.manager;

import io.github.desynq.commandsurvival.systems.market.item.MarketableItem;
import io.github.desynq.commandsurvival.systems.money.Money;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

public abstract class Transaction<Implementation extends Transaction<Implementation>> {
    protected final Player player;
    protected final @NotNull MarketableItem marketableItem;
    protected final int amount;
    protected IResult transactionResult;

    protected Money totalTransactionPrice;
    protected final Money[] buyPrice = new Money[NUMBER_OF_STAGES];
    protected final Money[] playerBalance = new Money[NUMBER_OF_STAGES];
    protected final double[] circulation = new double[NUMBER_OF_STAGES];
    protected final Money[] sellPrice = new Money[NUMBER_OF_STAGES];

    protected static int BEFORE = 0;
    protected static int AFTER = 1;
    protected static int NUMBER_OF_STAGES = 2;

    public Transaction(Player player, @NotNull MarketableItem marketableItem, int amount) {
        this.player = player;
        this.marketableItem = marketableItem;
        this.amount = amount;
    }

    @SuppressWarnings("unchecked")
    protected Implementation self() {
        return (Implementation) this;
    }

    public Implementation executeTransaction() {
        transactionResult = performTransaction();
        return self();
    }

    protected abstract IResult performTransaction();

    //------------------------------------------------------------------------------------------------------------------
    // GETTERS
    //------------------------------------------------------------------------------------------------------------------

    protected void validateBuyable() {
        if (marketableItem.isNotBuyable()) {
            throw new IllegalStateException("Supplied marketable is not buyable and therefore has no buy price");
        }
    }

    protected void validateSuccessful() {
        if (!transactionResult.isSuccessful()) {
            throw new IllegalStateException("Transaction did not succeed, cannot access post-transaction data");
        }
    }

    protected void validateTransactionable() {
        if (!transactionResult.isTransactionable()) {
            throw new IllegalStateException(
                    "Transaction was cancelled as the marketable does not accept this type of transaction");
        }
    }



    public IResult getResult() {
        return transactionResult;
    }

    public boolean isBuyable() {
        return !marketableItem.isNotBuyable();
    }

    //------------------------------------------------------------------------------------------------------------------
    // BEFORE
    //------------------------------------------------------------------------------------------------------------------

    public Money getPlayerBalanceBefore() {
        return playerBalance[BEFORE];
    }

    public double getCirculationBefore() {
        return circulation[BEFORE];
    }

    public Money getSellPriceBefore() {
        return sellPrice[BEFORE];
    }

    public Money getBuyPriceBefore() {
        validateBuyable();
        return buyPrice[BEFORE];
    }

    public Money getTotalTransactionPrice() {
        validateTransactionable();
        return totalTransactionPrice;
    }

    //------------------------------------------------------------------------------------------------------------------
    // AFTER
    //------------------------------------------------------------------------------------------------------------------

    public Money getBuyPriceAfter() {
        validateBuyable();
        validateSuccessful();
        return buyPrice[AFTER];
    }

    public Money getPlayerBalanceAfter() {
        validateSuccessful();
        return playerBalance[AFTER];
    }

    public double getCirculationAfter() {
        validateSuccessful();
        return circulation[AFTER];
    }

    public Money getSellPriceAfter() {
        validateSuccessful();
        return sellPrice[AFTER];
    }
}
