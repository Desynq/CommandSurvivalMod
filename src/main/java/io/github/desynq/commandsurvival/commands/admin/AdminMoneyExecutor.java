package io.github.desynq.commandsurvival.commands.admin;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.github.desynq.commandsurvival.systems.money.Money;
import io.github.desynq.commandsurvival.data.Username;
import io.github.desynq.commandsurvival.systems.money.MoneyManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

import static net.minecraft.ChatFormatting.*;
import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

class AdminMoneyExecutor {

    private static final RequiredArgumentBuilder<CommandSourceStack, String> USERNAME_ARGUMENT =
            argument("username", StringArgumentType.string());

    public static final LiteralArgumentBuilder<CommandSourceStack> COMMAND =
            literal("money")
                    .then(literal("get")
                            .then(USERNAME_ARGUMENT.executes(AdminMoneyExecutor::getMoney))
                    )
                    .then(literal("set")
                            .then(USERNAME_ARGUMENT
                                    .then(argument("amount", StringArgumentType.string())
                                            .executes(AdminMoneySetExecutor::execute)
                                    )
                            )
                    );



    private static int getMoney(CommandContext<CommandSourceStack> command) {
        Username username = new Username(command, "username");
        Money money = MoneyManager.fromStringUUID(username.getStringUUID());

        Player executor = command.getSource().getPlayer();
        if (executor != null) {
            getMoney$message(executor, money, username);
        }

        return 1;
    }
    private static void getMoney$message(@NotNull Player executor, Money money, Username username) {
        executor.sendSystemMessage(Component.literal("Player ")
                .withStyle(GRAY)
                .append(username.toString())
                .append(
                        Component.literal(" has ")
                                .withStyle(GRAY)
                )
                .append(
                        Component.literal(money.getDollarString())
                                .withStyle(DARK_GREEN)
                )
        );
    }
}
