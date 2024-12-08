package io.github.desynq.commandsurvival.commands.market;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.desynq.commandsurvival.commands.PlayerCommandExecutor;
import io.github.desynq.commandsurvival.util.chat.ComponentBuilder;
import net.minecraft.commands.CommandSourceStack;
import org.jetbrains.annotations.NotNull;

import static net.minecraft.ChatFormatting.*;

public abstract class MarketItemTransactionExecutor extends PlayerCommandExecutor {
    protected final String itemCategory;
    protected final String itemName;

    public MarketItemTransactionExecutor(@NotNull CommandContext<CommandSourceStack> cc)
            throws CommandSyntaxException {
        super(cc);
        itemCategory = MarketCommandArgument.ITEM_CATEGORY.get(cc);
        itemName = MarketCommandArgument.ITEM_NAME.get(cc);
    }

    protected void handleInvalidMarketableItem() {
        // Specified item 'ruby' from 'gems' is not marketable.
        executor.sendSystemMessage(new ComponentBuilder(
                RED, "Specified item '", itemName, GRAY, "' from '", itemCategory, GRAY, "' is not marketable."
        ).build());
    }
}
