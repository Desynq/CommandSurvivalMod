package io.github.desynq.commandsurvival.capability;

import io.github.desynq.commandsurvival.CommandSurvival;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CommandSurvival.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class MyCapability {

    public static final Capability<MyCapabilityInterface> INSTANCE = CapabilityManager.get(new CapabilityToken<>() {});

    @SubscribeEvent
    public static void register(RegisterCapabilitiesEvent event) {
        event.register(MyCapabilityInterface.class);
    }

    private MyCapability() {
    }
}
