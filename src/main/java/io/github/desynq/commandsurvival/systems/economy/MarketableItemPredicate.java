package io.github.desynq.commandsurvival.systems.economy;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;

@FunctionalInterface
public interface MarketableItemPredicate<Player, CompoundTag> {

    boolean test(Player player, CompoundTag nbt);
}
