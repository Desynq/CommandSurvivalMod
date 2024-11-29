package io.github.desynq.commandsurvival.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public interface MyCapabilityInterface extends INBTSerializable<CompoundTag> {

    String getValue();

    void setMyValue(String myValue);
}
