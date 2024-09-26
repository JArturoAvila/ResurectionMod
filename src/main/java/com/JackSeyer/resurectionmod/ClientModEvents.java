package com.JackSeyer.resurectionmod;

import com.JackSeyer.resurectionmod.block.ModBlocks;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ResurectionMod.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientModEvents {

    @SubscribeEvent
    public static void onRegisterRenderers(ModelEvent.RegisterAdditional event) {
        // Asigna el tipo de renderizado translúcido a la Mesa de Resurrección
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.RESURRECTION_TABLE.get(), RenderType.translucent());
    }
}
