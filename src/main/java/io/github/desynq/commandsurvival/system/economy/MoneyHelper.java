package io.github.desynq.commandsurvival.system.economy;

public class MoneyHelper {

    public static double fromDollar(double number) {
        return number * 100;
    }

    public static double fromDollarWithCharm(double number) {
        return (number - 0.01) * 100;
    }

    public static double toDollar(long number) {
        return number / 100.0;
    }

    public static String toDollarString(long number) {
        double dollars = toDollar(number);
        String moneyFormatted = String.format("$%,.2f", Math.abs(dollars));
        return (number < 0 ? "-" : "") + moneyFormatted;
    }

    public static Long fromSimpleDollarString(String dollarString) {
        String regex = "^\\d+(\\.\\d{1,2})?$";
        if (!dollarString.matches(regex)) {
            return null;
        }

        String[] parts = dollarString.split("\\.");

        if (parts.length == 1) {
            return Long.parseLong(parts[0]) * 100;
        }
        if (parts[1].length() == 1) {
            return Long.parseLong(parts[0] + parts[1]) * 10;
        }
        return Long.parseLong(parts[0] + parts[1]);
    }
}
