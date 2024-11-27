package io.github.desynq.commandsurvival.system.economy;

import io.github.desynq.commandsurvival.util.data.PersistentDataSerializer;
import net.minecraft.nbt.CompoundTag;

public class MarketableItemSerializer extends PersistentDataSerializer {
    private static final MarketableItemSerializer INSTANCE = new MarketableItemSerializer();

    @Override
    protected String getKey() {
        return "items_sold";
    }

    public static int getAmountSold(MarketableItem marketableItem) {
        return INSTANCE.deserializeNBT().getInt(marketableItem.itemName);
    }

    public static void setAmountSold(MarketableItem marketableItem, int newAmount) {
        CompoundTag tag = INSTANCE.deserializeNBT();
        tag.putInt(marketableItem.itemName, newAmount);
        INSTANCE.serializeNBT(tag);
    }
}
