package io.github.desynq.commandsurvival.system.economy.builder;

import io.github.desynq.commandsurvival.system.economy.MarketableItem;
import io.github.desynq.commandsurvival.system.economy.MarketableItemPredicate;
import io.github.desynq.commandsurvival.util.data.money.Money;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MarketableItemFluentBuilder implements
        BuilderPattern.ItemStackStep,
        BuilderPattern.BasePriceStep,
        BuilderPattern.ItemCategoryStep,
        BuilderPattern.ItemNameStep,
        BuilderPattern.BuyModifierStep,
        BuilderPattern.ScaleQuantityStep,
        BuilderPattern.PriceFloorStep,
        BuilderPattern.PriceCeilingStep,
        BuilderPattern.StartingCirculationStep,
        BuilderPattern.BuildStep
{

    public double startingCirculation = 0.0;
    public @NotNull ItemStack itemStack;
    public @NotNull Money basePrice;
    public @NotNull String itemCategory;
    public @NotNull String itemName;
    public @Nullable MarketableItemPredicate<Player, CompoundTag> predicate;
    public @Nullable Integer scaleQuantity;
    public @Nullable Money priceFloor;
    public @Nullable Money priceCeiling;
    public @Nullable Double buyModifier;

    private MarketableItemFluentBuilder() {}

    public static BuilderPattern.ItemStackStep newInstance() {
        return new MarketableItemFluentBuilder();
    }

    @Override
    public BuilderPattern.BasePriceStep setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
        return this;
    }

    @Override
    public BuilderPattern.ItemCategoryStep setBasePrice(Money basePrice) {
        this.basePrice = basePrice;
        return this;
    }

    @Override
    public BuilderPattern.ItemNameStep setItemCategory(String itemCategory) {
        this.itemCategory = itemCategory;
        return this;
    }

    @Override
    public BuilderPattern.BuyModifierStep setItemName(String itemName) {
        this.itemName = itemName;
        return this;
    }

    @Override
    public BuilderPattern.ScaleQuantityStep setBuyModifier(Double buyModifier) {
        this.buyModifier = buyModifier;
        return this;
    }

    @Override
    public BuilderPattern.PriceFloorStep setScaleQuantity(Integer scaleQuantity) {
        this.scaleQuantity = scaleQuantity;
        return this;
    }

    @Override
    public BuilderPattern.PriceCeilingStep setPriceFloor(Money priceFloor) {
        this.priceFloor = priceFloor;
        return this;
    }

    @Override
    public BuilderPattern.StartingCirculationStep setPriceCeiling(Money priceCeiling) {
        this.priceCeiling = priceCeiling;
        return this;
    }

    @Override
    public BuilderPattern.BuildStep setStartingCirculation(Double startingCirculation) {
        this.startingCirculation = startingCirculation;
        return this;
    }

    @Override
    public MarketableItem build() {
        return new MarketableItem(this);
    }
}
