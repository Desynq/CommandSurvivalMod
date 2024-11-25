package io.github.desynq.commandsurvival.util;

import io.github.desynq.commandsurvival.Main;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Main.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ServerHelper {
    private static MinecraftServer server;

    public static void runCommand(String command) {
        server.getCommands().performPrefixedCommand(server.createCommandSourceStack().withSuppressedOutput(), command);
    }




    @SubscribeEvent
    public static void onServerStart(ServerStartingEvent event) {
        server = event.getServer();
        Main.LOGGER.debug("Started ServerHelper");
    }
}
