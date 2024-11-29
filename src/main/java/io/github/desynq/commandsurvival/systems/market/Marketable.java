package io.github.desynq.commandsurvival.systems.market;

import io.github.desynq.commandsurvival.systems.money.Money;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A Marketable is something that can be sold as well as optionally
 * bought for a basePrice and is able to have various factors
 * influence its actual price.<br>
 * <br>
 * Marketable objects also inherently have circulation but the
 * object has to define what its circulation actually is in
 * order for it to be marketable.
 */
public abstract class Marketable {
    private final @NotNull Money basePrice;
    private final @Nullable Integer scaleQuantity;
    private final @Nullable Money priceFloor;
    private final @Nullable Money priceCeiling;
    private final @Nullable Double buyModifier;

    public Marketable(MarketableRecord record) {
        if (record.basePrice().getRaw() <= 0) {
            throw new IllegalArgumentException("Base price must be positive");
        }
        this.basePrice = record.basePrice();
        this.scaleQuantity = record.scaleQuantity();
        this.priceFloor = record.priceFloor();
        this.priceCeiling = record.priceCeiling();
        this.buyModifier = record.buyModifier();
    }

    public abstract double getCirculation();

    public abstract void setCirculation(double circulation);



    private static final double SCALING_FACTOR = 0.5;
    public Money getSellPrice(double circulation) {
        if (scaleQuantity == null) {
            return basePrice;
        }

        double scale = circulation / scaleQuantity;
        double realPrice = basePrice.getRaw() * Math.pow(SCALING_FACTOR, scale);

        if (priceFloor != null) {
            realPrice = Math.max(realPrice, priceFloor.getRaw());
        }
        if (priceCeiling != null) {
            realPrice = Math.min(realPrice, priceCeiling.getRaw());
        }

        return Money.fromCents(realPrice);
    }

    public Money getSellPrice() {
        return getSellPrice(getCirculation());
    }

    public Money getBuyPrice(double circulation) {
        if (buyModifier == null || buyModifier < 1) {
            return null; // not buyable
        }
        double realPrice = getSellPrice(circulation).getRaw() * buyModifier;
        return Money.fromCents(realPrice);
    }

    public Money getBuyPrice() {
        return getBuyPrice(getCirculation());
    }
}
