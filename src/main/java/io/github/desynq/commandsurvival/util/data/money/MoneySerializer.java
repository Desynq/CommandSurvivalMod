package io.github.desynq.commandsurvival.util.data.money;

import io.github.desynq.commandsurvival.util.ServerHelper;
import net.minecraft.nbt.CompoundTag;

/**
 * Package-only
 */
class MoneySerializer {
    private static final String KEY = "player_money";

    private static CompoundTag deserializeNBT() {
        return ServerHelper.getPersistentData().getCompound(KEY);
    }

    private static void serializeNBT(CompoundTag tag) {
        ServerHelper.getPersistentData().put(KEY, tag);
    }



    public static Money getMoney(String uuidString) {
        return Money.fromCents(deserializeNBT().getLong(uuidString));
    }

    public static void setMoney(String uuidString, Money money) {
        CompoundTag tag = deserializeNBT();
        tag.putLong(uuidString, money.getRaw());
        serializeNBT(tag);
    }
}
