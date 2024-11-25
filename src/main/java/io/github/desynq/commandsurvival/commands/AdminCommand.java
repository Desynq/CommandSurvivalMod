package io.github.desynq.commandsurvival.commands;

import com.google.common.collect.Lists;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import io.github.desynq.commandsurvival.Main;
import io.github.desynq.commandsurvival.commands.admin.Reload;
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

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;
import static net.minecraft.server.commands.ReloadCommand.reloadPacks;

public class AdminCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(literal("admin")
                .requires(cs -> cs.hasPermission(2))
                .then(literal("reload")
                        .executes(command -> Reload.scheduleReload(10))

                        .then(argument("seconds", IntegerArgumentType.integer(1, 60))
                                .executes(command -> Reload.scheduleReload(IntegerArgumentType.getInteger(command, "seconds")))
                        )
                )
        );
    }
}
