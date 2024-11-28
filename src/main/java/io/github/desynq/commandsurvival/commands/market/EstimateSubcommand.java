package io.github.desynq.commandsurvival.commands.market;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import io.github.desynq.commandsurvival.system.economy.MarketableItem;
import io.github.desynq.commandsurvival.util.data.money.Money;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import static net.minecraft.ChatFormatting.*;

public class EstimateSubcommand {
    private final int days;
    private final String itemName;
    private final @Nullable Player player;
    private Money estimate;

    private EstimateSubcommand(CommandContext<CommandSourceStack> command) {
        days = IntegerArgumentType.getInteger(command, "days");
        itemName = StringArgumentType.getString(command, "item_name");
        @Nullable MarketableItem marketableItem = MarketableItem.getFromName(itemName);
        player = command.getSource().getPlayer();

        if (marketableItem == null) {
            messageInvalidMarketableItem();
            return;
        }
        estimate = marketableItem.estimate(days);
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
                String.valueOf(days)
        )));
    }


    public static int execute(CommandContext<CommandSourceStack> command) {
        new EstimateSubcommand(command);
        return 1;
    }
}
