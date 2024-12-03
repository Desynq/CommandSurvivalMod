package io.github.desynq.commandsurvival.systems.market.manager;

import dev.latvian.mods.kubejs.core.PlayerKJS;
import io.github.desynq.commandsurvival.systems.market.item.MarketableItem;
import io.github.desynq.commandsurvival.systems.money.MoneyManager;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Manages transactions via the market system
 * I.e., buying and selling
 */
public class MarketManager {


    public static BuyOperation buy(Player player, @NotNull MarketableItem marketableItem, int amount) {
        return new BuyOperation(player, marketableItem, amount);
    }

    public static void sell(Player player, @NotNull MarketableItem marketableItem)
            throws NotSellableException {

        if (marketableItem.buyModifier != null && marketableItem.buyModifier < 1) {
            throw new NotSellableException();
        }
    }


    public static class NotBuyableException extends RuntimeException {
        public NotBuyableException() {
            super("Item is not buyable");
        }
    }

    public static class NotSellableException extends RuntimeException {
        public NotSellableException() {
            super("Item is not sellable");
        }
    }

    public static class NotAffordableException extends RuntimeException {
        public NotAffordableException() {
            super("Player cannot afford item");
        }
    }
}
