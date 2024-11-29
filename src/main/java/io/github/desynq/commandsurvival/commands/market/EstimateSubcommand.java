package io.github.desynq.commandsurvival.commands.market;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import io.github.desynq.commandsurvival.systems.market.item.MarketableItem;
import io.github.desynq.commandsurvival.systems.market.item.MarketableItemInstancesManager;
import io.github.desynq.commandsurvival.systems.money.Money;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public class EstimateSubcommand {
    private final int days;
    private final String itemName;
    private final @Nullable Player player;
    private Money estimate;

    private EstimateSubcommand(CommandContext<CommandSourceStack> command) {
        days = IntegerArgumentType.getInteger(command, "days");
        itemName = StringArgumentType.getString(command, "item_name");
        MarketableItem marketableItem = MarketableItemInstancesManager.getFromName(itemName);
        player = command.getSource().getPlayer();

        if (marketableItem == null) {
            messageInvalidMarketableItem();
            return;
        }
        estimate = marketableItem.estimateSellPrice(days);
        messageEstimate();
    }

    private void messageInvalidMarketableItem() {
        if (player == null) {
            return;
        }
        player.sendSystemMessage(Component.literal(String.format(
                "§cItem name §e`%s`§c could not be resolved to a valid marketable item.",
                itemName
        )));
    }

    private void messageEstimate() {
        if (player == null) {
            return;
        }
        player.sendSystemMessage(Component.translatable(String.format(
                "§7Item §e`%s`§7 is estimated to have a sell price of §a%s§7 after §e%s§7 days.",
                itemName,
                estimate.getDollarString(),
                days
        )));
    }


    public static int execute(CommandContext<CommandSourceStack> command) {
        new EstimateSubcommand(command);
        return 1;
    }
}
