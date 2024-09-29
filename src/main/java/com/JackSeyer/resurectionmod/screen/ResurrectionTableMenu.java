package com.JackSeyer.resurectionmod.screen;

import com.JackSeyer.resurectionmod.blockentity.ResurrectionTableBlockEntity;
import com.JackSeyer.resurectionmod.block.ModBlocks;
import com.JackSeyer.resurectionmod.init.MenuInit;
import com.JackSeyer.resurectionmod.item.ModItems;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos; // Importa BlockPos
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class ResurrectionTableMenu extends AbstractContainerMenu {
    private final ResurrectionTableBlockEntity blockEntity;
    private final ContainerLevelAccess levelAccess;
    private final BlockPos blockPos; // Añade BlockPos como un campo

    // Constructor para el cliente
    public ResurrectionTableMenu(int containerId, Inventory playerInv, FriendlyByteBuf additionalData) {
        this(containerId, playerInv, (ResurrectionTableBlockEntity) playerInv.player.level().getBlockEntity(additionalData.readBlockPos()));
    }

    // Constructor para el servidor
    public ResurrectionTableMenu(int containerId, Inventory playerInv, BlockEntity blockEntity) {
        super(MenuInit.RESURRECTION_TABLE_MENU.get(), containerId);
        if (blockEntity instanceof ResurrectionTableBlockEntity be) {
            this.blockEntity = be;
            this.blockPos = be.getBlockPos(); // Almacena la posición del bloque
        } else {
            throw new IllegalStateException("Incorrect block entity class (%s) passed into ResurrectionTableMenu!"
                    .formatted(blockEntity.getClass().getCanonicalName()));
        }

        this.levelAccess = ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos());

        // Crear inventarios
        createPlayerHotbar(playerInv);
        createPlayerInventory(playerInv);
        createBlockEntityInventory(be);
    }

    private void createBlockEntityInventory(ResurrectionTableBlockEntity be) {
        // Obtén la capacidad de inventario usando ForgeCapabilities.ITEM_HANDLER
        LazyOptional<IItemHandler> itemHandlerOptional = be.getCapability(ForgeCapabilities.ITEM_HANDLER);

        // Verifica si el inventario está presente
        itemHandlerOptional.ifPresent(inventory -> {
            // Solo se puede colocar el PlayerSoul en el primer slot
            addSlot(new SlotItemHandler(inventory, 0, 56, 17) {
                @Override
                public boolean mayPlace(@NotNull ItemStack stack) {
                    return stack.getItem() == ModItems.PLAYERSOUL.get();
                }
            });

            // Solo se puede colocar el Corazón del Mar en el segundo slot
            addSlot(new SlotItemHandler(inventory, 1, 56, 53) {
                @Override
                public boolean mayPlace(@NotNull ItemStack stack) {
                    return stack.getItem() == Items.HEART_OF_THE_SEA;
                }
            });
        });
    }

    private void createPlayerInventory(Inventory playerInv) {
        // Inventario del jugador
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                addSlot(new Slot(playerInv, 9 + column + (row * 9), 8 + (column * 18), 84 + (row * 18)));
            }
        }
    }

    private void createPlayerHotbar(Inventory playerInv) {
        // Barra rápida del jugador
        for (int column = 0; column < 9; column++) {
            addSlot(new Slot(playerInv, column, 8 + (column * 18), 142));
        }
    }

    public BlockPos getBlockPos() {
        return this.blockPos; // Asegúrate de que blockPos se inicialice en el constructor
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        Slot fromSlot = getSlot(index);
        ItemStack fromStack = fromSlot.getItem();

        if (fromStack.getCount() <= 0)
            fromSlot.set(ItemStack.EMPTY);

        if (!fromSlot.hasItem())
            return ItemStack.EMPTY;

        ItemStack copyFromStack = fromStack.copy();

        if (index < 36) {
            // Dentro del inventario del jugador
            if (!moveItemStackTo(fromStack, 36, 38, false)) // Cambia según el número de slots en el blockEntity
                return ItemStack.EMPTY;
        } else if (index < 38) {
            // Dentro del inventario de la blockEntity
            if (!moveItemStackTo(fromStack, 0, 36, false))
                return ItemStack.EMPTY;
        } else {
            System.err.println("Invalid slot index: " + index);
            return ItemStack.EMPTY;
        }

        fromSlot.setChanged();
        fromSlot.onTake(player, fromStack);

        return copyFromStack;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return stillValid(this.levelAccess, player, ModBlocks.RESURRECTION_TABLE.get());
    }

    public ResurrectionTableBlockEntity getBlockEntity() {
        return this.blockEntity;
    }
}
