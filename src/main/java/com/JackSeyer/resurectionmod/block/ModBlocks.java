package com.JackSeyer.resurectionmod.block;

import com.JackSeyer.resurectionmod.ResurectionMod;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@Mod.EventBusSubscriber(modid = ResurectionMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModBlocks {

    // Deferred Register para los bloques
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, "resurectionmod");

    // Registrar el bloque Resurrection Table
    public static final RegistryObject<Block> RESURRECTION_TABLE = BLOCKS.register("resurrection_table", ResurrectionTableBlock::new);

    // Deferred Register para los items (BlockItems incluidos)
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ResurectionMod.MODID);

    // Registrar el BlockItem para Resurrection Table
    public static final RegistryObject<Item> RESURRECTION_TABLE_ITEM = ITEMS.register("resurrection_table",
            () -> new BlockItem(RESURRECTION_TABLE.get(), new Item.Properties()));

    // Evento para agregar el bloque a la pestaña creativa
    @SubscribeEvent
    public static void onBuildCreativeTabContents(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {  // Usar getTabKey()
            event.accept(RESURRECTION_TABLE_ITEM.get());  // Añadir el BlockItem a la pestaña de construcción
        }
    }
}

