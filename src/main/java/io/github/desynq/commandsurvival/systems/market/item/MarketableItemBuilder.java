package io.github.desynq.commandsurvival.systems.market.item;

import io.github.desynq.commandsurvival.systems.market.MarketableRecord;
import io.github.desynq.commandsurvival.systems.money.Money;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

import static io.github.desynq.commandsurvival.systems.market.item.MarketableItemBuilderInterface.*;

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
    public @Nullable MarketableItemPredicate<Player, CompoundTag> marketableItemPredicate;
    public @Nullable Integer scaleQuantity;
    public @Nullable Money priceFloor;
    public @Nullable Money priceCeiling;
    public @Nullable Double buyModifier;

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
    public MarketableItem build(boolean shouldBeManaged) {
        MarketableItem marketableItem = new MarketableItem(this);
        if (!shouldBeManaged) {
            return marketableItem;
        }

        boolean notDuplicate = MarketableItemInstancesManager.addInstance(marketableItem);
        if (!notDuplicate) {
            throw new IllegalArgumentException(String.format("Marketable item with name %s is already being managed", this.itemName));
        }
        return marketableItem;
    }

    /**
     *
     * @return Record of components needed in order to initialize superclass of the MarketableItem
     */
    public MarketableRecord getBaseComponents() {
        return new MarketableRecord(basePrice, scaleQuantity, priceFloor, priceCeiling, buyModifier);
    }
}
