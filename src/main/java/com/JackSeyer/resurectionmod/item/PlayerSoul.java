package com.JackSeyer.resurectionmod.item;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class PlayerSoul extends Item {

    public PlayerSoul(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isFireResistant() {
        // Evita que el ítem se queme en lava o fuego
        return true;
    }

    @Override
    public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity) {
        // Haz que flote en lava
        if (entity.isInLava()) {
            entity.setDeltaMovement(entity.getDeltaMovement().x, 0.04, entity.getDeltaMovement().z); // Ajusta la velocidad de flotación en lava
        }

        // Haz que flote en agua
        if (entity.isInWater()) {
            entity.setDeltaMovement(entity.getDeltaMovement().x, 0.04, entity.getDeltaMovement().z); // Ajusta la velocidad de flotación en agua
        }

        return false; // Retorna falso para que continúe con la lógica predeterminada
    }

}
