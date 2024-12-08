package io.github.desynq.commandsurvival.systems.market.manager;

import io.github.desynq.commandsurvival.helpers.PlayerHelper;
import io.github.desynq.commandsurvival.systems.market.item.MarketableItem;
import io.github.desynq.commandsurvival.systems.money.Money;
import io.github.desynq.commandsurvival.systems.money.MoneyManager;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

public final class SellOperation extends Transaction<SellOperation> {
    private static final int CHECK_AMOUNT = 0;

    private final int[] inventoryAmount = new int[NUMBER_OF_STAGES];
    private int amountToSell;

    public SellOperation(Player player, @NotNull MarketableItem marketableItem, int amount) {
        super(player, marketableItem, amount);
    }

    @Override
    protected SellResult performTransaction() {
        if (amount < 0) {
            return SellResult.NEGATIVE_AMOUNT_SPECIFIED;
        }
        if (amount == 0) {
            return SellResult.ZERO_AMOUNT_SPECIFIED;
        }
        inventoryAmount[BEFORE] = clearAmount(CHECK_AMOUNT);
        if (inventoryAmount[BEFORE] <= 0) {
            return SellResult.NO_ITEMS;
        }
        amountToSell = Math.min(inventoryAmount[BEFORE], amount);
        recordPriorState();
        applyTransaction();
        recordAfterState();
        return SellResult.SUCCESS;
    }

    private int clearAmount(int amount) {
        return PlayerHelper.clear(player, marketableItem, amount);
    }

    private void recordPriorState() {
        buyPrice[BEFORE] = marketableItem.getBuyPrice().orElse(null);
        playerBalance[BEFORE] = MoneyManager.fromPlayer(player);
        circulation[BEFORE] = marketableItem.getCirculation();
        sellPrice[BEFORE] = marketableItem.getSellPrice();
        totalTransactionPrice = calculateTotalSellPrice();
    }

    private Money calculateTotalSellPrice() {
        return sellPrice[BEFORE].copy().multiply(amountToSell);
    }

    private void applyTransaction() {
        MoneyManager.addToPlayer(player, totalTransactionPrice);
        clearAmount(amountToSell);
        marketableItem.addToCirculation(amountToSell);
    }

    private void recordAfterState() {
        buyPrice[AFTER] = marketableItem.getBuyPrice().orElse(null);
        playerBalance[AFTER] = MoneyManager.fromPlayer(player);
        circulation[AFTER] = marketableItem.getCirculation();
        sellPrice[AFTER] = marketableItem.getSellPrice();
        inventoryAmount[AFTER] = clearAmount(CHECK_AMOUNT);
    }

    //------------------------------------------------------------------------------------------------------------------
    // GETTERS
    //------------------------------------------------------------------------------------------------------------------

    @Override
    public SellResult getResult() {
        return (SellResult) transactionResult;
    }

    public int getAmountInInventoryBeforeTransaction() {
        return inventoryAmount[BEFORE];
    }

    public int getAmountLeftInInventory() {
        validateSuccessful();
        return inventoryAmount[AFTER];
    }

    public int getAmountSold() {
        validateSuccessful();
        return amountToSell;
    }
}
