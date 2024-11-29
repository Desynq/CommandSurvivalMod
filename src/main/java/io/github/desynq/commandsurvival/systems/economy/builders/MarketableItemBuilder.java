package io.github.desynq.commandsurvival.systems.economy.builders;

import io.github.desynq.commandsurvival.systems.economy.MarketableItem;
import io.github.desynq.commandsurvival.systems.economy.MarketableItemPredicate;
import io.github.desynq.commandsurvival.data.Money;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import static io.github.desynq.commandsurvival.systems.economy.builders.IMarketableItemBuilder.*;

public class MarketableItemBuilder implements
        ItemStackStep,
        BasePriceStep,
        ItemCategoryStep,
        ItemNameStep,
        BuyModifierStep,
        ScaleQuantityStep,
        PriceFloorStep,
        PriceCeilingStep,
        BuildStep
{
    public ItemStack itemStack;
    public Money basePrice;
    public String itemCategory;
    public String itemName;
    public @Nullable MarketableItemPredicate<Player, CompoundTag> predicate;
    public @Nullable Integer scaleQuantity;
    public @Nullable Money priceFloor;
    public @Nullable Money priceCeiling;
    public @Nullable Double buyModifier;
    public double startingCirculation = 0.0;

    private MarketableItemBuilder() {}

    public static ItemStackStep newInstance() {
        return new MarketableItemBuilder();
    }

    @Override
    public BasePriceStep setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
        return this;
    }

    @Override
    public ItemCategoryStep setBasePrice(Money basePrice) {
        this.basePrice = basePrice;
        return this;
    }

    @Override
    public ItemNameStep setItemCategory(String itemCategory) {
        this.itemCategory = itemCategory;
        return this;
    }

    @Override
    public BuyModifierStep setItemName(String itemName) {
        this.itemName = itemName;
        return this;
    }

    @Override
    public ScaleQuantityStep setBuyModifier(@Nullable Double buyModifier) {
        this.buyModifier = buyModifier;
        return this;
    }

    @Override
    public PriceFloorStep setScaleQuantity(@Nullable Integer scaleQuantity) {
        this.scaleQuantity = scaleQuantity;
        return this;
    }

    @Override
    public PriceCeilingStep setPriceFloor(@Nullable Money priceFloor) {
        this.priceFloor = priceFloor;
        return this;
    }

    @Override
    public BuildStep setPriceCeiling(@Nullable Money priceCeiling) {
        this.priceCeiling = priceCeiling;
        return this;
    }

    @Override
    public MarketableItem build() {
        return new MarketableItem(this);
    }
}
