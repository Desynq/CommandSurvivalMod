package io.github.desynq.commandsurvival.commands;

import com.google.common.collect.Lists;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import io.github.desynq.commandsurvival.Main;
import io.github.desynq.commandsurvival.util.MagicValues;
import io.github.desynq.commandsurvival.util.ServerHelper;
import io.github.desynq.commandsurvival.util.TaskScheduler;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.world.level.storage.WorldData;

import java.util.Collection;

import static net.minecraft.commands.Commands.literal;
import static net.minecraft.server.commands.ReloadCommand.reloadPacks;

public class AdminCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                literal("admin")
                        .requires(cs -> cs.hasPermission(2))
                        .then(
                                literal("reload")
                                        .executes(AdminCommand::executeReload)
                        )
        );
    }

    private static int executeReload(CommandContext<CommandSourceStack> command) {
        final int startSeconds = 10;
        for (int seconds = startSeconds; seconds >= 0; seconds--) {
            new ReloadSchedule(seconds, startSeconds, command.getSource().getServer());
        }
        return 1;
    }

    private static class ReloadSchedule {
        private final int secondsLeft;
        private final MinecraftServer server;

        public ReloadSchedule(int secondsLeft, int startSeconds, MinecraftServer server) {
            this.secondsLeft = secondsLeft;
            this.server = server;

            int delay = (startSeconds - secondsLeft) * MagicValues.TICKS_PER_SECOND;
            TaskScheduler.scheduleTask(server, delay, this::task);
        }

        private void task() {
            if (secondsLeft == 0) {
                for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                    player.sendSystemMessage(Component.literal("Reloading..."));
                    ServerHelper.runCommand("reload");
                    return;
                }
            }
            for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                String message = String.format("Reloading in %s...", secondsLeft);
                player.sendSystemMessage(Component.literal(message));
            }
        }
    }
}
