package io.github.desynq.commandsurvival.helpers;

public class MarketHelper {

    // mean gain/loss of 2.5%
    public static double getBiasedFluctuationPercentage() {
        return MathHelper.getBiasedRandom(0, 0.5, 20);
    }

    public static double getFluctuatedCirculation(double circulation, double percentage) {
        return circulation * (1 + (circulation < 0 ? 1 : -1) * percentage);
    }
}
