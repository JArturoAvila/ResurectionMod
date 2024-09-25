package com.JackSeyer.resurectionmod.command;

import com.JackSeyer.resurectionmod.event.PlayerDeathEvent;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.GameType;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ReviveCommand {

    // Evento para registrar el comando
    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        dispatcher.register(
                Commands.literal("revive")
                        .requires(commandSource -> commandSource.hasPermission(2)) // Solo jugadores con permiso nivel 2 (operadores) pueden usar el comando
                        .then(Commands.argument("player", StringArgumentType.string()) // Recibe un argumento con el nombre del jugador
                                .executes(context -> {
                                    String playerName = StringArgumentType.getString(context, "player");
                                    ServerPlayer targetPlayer = context.getSource().getServer().getPlayerList().getPlayerByName(playerName);

                                    if (targetPlayer != null) {
                                        // Cambiar el modo de juego del jugador a supervivencia
                                        targetPlayer.setGameMode(GameType.SURVIVAL);

                                        // Remover al jugador de la lista de "muertos"
                                        PlayerDeathEvent.deadPlayers.remove(targetPlayer);

                                        // Enviar un mensaje al jugador
                                        context.getSource().sendSuccess(() ->
                                                targetPlayer.getDisplayName().copy().append(" ha sido revivido!"), true);

                                        return 1; // Retorna éxito
                                    } else {
                                        context.getSource().sendFailure(Component.literal("El jugador no fue encontrado."));
                                        return 0; // Retorna fallo
                                    }
                                })
                        )
        );
    }
}
