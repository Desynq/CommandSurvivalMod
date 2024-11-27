package io.github.desynq.commandsurvival.util.data.money;

import net.minecraft.world.entity.player.Player;

public class Money {
    private long money;

    private Money(long cents) {
        this.money = cents;
    }

    //------------------------------------------------------------------------------------------------------------------
    // FACTORIES
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Technically also fromRaw() since money is stored in cents for precision
     */
    public static Money fromCents(long cents) {
        return new Money(cents);
    }

    public static Money fromDollars(long dollars) {
        return new Money(dollars * 100);
    }

    public static Money fromDollars(double dollars) {
        return new Money((long)(dollars * 100));
    }

    public static Money fromDollarsAndCents(long dollars, long cents) {
        return new Money((dollars * 100) + cents);
    }

    public static Money fromString(String moneyString) {
        return new MoneyString(moneyString).toMoney();
    }

    public static Money fromStringUUID(String stringUUID) {
        return MoneySerializer.getMoney(stringUUID);
    }

    public static Money fromPlayer(Player player) {
        return fromStringUUID(player.getStringUUID());
    }

    //------------------------------------------------------------------------------------------------------------------
    // GETTERS
    //------------------------------------------------------------------------------------------------------------------

    public double getDollars() {
        return money / 100.0;
    }

    public long getRaw() {
        return money;
    }

    public long getCents() {
        return money % 100;
    }

    public String getDollarString() {
        double dollars = getDollars();
        String moneyFormatted = String.format("$%,.2f", Math.abs(dollars));
        return (money < 0 ? "-" : "") + moneyFormatted;
    }

    //------------------------------------------------------------------------------------------------------------------
    // OPERATIONS
    //------------------------------------------------------------------------------------------------------------------

    public Money add(long moneyToAdd) {
        money += moneyToAdd;
        return this;
    }

    public Money add(Money moneyToAdd) {
        return add(moneyToAdd.money);
    }

    //------------------------------------------------------------------------------------------------------------------
    // SIDE EFFECTS
    //------------------------------------------------------------------------------------------------------------------

    public void applyToPlayer(String uuidString) {
        MoneySerializer.setMoney(uuidString, this);
    }
}
