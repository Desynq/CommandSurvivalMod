package io.github.desynq.commandsurvival.systems.market.manager;

public enum SellResult implements IResult {
    NEGATIVE_AMOUNT_SPECIFIED,
    ZERO_AMOUNT_SPECIFIED,
    NO_ITEMS,
    SUCCESS;

    @Override
    public boolean isSuccessful() {
        return this == SUCCESS;
    }

    @Override
    public boolean isTransactionable() {
        return true; // all marketable objects are sellable
    }
}
