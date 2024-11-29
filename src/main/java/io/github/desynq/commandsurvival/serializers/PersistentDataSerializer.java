package io.github.desynq.commandsurvival.serializers;

import io.github.desynq.commandsurvival.helpers.ServerHelper;
import net.minecraft.nbt.CompoundTag;

public abstract class PersistentDataSerializer {

    protected abstract String getKey();

    protected CompoundTag deserializeNBT() {
        String key = getKey();
        return ServerHelper.getPersistentData().getCompound(key);
    }

    protected void serializeNBT(CompoundTag tag) {
        String key = getKey();
        ServerHelper.getPersistentData().put(key, tag);
    }
}
