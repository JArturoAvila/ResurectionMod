package com.JackSeyer.resurectionmod.blockentity;

import com.JackSeyer.resurectionmod.init.ModBlockEntities;
import com.JackSeyer.resurectionmod.item.ModItems;
import com.JackSeyer.resurectionmod.screen.ResurrectionTableMenu;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ResurrectionTableBlockEntity extends BlockEntity implements MenuProvider {
    private static final Component TITLE =
            Component.translatable("Resurrection Table");

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

    public void tryResurrect(Level level, BlockPos pos, Player player) {
        player.sendSystemMessage(Component.literal("Intentando iniciar resurrección..."));

        if (level.isClientSide) {
           // player.sendSystemMessage(Component.literal("Lado cliente, no se procede."));
            return; // Evita la ejecución en el lado del cliente
        }

        // Asegúrate de que el nivel y el servidor no sean nulos
        if (level == null || level.getServer() == null) {
          //  player.sendSystemMessage(Component.literal("El nivel o servidor no están disponibles."));
            return;
        }

        // Obtener la capacidad de inventario
        this.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(inventory -> {
            // Obtener los items de las ranuras
            ItemStack playerSoulStack = inventory.getStackInSlot(0);
            ItemStack heartOfSeaStack = inventory.getStackInSlot(1);

            // Verificar si ambos items están en las ranuras correspondientes
            if (playerSoulStack.getItem() == ModItems.PLAYERSOUL.get() && heartOfSeaStack.getItem() == Items.HEART_OF_THE_SEA) {

                // Extraer el nombre del jugador del PlayerSoul
                String playerName = playerSoulStack.getHoverName().getString().replace("'s Soul", "");
                ServerPlayer deadPlayer = level.getServer().getPlayerList().getPlayerByName(playerName);

                if (deadPlayer == null) {
                    // Si el jugador no está conectado, cerrar la mesa y devolver los items
                    player.sendSystemMessage(Component.literal(playerName + " no está conectado."));
                    player.drop(playerSoulStack, false);
                    player.drop(heartOfSeaStack, false);
                } else {

                    // Si el jugador está conectado, eliminar los items
                    inventory.extractItem(0, 1, false);
                    inventory.extractItem(1, 1, false);

                    // Crear un rayo en la mesa
                    LightningBolt lightning = EntityType.LIGHTNING_BOLT.create(level);
                    lightning.moveTo(Vec3.atBottomCenterOf(pos));
                    level.addFreshEntity(lightning);

                    // Usar un método de programación para esperar un tiempo antes de teletransportar al jugador
                    level.getServer().execute(() -> {
                        // Teleportar y revivir al jugador
                        ServerLevel serverLevel = (ServerLevel) level;
                        deadPlayer.teleportTo(serverLevel, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, deadPlayer.getYRot(), deadPlayer.getXRot());
                        deadPlayer.setGameMode(GameType.SURVIVAL);

                        // Enviar mensaje dorado en el chat
                        Component resurrectMessage = Component.literal(playerName + " ha resucitado de entre los muertos!").withStyle(ChatFormatting.GOLD);
                        level.getServer().getPlayerList().broadcastSystemMessage(resurrectMessage, false);
                    });
                }
            } else {
                player.sendSystemMessage(Component.literal("Items incorrectos o faltantes."));
            }
        });
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
