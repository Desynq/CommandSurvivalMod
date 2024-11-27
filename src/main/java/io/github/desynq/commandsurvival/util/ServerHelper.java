package io.github.desynq.commandsurvival.util;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import dev.latvian.mods.kubejs.core.MinecraftServerKJS;
import dev.latvian.mods.kubejs.core.ServerLevelKJS;
import dev.latvian.mods.kubejs.core.WithPersistentData;
import io.github.desynq.commandsurvival.Main;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Collection;

@Mod.EventBusSubscriber(modid = Main.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ServerHelper {
    public static MinecraftServer server;

    public static void runCommand(String command) {
        server.getCommands().performPrefixedCommand(server.createCommandSourceStack().withSuppressedOutput(), command);
    }

    public static CompoundTag getPersistentData() {
        return ((MinecraftServerKJS) server).kjs$getPersistentData();
    }




    @SubscribeEvent
    public static void onServerStart(ServerStartingEvent event) {
        server = event.getServer();
        Main.LOGGER.debug("Started ServerHelper");
    }
}
