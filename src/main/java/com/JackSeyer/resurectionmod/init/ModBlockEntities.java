package com.JackSeyer.resurectionmod.init;

import com.JackSeyer.resurectionmod.ResurectionMod;
import com.JackSeyer.resurectionmod.blockentity.ResurrectionTableBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {

    // Registro de tipos de entidades de bloques
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, ResurectionMod.MODID);

    // Registro de la entidad ResurrectionTableBlockEntity
    public static final RegistryObject<BlockEntityType<ResurrectionTableBlockEntity>> RESURRECTION_TABLE_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("resurrection_table_block_entity",
                    () -> BlockEntityType.Builder.of(ResurrectionTableBlockEntity::new, BlockInit.RESURRECTION_TABLE_BLOCK.get())
                            .build(null));
}
