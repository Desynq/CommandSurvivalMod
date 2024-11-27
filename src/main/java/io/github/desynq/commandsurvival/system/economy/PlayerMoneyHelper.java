package io.github.desynq.commandsurvival.system.economy;

import io.github.desynq.commandsurvival.util.ServerHelper;
import io.github.desynq.commandsurvival.util.data.Username;
import io.github.desynq.commandsurvival.util.data.UsernameUUIDMap;
import net.minecraft.nbt.CompoundTag;

import java.util.UUID;

public class PlayerMoneyHelper {
    private static final String KEY = "player_money";

    private static CompoundTag deserializeNBT() {
        return ServerHelper.getPersistentData().getCompound(KEY);
    }

    private static void serializeNBT(CompoundTag tag) {
        ServerHelper.getPersistentData().put(KEY, tag);
    }



    public static long getMoney(String uuidString) {
        return deserializeNBT().getLong(uuidString);
    }

    public static long getMoneyFromUsername(String username) {
        String uuidString = UsernameUUIDMap.getStringUUID(username);
        return getMoney(uuidString);
    }


    public static void setMoney(String uuidString, long amount) {
        CompoundTag tag = deserializeNBT();
        tag.putLong(uuidString, amount);

        serializeNBT(tag);
    }



    public static void addMoney(UUID uuid, long amount) {
        String uuidString = uuid.toString();
        long currentMoney = getMoney(uuidString);

        setMoney(uuidString, currentMoney + amount);
    }
}
