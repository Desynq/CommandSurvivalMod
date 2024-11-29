package io.github.desynq.commandsurvival.systems.market;

import io.github.desynq.commandsurvival.systems.money.Money;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record MarketableRecord(
        @NotNull Money basePrice,
        @Nullable Integer scaleQuantity,
        @Nullable Money priceFloor,
        @Nullable Money priceCeiling,
        @Nullable Double buyModifier
) {

}
