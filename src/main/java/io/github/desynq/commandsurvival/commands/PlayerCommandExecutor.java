package io.github.desynq.commandsurvival.commands;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

public abstract class PlayerCommandExecutor {

    protected final Player executor;

    public PlayerCommandExecutor(@NotNull CommandContext<CommandSourceStack> command) {
        if (!command.getSource().isPlayer()) {
            throw new IllegalArgumentException("Command source must be a player");
        }
        executor = command.getSource().getPlayer();
    }

    public int getResult() {
        return 1;
    }
}
