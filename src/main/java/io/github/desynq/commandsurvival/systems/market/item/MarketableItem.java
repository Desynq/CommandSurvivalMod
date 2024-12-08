package io.github.desynq.commandsurvival.systems.market.item;

import io.github.desynq.commandsurvival.systems.market.Marketable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

/**
 * Ties {@link Marketable} to an item with {@link MarketableItem#itemStack}
 */
public class MarketableItem extends Marketable {

    public final @NotNull ItemStack itemStack;
    public final @NotNull String itemCategory;
    public final @NotNull String itemName;
    public final String nbtMatch;

    public MarketableItem(@NotNull MarketableItemBuilder builder) {
        super(builder.getBaseComponents());
        this.itemStack = builder.itemStack;
        this.nbtMatch = builder.nbtMatch;
        this.itemCategory = builder.itemCategory;
        this.itemName = builder.itemName;
    }

    //------------------------------------------------------------------------------------------------------------------
    // ITEM ACCESSORS
    //------------------------------------------------------------------------------------------------------------------

    public String getItemId() {
        ResourceLocation rl = ForgeRegistries.ITEMS.getKey(itemStack.getItem());
        if (rl == null) {
            throw new IllegalArgumentException("Marketable item is using an unregistered item");
        }
        return rl.toString();
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
        double newCirculation = MarketableItemHelper.getFluctuatedCirculation(getCirculation(), percentage);
        setCirculation(newCirculation);
    }

    //------------------------------------------------------------------------------------------------------------------
    // Estimation
    //------------------------------------------------------------------------------------------------------------------

    public EstimateResult estimateSellPrice(int days) {
        double initialCirculation = getCirculation();
        double finalCirculation = simulateFluctuation(initialCirculation, days);

        return new EstimateResult(
                getSellPrice(),
                getSellPrice(finalCirculation),
                initialCirculation,
                finalCirculation,
                days
        );
    }

    private double simulateFluctuation(double circulation, int days) {
        for (int day = days; day >= 0; day--) {
            double percentage = MarketableItemHelper.generateFluctuationPercentage();
            circulation = MarketableItemHelper.getFluctuatedCirculation(circulation, percentage);
        }
        return circulation;
    }
}
