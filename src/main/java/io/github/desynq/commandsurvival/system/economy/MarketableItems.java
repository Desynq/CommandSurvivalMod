package io.github.desynq.commandsurvival.system.economy;

import io.github.desynq.commandsurvival.system.economy.builder.MarketableItemBuilder;
import io.github.desynq.commandsurvival.util.data.money.Money;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class MarketableItems {

    public static final MarketableItem DIAMOND = MarketableItemBuilder
            .newInstance()
            .setItemStack(new ItemStack(Items.DIAMOND.asItem()))
            .setBasePrice(Money.fromDollars(100.00))
            .setItemCategory("gems")
            .setItemName("diamond")
            .setBuyModifier(2.0)
            .setScaleQuantity(100)
            .setPriceFloor(Money.fromDollars(25.00))
            .setPriceCeiling(Money.fromDollars(200.00))
            .setStartingCirculation(500.0)
            .build();
}
