package io.github.desynq.commandsurvival.systems.market;

import io.github.desynq.commandsurvival.systems.money.Money;
import io.github.desynq.commandsurvival.helpers.MarketHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Data type that allows for getting sell and buy price<br>
 * - Requires some basic parameters for the instance itself<br>
 * - Some methods require circulation which is retrieved externally
 * (usually tied to an instance of {@link MarketableItem})
 */
public class Marketable {
    private final @Nullable Integer scaleQuantity;
    private final @NotNull Money basePrice;
    private final @Nullable Money priceFloor;
    private final @Nullable Money priceCeiling;
    private final @Nullable Double buyModifier;

    public Marketable(
            @NotNull Money basePrice,
            @Nullable Integer scaleQuantity,
            @Nullable Money priceFloor,
            @Nullable Money priceCeiling,
            @Nullable Double buyModifier
    ) {
        this.basePrice = basePrice;
        this.scaleQuantity = scaleQuantity;
        this.priceFloor = priceFloor;
        this.priceCeiling = priceCeiling;
        this.buyModifier = buyModifier;
    }

    public Money getSellPrice(double circulation) {
        if (scaleQuantity == null) {
            return basePrice;
        }

        double scale = circulation / scaleQuantity;
        double realPrice = basePrice.getRaw() * Math.pow(0.5, scale);

        if (priceFloor != null) {
            realPrice = Math.max(realPrice, priceFloor.getRaw());
        }
        if (priceCeiling != null) {
            realPrice = Math.min(realPrice, priceCeiling.getRaw());
        }

        return Money.fromCents(realPrice);
    }

    public Money getBuyPrice(double circulation) {
        if (buyModifier == null || buyModifier < 1) {
            return null; // not buyable
        }
        double realPrice = getSellPrice(circulation).getRaw() * buyModifier;
        return Money.fromCents(realPrice);
    }

    public Money estimate(int days, double circulation) {
        double percentage;

        for (int day = days; day >= 0; day--) {
            percentage = MarketHelper.getBiasedFluctuationPercentage();
            circulation = MarketHelper.getFluctuatedCirculation(circulation, percentage);
        }
        return getSellPrice(circulation);
    }
}