package io.github.desynq.commandsurvival.systems.market;

import io.github.desynq.commandsurvival.serializers.PersistentDataSerializer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

public class MarketableItemSerializer extends PersistentDataSerializer {
    private static final MarketableItemSerializer INSTANCE = new MarketableItemSerializer();

    @Override
    protected String getKey() {
        return "items_sold";
    }

    public static boolean hasCirculation(MarketableItem marketableItem) {
        return INSTANCE.deserializeNBT().contains(marketableItem.itemName, Tag.TAG_DOUBLE);
    }

    public static double getCirculation(MarketableItem marketableItem) {
        return INSTANCE.deserializeNBT().getDouble(marketableItem.itemName);
    }

    public static void setCirculation(MarketableItem marketableItem, double newAmount) {
        CompoundTag tag = INSTANCE.deserializeNBT();
        tag.putDouble(marketableItem.itemName, newAmount);
        INSTANCE.serializeNBT(tag);
    }
}
