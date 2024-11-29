package io.github.desynq.commandsurvival.systems.market.item;

import io.github.desynq.commandsurvival.systems.market.Marketable;
import io.github.desynq.commandsurvival.systems.money.Money;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Ties {@link Marketable} to an item with {@link MarketableItem#itemStack}
 */
public class MarketableItem extends Marketable {
    //------------------------------------------------------------------------------------------------------------------
    // Instance Fields
    //------------------------------------------------------------------------------------------------------------------

    public final @NotNull ItemStack itemStack;
    public final @NotNull String itemCategory;
    public final @NotNull String itemName;
    public final @Nullable MarketableItemPredicate<Player, CompoundTag> marketableItemPredicate;

    //------------------------------------------------------------------------------------------------------------------
    // Constructors
    //------------------------------------------------------------------------------------------------------------------

    public MarketableItem(@NotNull MarketableItemBuilder builder) {
        super(builder.getBaseComponents());
        this.itemStack = builder.itemStack;
        this.marketableItemPredicate = builder.marketableItemPredicate;
        this.itemCategory = builder.itemCategory;
        this.itemName = builder.itemName;
    }

    //------------------------------------------------------------------------------------------------------------------
    // Circulation
    //------------------------------------------------------------------------------------------------------------------

    @Override
    public double getCirculation() {
        return MarketableItemSerializer.getCirculation(this);
    }

    @Override
    public void setCirculation(double circulation) {
        MarketableItemSerializer.setCirculation(this, circulation);
    }

    public void addToCirculation(double amount) {
        setCirculation(getCirculation() + amount);
    }

    public void fluctuateCirculation(double percentage) {
        setCirculation(getFluctuatedCirculation(percentage));
    }

    public double getFluctuatedCirculation(double percentage) {
        double circulation = getCirculation();
        int i = circulation < 0 ? 1 : -1;
        return circulation * (1 + i * percentage);
    }

    public Money estimateSellPrice(int days) {
        double circulation = getCirculation();
        double percentage;

        for (int day = days; day >= 0; day--) {
            percentage = MarketableItemHelper.generateFluctuationPercentage();
            circulation = getFluctuatedCirculation(percentage);
        }
        return getSellPrice(circulation);
    }
}
