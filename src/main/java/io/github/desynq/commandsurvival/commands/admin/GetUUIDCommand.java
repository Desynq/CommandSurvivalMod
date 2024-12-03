package io.github.desynq.commandsurvival.commands.admin;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.desynq.commandsurvival.commands.PlayerCommandExecutor;
import io.github.desynq.commandsurvival.util.chat.ComponentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;

import static net.minecraft.ChatFormatting.*;

public class GetUUIDCommand extends PlayerCommandExecutor {
    private Entity entity;

    public GetUUIDCommand(CommandContext<CommandSourceStack> command) {
        super(command);
        try {
            entity = EntityArgument.getEntity(command, "entity");
        }
        catch (CommandSyntaxException e) {
            handleInvalidSelector();
            return;
        }
        handleValidEntity();
    }

    private void handleValidEntity() {
        // `displayName`'s UUID is `uuid`
        executor.sendSystemMessage(new ComponentBuilder(WHITE)
                        .next(entity.getDisplayName(), RESET)
                        .next("'s UUID is ")
                        .next(entity.getStringUUID(), YELLOW)
                .build());
    }

    private void handleInvalidSelector() {
        executor.sendSystemMessage(Component.literal("Entity does not exist").withStyle(RED));
    }
}
