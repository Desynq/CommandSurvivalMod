package io.github.desynq.commandsurvival.systems.market.manager;

public enum BuyResult implements IResult {
    NOT_BUYABLE,
    NOT_AFFORDABLE,
    SUCCESS;

    @Override
    public boolean isSuccessful() {
        return this == SUCCESS;
    }

    @Override
    public boolean isTransactionable() {
        return this != NOT_BUYABLE;
    }
}
