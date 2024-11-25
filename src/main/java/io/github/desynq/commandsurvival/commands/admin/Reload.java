package io.github.desynq.commandsurvival.commands.admin;

import io.github.desynq.commandsurvival.util.MagicValues;
import io.github.desynq.commandsurvival.util.ServerHelper;
import io.github.desynq.commandsurvival.util.TaskScheduler;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public class Reload {

    public static int scheduleReload(int startSeconds) {
        for (int seconds = startSeconds; seconds >= 0; seconds--) {
            new ScheduledTask(seconds, startSeconds);
        }
        return 1;
    }

    private static class ScheduledTask {
        private final int secondsLeft;
        private final MinecraftServer server;

        public ScheduledTask(int secondsLeft, int startSeconds) {
            this.secondsLeft = secondsLeft;
            this.server = ServerHelper.server;

            int delay = (startSeconds - secondsLeft) * MagicValues.TICKS_PER_SECOND;
            TaskScheduler.scheduleTask(server, delay, this::execute);
        }

        private void execute() {
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
