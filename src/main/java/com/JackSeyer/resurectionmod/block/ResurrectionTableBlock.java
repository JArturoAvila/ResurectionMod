package com.JackSeyer.resurectionmod.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

public class ResurrectionTableBlock extends Block {
    public ResurrectionTableBlock() {
        super(BlockBehaviour.Properties.of()
                .mapColor(MapColor.STONE)
                .strength(1.5F, 6.0F)
                .requiresCorrectToolForDrops()
                .noOcclusion() // Permite que otros bloques sean visibles a través de él
                .dynamicShape() // Permite que el bloque tenga una forma dinámica
                .lightLevel(state -> 5) // Ajusta el nivel de luz emitido (opcional)
                .isSuffocating((state, world, pos) -> false) // No causa asfixia al jugador
        );

    }

}
