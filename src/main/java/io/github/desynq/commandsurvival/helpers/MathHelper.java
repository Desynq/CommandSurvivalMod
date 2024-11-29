package io.github.desynq.commandsurvival.helpers;

public class MathHelper {

    public static double getBiasedRandom(double min, double max, double bias) {
        double random = Math.pow(Math.random(), bias);
        return (max - min) * random + min;
    }
}
