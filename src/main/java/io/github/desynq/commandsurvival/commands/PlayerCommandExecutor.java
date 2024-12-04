package io.github.desynq.commandsurvival.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

public abstract class PlayerCommandExecutor {

    protected final Player executor;

    public PlayerCommandExecutor(@NotNull CommandContext<CommandSourceStack> command) throws CommandSyntaxException {
        executor = command.getSource().getPlayerOrException();
    }

    public int getResult() {
        return 1;
    }
}
