package io.github.desynq.commandsurvival.systems.market.item;

import io.github.desynq.commandsurvival.helpers.MathHelper;

public class MarketableItemHelper {

    // mean gain/loss of 2.5%
    public static double generateFluctuationPercentage() {
        return MathHelper.getBiasedRandom(0, 0.5, 20);
    }
}
