package io.github.desynq.commandsurvival.commands;

import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.github.desynq.commandsurvival.system.economy.MoneyHelper;
import io.github.desynq.commandsurvival.system.economy.PlayerMoneyHelper;
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
                                    .then(argument("amount", LongArgumentType.longArg(0))
                                            .executes(Admin$Money::setMoney)
                                    )
                            )
                    );



    private static int getMoney(CommandContext<CommandSourceStack> command) {
        Username username = new Username(command, "username");
        long money = PlayerMoneyHelper.getMoney(username.getUUIDString());

        Player player = command.getSource().getPlayer();
        if (player != null) {
            getMoney$Message(player, money);
        }

        return 1;
    }
    private static void getMoney$Message(@NotNull Player player, long money) {
        player.sendSystemMessage(Component.literal("Player ")
                .withStyle(GRAY)
                .append(player.getDisplayName())
                .append(
                        Component.literal(" has ")
                                .withStyle(GRAY)
                )
                .append(
                        Component.literal(MoneyHelper.toDollarString(money))
                                .withStyle(DARK_GREEN)
                )
        );
    }



    private static int setMoney(CommandContext<CommandSourceStack> command) {
        Username username = new Username(command, "username");
        long amount = LongArgumentType.getLong(command, "amount");

        Player player = command.getSource().getPlayer();
        if (player != null) {
            long previousMoney = PlayerMoneyHelper.getMoney(username.getUUIDString());
            setMoney$Message(player, amount, previousMoney);
        }

        PlayerMoneyHelper.setMoney(username.getUUIDString(), amount);

        return 1;
    }
    private static void setMoney$Message(@NotNull Player player, long amount, long previousMoney) {
        player.sendSystemMessage(Component.literal("Player ")
                .withStyle(GRAY)
                .append(player.getDisplayName())
                .append(
                        Component.literal(" now has ")
                                .withStyle(GRAY)
                )
                .append(
                        Component.literal(MoneyHelper.toDollarString(amount))
                                .withStyle(DARK_GREEN)
                )
                .append(
                        Component.literal(" instead of ")
                                .withStyle(GRAY)
                )
                .append(
                        Component.literal(MoneyHelper.toDollarString(previousMoney))
                                .withStyle(YELLOW)
                )
        );
    }
}
