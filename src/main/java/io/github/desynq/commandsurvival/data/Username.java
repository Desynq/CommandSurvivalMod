package io.github.desynq.commandsurvival.data;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.world.entity.player.Player;

public class Username {

    private final String username;

    public Username(String username) {
        this.username = username;
    }

    public Username(CommandContext<CommandSourceStack> command, String argumentName) {
        this(StringArgumentType.getString(command, argumentName));
    }

    public static Username get(Player player) {
        return new Username(player.getGameProfile().getName());
    }

    public String get() {
        return username;
    }

    public String getStringUUID() {
        return UsernameUUIDMap.getStringUUID(username);
    }
}
