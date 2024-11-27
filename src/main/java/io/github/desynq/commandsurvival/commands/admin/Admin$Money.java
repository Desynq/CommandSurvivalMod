package io.github.desynq.commandsurvival.commands.admin;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.github.desynq.commandsurvival.util.data.money.Money;
import io.github.desynq.commandsurvival.util.data.Username;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

import static net.minecraft.ChatFormatting.*;
import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

class Admin$Money {

    private static final RequiredArgumentBuilder<CommandSourceStack, String> USERNAME_ARGUMENT =
            argument("username", StringArgumentType.string());

    public static final LiteralArgumentBuilder<CommandSourceStack> COMMAND =
            literal("money")
                    .then(literal("get")
                            .then(USERNAME_ARGUMENT.executes(Admin$Money::getMoney))
                    )
                    .then(literal("set")
                            .then(USERNAME_ARGUMENT
                                    .then(argument("amount", StringArgumentType.string())
                                            .executes(SetMoneyProcess::execute)
                                    )
                            )
                    );



    private static int getMoney(CommandContext<CommandSourceStack> command) {
        Username username = new Username(command, "username");
        Money money = Money.fromStringUUID(username.getStringUUID());

        Player player = command.getSource().getPlayer();
        if (player != null) {
            getMoney$message(player, money);
        }

        return 1;
    }
    private static void getMoney$message(@NotNull Player player, Money money) {
        player.sendSystemMessage(Component.literal("Player ")
                .withStyle(GRAY)
                .append(player.getDisplayName())
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
