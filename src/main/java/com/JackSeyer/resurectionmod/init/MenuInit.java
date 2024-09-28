package com.JackSeyer.resurectionmod.init;

import com.JackSeyer.resurectionmod.ResurectionMod;
import com.JackSeyer.resurectionmod.screen.ResurrectionTableMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MenuInit {
    // Registro de tipos de menús para tu mod
    public static final DeferredRegister<MenuType<?>> MENU_TYPES =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, ResurectionMod.MODID);

    // Registro del menú de la mesa de resurrección
    public static final RegistryObject<MenuType<ResurrectionTableMenu>> RESURRECTION_TABLE_MENU = MENU_TYPES.register("resurrection_table_menu",
            () -> IForgeMenuType.create(ResurrectionTableMenu::new));

}
