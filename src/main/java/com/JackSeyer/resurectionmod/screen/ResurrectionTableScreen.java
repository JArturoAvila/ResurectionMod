package com.JackSeyer.resurectionmod.screen;

import com.JackSeyer.resurectionmod.blockentity.ResurrectionTableBlockEntity;
import com.JackSeyer.resurectionmod.network.ModNetwork;
import com.JackSeyer.resurectionmod.network.ResurrectionPacket;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class ResurrectionTableScreen extends AbstractContainerScreen<ResurrectionTableMenu> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation("resurectionmod", "textures/gui/resurrection_table.png");

    public ResurrectionTableScreen(ResurrectionTableMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    protected void init() {
        super.init();

        this.addRenderableWidget(
                new Button.Builder(
                        Component.translatable("Revivir"),
                        button -> {
                            // Envía un paquete al servidor con la posición del bloque de la mesa
                            BlockPos blockPos = this.menu.getBlockPos(); // Asegúrate de que getBlockPos() está correctamente definido en el menú
                            ModNetwork.CHANNEL.sendToServer(new ResurrectionPacket(blockPos)); // Aquí pasamos la posición correcta
                        }
                )
                        .pos(this.leftPos + 110, this.topPos + 55)
                        .size(50, 20)
                        .build()
        );
    }







    @Override
    protected void renderBg(@NotNull GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        // Dibuja el fondo de la GUI
        pGuiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    public void render(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }
}
