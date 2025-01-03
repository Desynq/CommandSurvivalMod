package io.github.desynq.commandsurvival.commands.admin;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import io.github.desynq.commandsurvival.data.Username;
import io.github.desynq.commandsurvival.systems.money.Money;
import io.github.desynq.commandsurvival.systems.money.MoneyManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import static net.minecraft.ChatFormatting.*;

public class AdminMoneySetExecutor {
    private final Money money;
    private final Player player;
    private Money previousMoney;

    private AdminMoneySetExecutor(Username username, Money money, Player player) {
        this.money = money;
        this.player = player;
        if (money == null) {
            logInvalidMoneyType();
            return;
        }

        this.previousMoney = MoneyManager.fromStringUUID(username.getStringUUID());
        MoneyManager.applyToPlayer(username.getStringUUID(), money);
        logSuccess();
    }

    private void logSuccess() {
        if (player == null) {
            return;
        }

        player.sendSystemMessage(Component.literal("Player ")
                .withStyle(GRAY)
                .append(player.getDisplayName())
                .append(
                        Component.literal(" now has ")
                                .withStyle(GRAY)
                )
                .append(
                        Component.literal(money.toString())
                                .withStyle(DARK_GREEN)
                )
                .append(
                        Component.literal(" instead of ")
                                .withStyle(GRAY)
                )
                .append(
                        Component.literal(previousMoney.toString())
                                .withStyle(YELLOW)
                )
        );
    }

    private void logInvalidMoneyType() {
        if (player == null) {
            return;
        }

        player.sendSystemMessage(Component.literal("Invalid money type: must be in format of either x, x.x, or x.xx"));
    }



    public static int execute(CommandContext<CommandSourceStack> command) {
        Username username = new Username(AdminCommandArgument.USERNAME.get(command));
        String moneyString = AdminCommandArgument.MONEY.get(command);
        Money money = Money.fromString(moneyString);
        Player player = command.getSource().getPlayer();

        new AdminMoneySetExecutor(username, money, player);
        return 1;
    }
}
