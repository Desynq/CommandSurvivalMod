package io.github.desynq.commandsurvival.commands.admin;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.desynq.commandsurvival.commands.PlayerCommandExecutor;
import io.github.desynq.commandsurvival.systems.money.Money;
import io.github.desynq.commandsurvival.data.Username;
import io.github.desynq.commandsurvival.systems.money.MoneyManager;
import net.minecraft.commands.CommandSourceStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static net.minecraft.ChatFormatting.*;

public final class AdminMoneyGetExecutor extends PlayerCommandExecutor {
    private @Nullable Username username;
    private final Money money;

    public AdminMoneyGetExecutor(@NotNull CommandContext<CommandSourceStack> cc) throws CommandSyntaxException {
        super(cc);

        try {
            username = new Username(AdminCommandArgument.USERNAME.get(cc));
        } catch (IllegalArgumentException e) {
            username = null;
        }

        if (username == null) {
            money = MoneyManager.fromPlayer(executor);
            printMoneyFromSelf();
        }
        else {
            money = MoneyManager.fromStringUUID(username.getStringUUID());
            printMoneyFromUsername();
        }
    }

    private void printMoneyFromSelf() {
        // You have $1.00.
        messageSelf(WHITE, "You have ", money, DARK_GREEN, ".");
    }

    private void printMoneyFromUsername() {
        // Player under username 'Desynq' has $1.00.
        messageSelf(WHITE, "Player under username '", username, "' has ", money, DARK_GREEN, ".");
    }

    public static int execute(@NotNull CommandContext<CommandSourceStack> cc) throws CommandSyntaxException {
        return new AdminMoneyGetExecutor(cc).getResult();
    }
}
