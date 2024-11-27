package io.github.desynq.commandsurvival.util.data.money;

import io.github.desynq.commandsurvival.util.data.PersistentDataSerializer;
import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Package-only
 */
class MoneySerializer extends PersistentDataSerializer {
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
