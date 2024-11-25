package io.github.desynq.commandsurvival;

import io.github.desynq.commandsurvival.commands.AdminCommand;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Main.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CSEventHandler {

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        AdminCommand.register(event.getDispatcher());
        Main.LOGGER.debug("Registered Admin Command");
    }
}
