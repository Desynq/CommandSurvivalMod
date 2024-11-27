package io.github.desynq.commandsurvival.system.economy;

import io.github.desynq.commandsurvival.util.data.money.Money;
import org.jetbrains.annotations.Nullable;

public interface MarketableItemInterface {

    static Money getSellPrice(
            Money basePrice,
            double circulation,
            double startingCirculation,
            @Nullable Integer scaleQuantity,
            @Nullable Money priceFloor,
            @Nullable Money priceCeiling
    ) {
        if (scaleQuantity == null) {
            return basePrice;
        }
        
        double scale = circulation / (scaleQuantity + startingCirculation);
        double realPrice = basePrice.getRaw() * Math.pow(0.5, scale);

        if (priceFloor != null) {
            realPrice = Math.max(realPrice, priceFloor.getRaw());
        }
        if (priceCeiling != null) {
            realPrice = Math.min(realPrice, priceCeiling.getRaw());
        }

        return Money.fromCents(realPrice);
    }
}
