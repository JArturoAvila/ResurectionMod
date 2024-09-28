package com.JackSeyer.resurectionmod.client.handler;

import com.JackSeyer.resurectionmod.ResurectionMod;
import com.JackSeyer.resurectionmod.screen.ResurrectionTableScreen;
import com.JackSeyer.resurectionmod.init.MenuInit;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = ResurectionMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModHandler {
    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            // Registra los menús de tu mod
            MenuScreens.register(MenuInit.RESURRECTION_TABLE_MENU.get(), ResurrectionTableScreen::new);
        });
    }

    @SubscribeEvent
    public static void registerKeys(RegisterKeyMappingsEvent event) {
        // Registra las teclas de acceso rápido aquí
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        // Registra renderizadores de entidades aquí
    }
}
