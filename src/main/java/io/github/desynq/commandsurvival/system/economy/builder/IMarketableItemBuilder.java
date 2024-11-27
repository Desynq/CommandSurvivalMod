package io.github.desynq.commandsurvival.system.economy.builder;

import io.github.desynq.commandsurvival.system.economy.MarketableItem;
import io.github.desynq.commandsurvival.util.data.money.Money;
import net.minecraft.world.item.ItemStack;

public interface IMarketableItemBuilder {

    interface Builder<T extends Builder<T>> {

    }

    interface ItemStackStep {
        IMarketableItemBuilder.BasePriceStep setItemStack(ItemStack itemStack);
    }

    interface BasePriceStep {
        IMarketableItemBuilder.ItemCategoryStep setBasePrice(Money basePrice);
    }

    interface ItemCategoryStep {
        IMarketableItemBuilder.ItemNameStep setItemCategory(String itemCategory);
    }

    interface ItemNameStep {
        IMarketableItemBuilder.BuyModifierStep setItemName(String itemName);
    }

    interface BuyModifierStep {
        IMarketableItemBuilder.ScaleQuantityStep setBuyModifier(Double buyModifier);
    }

    interface ScaleQuantityStep {
        IMarketableItemBuilder.PriceFloorStep setScaleQuantity(Integer scaleQuantity);
    }

    interface PriceFloorStep {
        IMarketableItemBuilder.PriceCeilingStep setPriceFloor(Money priceFloor);
    }

    interface PriceCeilingStep {
        IMarketableItemBuilder.StartingCirculationStep setPriceCeiling(Money priceCeiling);
    }

    interface StartingCirculationStep {
        IMarketableItemBuilder.BuildStep setStartingCirculation(Double startingCirculation);
    }

    interface BuildStep {
        MarketableItem build();
    }
}
