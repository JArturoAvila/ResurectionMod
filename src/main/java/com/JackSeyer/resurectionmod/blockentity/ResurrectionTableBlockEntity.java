package com.JackSeyer.resurectionmod.blockentity;

import com.JackSeyer.resurectionmod.init.ModBlockEntities;
import com.JackSeyer.resurectionmod.screen.ResurrectionTableMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ResurrectionTableBlockEntity extends BlockEntity implements MenuProvider {
    private static final Component TITLE =
            Component.translatable("container.resurrection_table");

    // Define un inventario con dos slots para el bloque
    private final ItemStackHandler inventory = new ItemStackHandler(2) {
        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            ResurrectionTableBlockEntity.this.setChanged(); // Marca cambios
        }
    };

    // LazyOptional es una capa de abstracción para capacidades como inventarios
    private final LazyOptional<ItemStackHandler> optional = LazyOptional.of(() -> this.inventory);

    public ResurrectionTableBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.RESURRECTION_TABLE_BLOCK_ENTITY.get(), pos, state);
    }
    public void openMenu(ServerPlayer player) {
        NetworkHooks.openScreen(player, this, this.getBlockPos());
    }

    @Override
    public void load(@NotNull CompoundTag nbt) {
        super.load(nbt);
        CompoundTag resurrectionData = nbt.getCompound("ResurrectionTableData");
        this.inventory.deserializeNBT(resurrectionData.getCompound("Inventory"));
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag nbt) {
        super.saveAdditional(nbt);
        CompoundTag resurrectionData = new CompoundTag();
        resurrectionData.put("Inventory", this.inventory.serializeNBT());
        nbt.put("ResurrectionTableData", resurrectionData);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return this.optional.cast();
        }
        return super.getCapability(cap);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        this.optional.invalidate();
    }

    public ItemStackHandler getInventory() {
        return this.inventory;
    }

    @Override
    public @NotNull Component getDisplayName() {
        return TITLE; // Nombre del contenedor que se muestra en la GUI
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, @NotNull Inventory pPlayerInventory, Player pPlayer) {
        return new ResurrectionTableMenu(pContainerId, pPlayerInventory, this);
    }
}
