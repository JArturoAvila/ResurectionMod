package com.JackSeyer.resurectionmod.network;

import com.JackSeyer.resurectionmod.blockentity.ResurrectionTableBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ResurrectionPacket {
    private final BlockPos pos;

    public ResurrectionPacket(BlockPos pos) {
        this.pos = pos;
    }

    // Método para escribir los datos al paquete
    public static void encode(ResurrectionPacket packet, FriendlyByteBuf buffer) {
        buffer.writeBlockPos(packet.pos);
    }

    // Método para leer los datos del paquete
    public static ResurrectionPacket decode(FriendlyByteBuf buffer) {
        return new ResurrectionPacket(buffer.readBlockPos());
    }

    // Método que se ejecutará cuando se reciba el paquete en el servidor
    public static void handle(ResurrectionPacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerPlayer player = context.get().getSender();
            if (player != null) {
                Level level = player.level();
                BlockPos pos = packet.pos;
                BlockEntity blockEntity = level.getBlockEntity(pos);

                if (blockEntity instanceof ResurrectionTableBlockEntity resurrectionTable) {
                    resurrectionTable.tryResurrect(level, pos, player);
                }
            }
        });
        context.get().setPacketHandled(true);
    }
}
