package io.github.desynq.commandsurvival.data;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Data element for money
 * Handles converting types into money and vice versa
 * Also has basic methods for manipulating money as a value
 */
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

    /**
     * Always rounds up
     */
    @Contract("_ -> new")
    public static @NotNull Money fromCents(double cents) {
        return new Money((long) Math.ceil(cents));
    }

    @Contract(value = "_ -> new", pure = true)
    public static @NotNull Money fromDollars(long dollars) {
        return new Money(dollars * 100);
    }

    /**
     * Always rounds up
     */
    @Contract("_ -> new")
    public static @NotNull Money fromDollars(double dollars) {
        return new Money((long) Math.ceil(dollars * 100));
    }

    @Contract(value = "_, _ -> new", pure = true)
    public static @NotNull Money fromDollarsAndCents(long dollars, long cents) {
        return new Money((dollars * 100) + cents);
    }

    public static Money fromString(String moneyString) {
        return new MoneyString(moneyString).toMoney();
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
}
