package io.github.desynq.commandsurvival.commands.admin;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import org.jetbrains.annotations.NotNull;

import static net.minecraft.commands.Commands.literal;

public class AdminCommand {

    public static void register(@NotNull CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(literal("admin")
                .requires(cs -> cs.hasPermission(2))
                .then(literal("reload")
                        .executes(command -> AdminReloadExecutor.scheduleReload(10))

                        .then(AdminCommandArgument.SECONDS.argumentBuilder()
                                .executes(command -> AdminReloadExecutor.scheduleReload(IntegerArgumentType.getInteger(command, "seconds")))
                        )
                )
                .then(MONEY_ADMIN_COMMANDS)
                .then(literal("get_uuid")
                        .requires(CommandSourceStack::isPlayer)
                        .then(AdminCommandArgument.ENTITY.argumentBuilder()
                                .executes(c -> new AdminGetUUIDExecutor(c).getResult())
                        )
                )
        );
    }

    private static final LiteralArgumentBuilder<CommandSourceStack> MONEY_ADMIN_COMMANDS =
            literal("money")
                    .then(literal("get")
                            .requires(CommandSourceStack::isPlayer)
                            .executes(AdminMoneyGetExecutor::execute)
                            .then(AdminCommandArgument.USERNAME.argumentBuilder()
                                    .executes(AdminMoneyGetExecutor::execute)
                            )
                    )
                    .then(literal("set")
                            .requires(CommandSourceStack::isPlayer)
                            .then(AdminCommandArgument.USERNAME.argumentBuilder()
                                    .then(AdminCommandArgument.MONEY.argumentBuilder()
                                            .executes(AdminMoneySetExecutor::execute)
                                    )
                            )
                    );
}
