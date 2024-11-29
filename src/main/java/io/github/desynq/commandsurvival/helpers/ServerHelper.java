package io.github.desynq.commandsurvival.helpers;

import dev.latvian.mods.kubejs.core.MinecraftServerKJS;
import io.github.desynq.commandsurvival.CommandSurvival;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CommandSurvival.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
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
        CommandSurvival.LOGGER.debug("Started ServerHelper");
    }
}