package com.JackSeyer.resurectionmod.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

public class ResurrectionTableBlock extends Block {
    public ResurrectionTableBlock() {
        super(BlockBehaviour.Properties.of()
                .mapColor(MapColor.COLOR_BLUE)
                .strength(0.5F, 3.0F)
                .requiresCorrectToolForDrops()
                .noOcclusion() // Permite que otros bloques sean visibles a través de él
                .dynamicShape() // Permite que el bloque tenga una forma dinámica
                .lightLevel(state -> 5) // Ajusta el nivel de luz emitido (opcional)
                .isSuffocating((state, world, pos) -> false) // No causa asfixia al jugador
        );

    }

}
