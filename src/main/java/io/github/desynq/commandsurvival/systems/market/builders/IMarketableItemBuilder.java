package io.github.desynq.commandsurvival.systems.market.builders;

import io.github.desynq.commandsurvival.systems.market.MarketableItem;
import io.github.desynq.commandsurvival.data.Money;
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
        IMarketableItemBuilder.BuildStep setPriceCeiling(Money priceCeiling);
    }

    interface BuildStep {
        MarketableItem build();
    }
}
