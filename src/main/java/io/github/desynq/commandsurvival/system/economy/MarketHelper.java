package io.github.desynq.commandsurvival.system.economy;

import io.github.desynq.commandsurvival.util.MathHelper;
import io.github.desynq.commandsurvival.util.data.money.Money;
import org.jetbrains.annotations.Nullable;

public class MarketHelper {

    // mean gain/loss of 2.5%
    public static double getBiasedFluctuationPercentage() {
        return MathHelper.getBiasedRandom(0, 0.5, 20);
    }

    public static double getFluctuatedCirculation(double circulation, double percentage) {
        return circulation * (1 + (circulation < 0 ? 1 : -1) * percentage);
    }


    public static Money getSellPrice(
            Money basePrice,
            double circulation,
            @Nullable Integer scaleQuantity,
            @Nullable Money priceFloor,
            @Nullable Money priceCeiling
    ) {
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

    /**
     * TODO:
     * Extract {@link MarketableItem#getBuyPrice} to static method here
     */
    public static Money getBuyPrice() {
        return null;
    }
}
