package io.github.desynq.commandsurvival.systems.market;

@FunctionalInterface
public interface MarketableItemPredicate<Player, CompoundTag> {

    boolean test(Player player, CompoundTag nbt);
}
