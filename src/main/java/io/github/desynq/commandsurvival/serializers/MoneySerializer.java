package io.github.desynq.commandsurvival.serializers;

import io.github.desynq.commandsurvival.systems.money.Money;
import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Serializes an entity's money into the server by storing it with their stringUUID
 * Deserializes an entity's money from the server by retrieving it via their stringUUID
 */
public class MoneySerializer extends PersistentDataSerializer {
    private static final MoneySerializer INSTANCE = new MoneySerializer();

    @Override
    protected String getKey() {
        return "player_money";
    }

    @Contract("_ -> new")
    public static @NotNull Money getMoney(String uuidString) {
        return Money.fromCents(INSTANCE.deserializeNBT().getLong(uuidString));
    }

    public static void setMoney(String uuidString, @NotNull Money money) {
        CompoundTag tag = INSTANCE.deserializeNBT();
        tag.putLong(uuidString, money.getRaw());
        INSTANCE.serializeNBT(tag);
    }
}
