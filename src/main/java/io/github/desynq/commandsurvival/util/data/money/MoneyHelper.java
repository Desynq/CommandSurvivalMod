package io.github.desynq.commandsurvival.util.data.money;

public class MoneyHelper {

    public static void applyToPlayer(String stringUUID, Money money) {
        MoneySerializer.setMoney(stringUUID, money);
    }
}
