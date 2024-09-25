package com.JackSeyer.resurectionmod.item;

import com.JackSeyer.resurectionmod.ResurectionMod;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    // Obtiene la lista de items y crea un iten nuevo
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, ResurectionMod.MODID);

    // Crea el objeto PlayerSoul
    public static final RegistryObject<Item> PLAYERSOUL = ITEMS.register("playersoul",
            () -> new Item(new Item.Properties()));

    // Crea la funcion REGISTER
    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
