package io.github.desynq.commandsurvival.systems.market.item;

import io.github.desynq.commandsurvival.systems.money.Money;

public record EstimateResult(
        Money initialPrice,
        Money estimatedPrice,
        double initialCirculation,
        double estimatedCirculation,
        int daysSimulated
) {
}
