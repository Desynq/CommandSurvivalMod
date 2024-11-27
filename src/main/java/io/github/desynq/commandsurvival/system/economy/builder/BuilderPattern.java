package io.github.desynq.commandsurvival.system.economy.builder;

import io.github.desynq.commandsurvival.system.economy.MarketableItem;
import io.github.desynq.commandsurvival.util.data.money.Money;
import net.minecraft.world.item.ItemStack;

public interface BuilderPattern {

    interface ItemStackStep {
        BuilderPattern.BasePriceStep setItemStack(ItemStack itemStack);
    }

    interface BasePriceStep {
        BuilderPattern.ItemCategoryStep setBasePrice(Money basePrice);
    }

    interface ItemCategoryStep {
        BuilderPattern.ItemNameStep setItemCategory(String itemCategory);
    }

    interface ItemNameStep {
        BuilderPattern.BuyModifierStep setItemName(String itemName);
    }

    interface BuyModifierStep {
        BuilderPattern.ScaleQuantityStep setBuyModifier(Double buyModifier);
    }

    interface ScaleQuantityStep {
        BuilderPattern.PriceFloorStep setScaleQuantity(Integer scaleQuantity);
    }

    interface PriceFloorStep {
        BuilderPattern.PriceCeilingStep setPriceFloor(Money priceFloor);
    }

    interface PriceCeilingStep {
        BuilderPattern.StartingCirculationStep setPriceCeiling(Money priceCeiling);
    }

    interface StartingCirculationStep {
        BuilderPattern.BuildStep setStartingCirculation(Double startingCirculation);
    }

    interface BuildStep {
        MarketableItem build();
    }
}
