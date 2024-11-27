package io.github.desynq.commandsurvival.system.economy;

import io.github.desynq.commandsurvival.util.data.PersistentDataSerializer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

public class MarketableItemSerializer extends PersistentDataSerializer {
    private static final MarketableItemSerializer INSTANCE = new MarketableItemSerializer();

    @Override
    protected String getKey() {
        return "items_sold";
    }

    public static boolean hasCirculation(MarketableItem marketableItem) {
        return INSTANCE.deserializeNBT().contains(marketableItem.itemName, Tag.TAG_INT);
    }

    public static int getCirculation(MarketableItem marketableItem) {
        return INSTANCE.deserializeNBT().getInt(marketableItem.itemName);
    }

    public static void setCirculation(MarketableItem marketableItem, int newAmount) {
        CompoundTag tag = INSTANCE.deserializeNBT();
        tag.putInt(marketableItem.itemName, newAmount);
        INSTANCE.serializeNBT(tag);
    }
}
