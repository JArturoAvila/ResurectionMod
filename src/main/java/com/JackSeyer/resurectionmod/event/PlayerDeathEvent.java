package com.JackSeyer.resurectionmod.event;

import com.JackSeyer.resurectionmod.item.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetSubtitleTextPacket;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashSet;
import java.util.Set;

@Mod.EventBusSubscriber
public class PlayerDeathEvent {

    public static final Set<ServerPlayer> deadPlayers = new HashSet<>();

    @SubscribeEvent
    public static void onPlayerDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof ServerPlayer) {
            ServerPlayer player = (ServerPlayer) event.getEntity();
            ServerLevel level = (ServerLevel) player.level();

            // 1. Mensaje en el chat
            String deathMessage = player.getName().getString() + " murió en " +
                    Math.round(player.getX()) + ", " +
                    Math.round(player.getY()) + ", " +
                    Math.round(player.getZ());
            level.getServer().getPlayerList().broadcastSystemMessage(Component.literal(deathMessage), false);

            // 2. Reproducir un sonido de muerte
            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.GENERIC_EXPLODE,
                    player.getSoundSource(), 1.0F, 1.0F);

            // 3. Mostrar un título a todos los jugadores
            Component title = Component.literal(player.getName().getString() + " ha caído!")
                    .withStyle(ChatFormatting.DARK_RED);

            Component subtitle = Component.literal("En las coordenadas: " + Math.round(player.getX()) + ", " + Math.round(player.getY()) + ", " + Math.round(player.getZ()))
                    .withStyle(ChatFormatting.DARK_RED);

            for (ServerPlayer onlinePlayer : level.getServer().getPlayerList().getPlayers()) {
                onlinePlayer.connection.send(new ClientboundSetTitleTextPacket(title));
                onlinePlayer.connection.send(new ClientboundSetSubtitleTextPacket(subtitle));
            }

            // Crea el ItemStack del alma del jugador
            ItemStack playerSoul = new ItemStack(ModItems.PLAYERSOUL.get());
            playerSoul.setHoverName(Component.literal(player.getName().getString() + "'s Soul"));

            // Verificar si el jugador murió en el End y por caída fuera del mundo (y < 0)
            if (level.dimension() == Level.END && player.getY() < 0) {
                // Dropea el item en coordenadas seguras en el End (cerca del portal de salida)
                double safeX = -3.0;
                double safeY = 64.0;  // Altura segura
                double safeZ = 0.0;

                ItemEntity itemEntity = new ItemEntity(level, safeX, safeY, safeZ, playerSoul);
                itemEntity.setUnlimitedLifetime();
                level.addFreshEntity(itemEntity);

                // Mandar mensaje con las coordenadas del drop
                String dropMessage = player.getName().getString() + "'s Soul fue dropeada en el portal de del end";
                level.getServer().getPlayerList().broadcastSystemMessage(Component.literal(dropMessage), false);
            } else if (player.getY() < level.getMinBuildHeight()) {
                // Dropear el ítem en la superficie más cercana si cayó por debajo del mundo en otras dimensiones
                int safeY = findSafeY(level, player.blockPosition().above());

                ItemEntity itemEntity = new ItemEntity(level, player.getX(), safeY, player.getZ(), playerSoul);
                itemEntity.setUnlimitedLifetime();
                level.addFreshEntity(itemEntity);

                // Mandar mensaje con las coordenadas del drop
                String dropMessage = player.getName().getString() + "'s Soul fue dropeada en " +
                        Math.round(player.getX()) + ", " + safeY + ", " + Math.round(player.getZ());
                level.getServer().getPlayerList().broadcastSystemMessage(Component.literal(dropMessage), false);
            } else {
                // Dropea el ítem en el lugar de la muerte si no fue fuera del mundo
                ItemEntity itemEntity = new ItemEntity(player.level(), player.getX(), player.getY(), player.getZ(), playerSoul);
                itemEntity.setUnlimitedLifetime();
                player.level().addFreshEntity(itemEntity);
            }


            // Añadir al jugador a la lista de muertos (para procesar en el siguiente tick)
            deadPlayers.add(player);
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        // Verificar si el jugador está en la lista de "muertos" para cambiar el modo de juego en el siguiente tick
        if (event.player instanceof ServerPlayer player && deadPlayers.contains(player)) {
            player.setGameMode(GameType.SPECTATOR);
            deadPlayers.remove(player); // Eliminamos de la lista una vez hecho el cambio
        }
    }

    // Evento para remover al jugador de la lista de "muertos" si revive
    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            deadPlayers.remove(player);  // Elimina al jugador de la lista si revive
        }
    }

    // Método para buscar una altura segura (primer bloque sólido) donde dropear el ítem
    private static int findSafeY(ServerLevel level, BlockPos pos) {
        for (int y = level.getMaxBuildHeight(); y > level.getMinBuildHeight(); y--) {
            BlockPos checkPos = new BlockPos(pos.getX(), y, pos.getZ());
            if (!level.getBlockState(checkPos).isAir()) {
                return y + 1;  // Retorna la primera coordenada con bloque sólido
            }
        }
        return level.getMinBuildHeight();  // Si no hay bloque sólido, dropea en el nivel mínimo
    }
}
