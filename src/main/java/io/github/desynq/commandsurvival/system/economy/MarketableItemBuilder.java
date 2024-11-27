package io.github.desynq.commandsurvival.system.economy;

import io.github.desynq.commandsurvival.util.data.money.Money;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.checkerframework.common.aliasing.qual.Unique;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class MarketableItemBuilder {
    public final ItemStack itemStack;
    public final Money basePrice;
    public MarketableItemPredicate<Player, CompoundTag> predicate;
    public Integer scaleQuantity;
    public final String itemCategory;
    public final String itemName;
    public Money buyPriceFloor;
    public Money sellPriceFloor;
    public Money buyPriceCeiling;
    public Float buyModifier;

    private MarketableItemBuilder(ItemStack itemStack, Money basePrice, String itemCategory, String itemName) {
        this.itemStack = itemStack;
        this.basePrice = basePrice;
        this.itemCategory = itemCategory;
        this.itemName = itemName;
    }

    public static MarketableItemBuilder newInstance(ItemStack itemStack, Money basePrice, String itemCategory, String itemName) {
        return new MarketableItemBuilder(itemStack, basePrice, itemCategory, itemName);
    }

    public MarketableItemBuilder predicate(MarketableItemPredicate<Player, CompoundTag> predicate) {
        this.predicate = predicate;
        return this;
    }

    public MarketableItemBuilder scaleQuantity(int scaleQuantity) {
        this.scaleQuantity = scaleQuantity;
        return this;
    }

    public MarketableItemBuilder buyPriceFloor(Money buyPriceFloor) {
        this.buyPriceFloor = buyPriceFloor;
        return this;
    }

    public MarketableItemBuilder sellPriceFloor(Money sellPriceFloor) {
        this.sellPriceFloor = sellPriceFloor;
        return this;
    }

    public MarketableItemBuilder buyPriceCeiling(Money buyPriceCeiling) {
        this.buyPriceCeiling = buyPriceCeiling;
        return this;
    }

    public MarketableItemBuilder buyModifier(float buyModifier) {
        this.buyModifier = buyModifier;
        return this;
    }

    public MarketableItem build() {
        if (itemStack == null || basePrice == null || itemCategory == null || itemName == null) {
            throw new IllegalStateException("Required fields cannot be null");
        }

        return new MarketableItem(this);
    }
}
