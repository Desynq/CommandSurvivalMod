package io.github.desynq.commandsurvival.systems.money;

import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Manages retrieving money from external sources and applying it external sources
 */
public class MoneyManager {

    @Contract("_ -> new")
    public static @NotNull Money fromStringUUID(String stringUUID) {
        return MoneySerializer.getMoney(stringUUID);
    }

    @Contract("_ -> new")
    public static @NotNull Money fromPlayer(@NotNull Player player) {
        return fromStringUUID(player.getStringUUID());
    }

    public static void applyToPlayer(String stringUUID, Money money) {
        MoneySerializer.setMoney(stringUUID, money);
    }

    public static void applyToPlayer(Player player, Money money) {
        applyToPlayer(player.getStringUUID(), money);
    }

    public static void applyToPlayer(Player player, long rawMoney) {
        applyToPlayer(player, Money.fromCents(rawMoney));
    }
}
