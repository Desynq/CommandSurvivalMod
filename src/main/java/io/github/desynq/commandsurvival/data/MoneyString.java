package io.github.desynq.commandsurvival.data;

/**
 * A string representation of money
 */
public class MoneyString {
    private final String moneyString;

    public MoneyString(String moneyString) {
        this.moneyString = moneyString;
    }

    public String[] toStringArray() {
        String regex = "^\\d+(\\.\\d{1,2})?$";
        if (!moneyString.matches(regex)) {
            return null;
        }

        return moneyString.split("\\.");
    }

    public Money toMoney() {
        String[] strings = toStringArray();;
        long dollars;
        long cents;
        try {
            dollars = Long.parseLong(strings[0]);
        }
        catch (NullPointerException | ArrayIndexOutOfBoundsException | NumberFormatException e) {
            return null;
        }

        try {
            cents = Long.parseLong(strings[1]);
        }
        catch (NullPointerException | ArrayIndexOutOfBoundsException e) {
            return Money.fromDollars(dollars);
        }
        catch (NumberFormatException e) {
            return null;
        }
        return Money.fromDollarsAndCents(dollars, cents);
    }
}
