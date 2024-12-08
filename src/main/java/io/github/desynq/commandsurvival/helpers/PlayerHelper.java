package io.github.desynq.commandsurvival.helpers;

import io.github.desynq.commandsurvival.systems.market.item.MarketableItem;
import net.minecraft.world.entity.player.Player;

public class PlayerHelper {

    public static int clear(Player player, String itemId, String itemNBT, int amount) {
        return ServerHelper.runCommand(
                "clear %s %s%s %s",
                player.getGameProfile().getName(),
                itemId,
                itemNBT,
                amount
        );
    }

    public static int clear(Player player, MarketableItem marketableItem, int amount) {
        return clear(player, marketableItem.getItemId(), marketableItem.nbtMatch, amount);
    }
}
