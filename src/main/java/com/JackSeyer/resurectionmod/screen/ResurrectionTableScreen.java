package com.JackSeyer.resurectionmod.screen;

import com.JackSeyer.resurectionmod.screen.ResurrectionTableMenu; // Asegúrate de tener esta clase
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class ResurrectionTableScreen extends AbstractContainerScreen<ResurrectionTableMenu> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation("resurectionmod", "textures/gui/resurrection_table.png"); // Ajusta el nombre de tu textura

    public ResurrectionTableScreen(ResurrectionTableMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);

        this.imageWidth = 176; // Ancho de la GUI
        this.imageHeight = 166; // Alto de la GUI
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
