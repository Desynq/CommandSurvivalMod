package io.github.desynq.commandsurvival.data;

import io.github.desynq.commandsurvival.CommandSurvival;
import io.github.desynq.commandsurvival.helpers.ServerHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashSet;
import java.util.Set;

/**
 * Idea is that usernames are non-unique due to player name changes whereas UUIDs are unique and cannot be changed by
 * the player, therefore there is a many-to-one relationship between usernames and UUIDs. Because of this, keys should
 * be usernames while values should be UUIDs.
 *
 * This data is mostly useful for when accessing offline data pertinent to a player.
 */
@Mod.EventBusSubscriber(modid = CommandSurvival.MODID)
public class UsernameUUIDMap {
    private static final String USERNAME_UUID_MAP = "username_to_uuid_map";

    private static CompoundTag deserializeNBT() {
        return ServerHelper.getPersistentData().getCompound(USERNAME_UUID_MAP);
    }

    private static void serializeNBT(CompoundTag tag) {
        ServerHelper.getPersistentData().put(USERNAME_UUID_MAP, tag);
    }



    public static String getStringUUID(String username) {
        return deserializeNBT().getString(username);
    }

    public static void add(String username, String stringUUID) {
        CompoundTag tag = deserializeNBT();
        tag.putString(username, stringUUID);
        serializeNBT(tag);
    }



    public static Set<String> getUsernames() {
        return deserializeNBT().getAllKeys();
    }

    public static Set<String> getUUIDStrings() {
        Set<String> set = new HashSet<>();

        for (String key : getUsernames()) {
            String value = deserializeNBT().getString(key);
            set.add(value);
        }
        return set;
    }


    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        String username = player.getGameProfile().getName();
        String stringUUID = player.getUUID().toString();
        add(username, stringUUID);
    }
}
