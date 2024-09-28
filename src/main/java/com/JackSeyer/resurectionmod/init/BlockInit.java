package com.JackSeyer.resurectionmod.init;

import com.JackSeyer.resurectionmod.ResurectionMod;
import com.JackSeyer.resurectionmod.block.ResurrectionTableBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockInit {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ResurectionMod.MODID);

    public static final RegistryObject<ResurrectionTableBlock> RESURRECTION_TABLE_BLOCK = BLOCKS.register("resurrection_table",
            () -> new ResurrectionTableBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)
                    .mapColor(MapColor.COLOR_BLUE)
                    .strength(5.0f, 10.0f) // Fuerza y dureza del bloque
                    .noOcclusion()
            )); // Permite la interacción desde cualquier lado
}
