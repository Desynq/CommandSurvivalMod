package io.github.desynq.commandsurvival.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.desynq.commandsurvival.util.chat.ComponentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

public abstract class PlayerCommandExecutor {

    protected final Player executor;

    public PlayerCommandExecutor(@NotNull CommandContext<CommandSourceStack> cc) throws CommandSyntaxException {
        executor = cc.getSource().getPlayerOrException();
    }

    protected void messageSelf(Object... args) {
        executor.sendSystemMessage( ComponentBuilder.build(args) );
    }

    public int getResult() {
        return 1;
    }
}
