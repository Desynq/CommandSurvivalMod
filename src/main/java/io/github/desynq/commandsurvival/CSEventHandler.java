package io.github.desynq.commandsurvival;

import io.github.desynq.commandsurvival.commands.admin.AdminCommand;
import io.github.desynq.commandsurvival.commands.market.MarketCommand;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CommandSurvival.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CSEventHandler {

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        AdminCommand.register(event.getDispatcher());
        CommandSurvival.LOGGER.debug("Registered Admin Command");

        MarketCommand.register(event.getDispatcher());
        CommandSurvival.LOGGER.debug("Registered Market Command");
    }
}
