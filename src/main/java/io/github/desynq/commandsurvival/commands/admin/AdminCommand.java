package io.github.desynq.commandsurvival.commands.admin;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class AdminCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(literal("admin")
                .requires(cs -> cs.hasPermission(2))
                .then(literal("reload")
                        .executes(command -> AdminReloadExecutor.scheduleReload(10))

                        .then(argument("seconds", IntegerArgumentType.integer(1, 60))
                                .executes(command -> AdminReloadExecutor.scheduleReload(IntegerArgumentType.getInteger(command, "seconds")))
                        )
                )
                .then(AdminMoneyExecutor.COMMAND)
                .then(literal("get_uuid")
                        .requires(CommandSourceStack::isPlayer)
                        .then(argument("entity", EntityArgument.entity())
                                .executes(c -> new AdminGetUUIDExecutor(c).getResult())
                        )
                )
        );
    }
}
