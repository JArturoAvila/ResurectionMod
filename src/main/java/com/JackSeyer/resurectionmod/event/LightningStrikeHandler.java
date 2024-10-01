package com.JackSeyer.resurectionmod.event;

import net.minecraftforge.event.entity.EntityStruckByLightningEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.world.entity.player.Player;

@Mod.EventBusSubscriber
public class LightningStrikeHandler {

    @SubscribeEvent
    public static void onEntityStruckByLightning(EntityStruckByLightningEvent event) {
        if (event.getEntity() instanceof Player) {
            // Cancelar el daño causado por el rayo a jugadores
            event.setCanceled(true);
        }
    }
}
