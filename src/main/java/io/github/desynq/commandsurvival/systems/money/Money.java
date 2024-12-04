package io.github.desynq.commandsurvival.systems.money;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Data element for money
 * Handles converting types into money and vice versa
 * Also has basic methods for performing operations on money mathematically
 */
public class Money implements Comparable<Money> {
    private long amount;

    private Money(long cents) {
        this.amount = cents;
    }

    //------------------------------------------------------------------------------------------------------------------
    // FACTORIES
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Technically also fromRaw() since money is stored in cents for precision
     */
    @Contract(value = "_ -> new", pure = true)
    public static @NotNull Money fromCents(long cents) {
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
        return amount / 100.0;
    }

    public long getRaw() {
        return amount;
    }

    public long getCents() {
        return amount % 100;
    }

    public String getDollarString() {
        double dollars = getDollars();
        String moneyFormatted = String.format("$%,.2f", Math.abs(dollars));
        return (amount < 0 ? "-" : "") + moneyFormatted;
    }

    //------------------------------------------------------------------------------------------------------------------
    // OPERATIONS
    //------------------------------------------------------------------------------------------------------------------

    public @NotNull Money copy() {
        return new Money(amount);
    }

    public Money add(long value) {
        amount += value;
        return this;
    }

    public Money add(@NotNull Money other) {
        return this.add(other.amount);
    }

    public Money subtract(long value) {
        amount -= value;
        return this;
    }

    public Money subtract(@NotNull Money other) {
        return this.subtract(other.amount);
    }

    public @NotNull Money multiply(long value) {
        amount *= value;
        return this;
    }

    public Money multiply(@NotNull Money other) {
        return this.multiply(other.amount);
    }

    @Override
    public int compareTo(@NotNull Money other) {
        return Long.compare(this.amount, other.amount);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Money other = (Money) obj;
        return compareTo(other) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount);  // Ensure consistency with equals
    }
}
