package io.github.desynq.commandsurvival.systems.market.item;

import io.github.desynq.commandsurvival.systems.money.Money;
import net.minecraft.world.item.ItemStack;

public interface MarketableItemBuilderInterface {

    interface Builder<T extends Builder<T>> {

    }

    interface ItemStackStep {
        MarketableItemBuilderInterface.BasePriceStep setItemStack(ItemStack itemStack);
    }

    interface BasePriceStep {
        MarketableItemBuilderInterface.ItemCategoryStep setBasePrice(Money basePrice);
    }

    interface ItemCategoryStep {
        MarketableItemBuilderInterface.ItemNameStep setItemCategory(String itemCategory);
    }

    interface ItemNameStep {
        MarketableItemBuilderInterface.BuyModifierStep setItemName(String itemName);
    }

    interface BuyModifierStep {
        MarketableItemBuilderInterface.ScaleQuantityStep setBuyModifier(Double buyModifier);
    }

    interface ScaleQuantityStep {
        MarketableItemBuilderInterface.PriceFloorStep setScaleQuantity(Integer scaleQuantity);
    }

    interface PriceFloorStep {
        MarketableItemBuilderInterface.PriceCeilingStep setPriceFloor(Money priceFloor);
    }

    interface PriceCeilingStep {
        MarketableItemBuilderInterface.BuildStep setPriceCeiling(Money priceCeiling);
    }

    interface BuildStep {
        MarketableItem build(boolean isManaged) throws IllegalArgumentException;
    }
}
