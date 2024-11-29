package io.github.desynq.commandsurvival.systems.market;

import io.github.desynq.commandsurvival.systems.market.item.MarketableItem;
import io.github.desynq.commandsurvival.systems.market.item.MarketableItemBuilder;
import io.github.desynq.commandsurvival.systems.money.Money;
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
            .build(true);

    public static void register() {}
}
