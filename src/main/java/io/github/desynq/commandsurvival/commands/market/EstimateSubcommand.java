package io.github.desynq.commandsurvival.commands.market;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import io.github.desynq.commandsurvival.systems.market.item.EstimateResult;
import io.github.desynq.commandsurvival.systems.market.item.MarketableItem;
import io.github.desynq.commandsurvival.systems.market.item.MarketableItemInstancesManager;
import io.github.desynq.commandsurvival.util.chat.TranslatableBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import static net.minecraft.ChatFormatting.*;

public class EstimateSubcommand {
    private final int days;
    private final String itemCategory;
    private final String itemName;
    private final @NotNull Player player;

    private EstimateSubcommand(CommandContext<CommandSourceStack> command) {
        days = IntegerArgumentType.getInteger(command, "days");
        itemCategory = StringArgumentType.getString(command, "item_category");
        itemName = StringArgumentType.getString(command, "item_name");
        player = Objects.requireNonNull(command.getSource().getPlayer());

        MarketableItemInstancesManager.getFromName(itemName).ifPresentOrElse(
                this::handleEstimate,
                this::handleInvalidMarketableItem
        );
    }

    public static int execute(CommandContext<CommandSourceStack> command) {
        new EstimateSubcommand(command);
        return 1;
    }

    private void handleEstimate(MarketableItem marketableItem) {
        EstimateResult estimate = marketableItem.estimateSellPrice(days);
        player.sendSystemMessage(new TranslatableBuilder()
                .next(itemName, BLUE)
                .next(itemCategory, BLUE)
                .next(days, YELLOW)
                .next(estimate.initialCirculation(), DARK_PURPLE)
                .next(estimate.estimatedCirculation(), LIGHT_PURPLE)
                .next(estimate.initialPrice(), DARK_PURPLE)
                .next(estimate.estimatedPrice(), LIGHT_PURPLE)
                .build("commands.market.estimate.success")
        );
    }

    private void handleInvalidMarketableItem() {
        player.sendSystemMessage(new TranslatableBuilder()
                .next(itemName, BLUE)
                .next(itemCategory, BLUE)
                .build("commands.market.estimate.failure.invalid_marketable_item")
        );
    }
}
