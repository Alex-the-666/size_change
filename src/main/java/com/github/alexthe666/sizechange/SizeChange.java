package com.github.alexthe666.sizechange;

import com.github.alexthe666.sizechange.entity.EntityGrowCharge;
import com.github.alexthe666.sizechange.entity.EntityShrinkCharge;
import com.github.alexthe666.sizechange.entity.SizeChangeEntityProperties;
import com.github.alexthe666.sizechange.event.CommonEvents;
import com.github.alexthe666.sizechange.items.ModItems;
import com.github.alexthe666.sizechange.message.MessageUseWeapon;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.ilexiconn.llibrary.server.network.NetworkWrapper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.registries.GameData;

@Mod(modid = SizeChange.MODID, name = SizeChange.NAME, version = SizeChange.VERSION)
public class SizeChange {
    public static final String MODID = "sizechange";
    public static final String NAME = "Size Change";
    public static final String VERSION = "1.0";
    @Instance(value = MODID)
    public static SizeChange INSTANCE;
    @SidedProxy(clientSide = "com.github.alexthe666.sizechange.ClientProxy", serverSide = "com.github.alexthe666.sizechange.CommonProxy")
    public static CommonProxy PROXY;
    public static CreativeTabs tab;
    public static SoundEvent ray;
    @NetworkWrapper({ MessageUseWeapon.class })
    public static SimpleNetworkWrapper NETWORK_WRAPPER;
    //-Dfml.coreMods.load=com.github.alexthe666.sizechange.asm.SizeChangeLoadingPlugin
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        tab = new CreativeTabs("sizechange") {
            @Override
            public ItemStack getTabIconItem() {
                return new ItemStack(ModItems.shrink_ray);
            }
        };
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        EntityPropertiesHandler.INSTANCE.registerProperties(SizeChangeEntityProperties.class);
        MinecraftForge.EVENT_BUS.register(new CommonEvents());
        EntityRegistry.registerModEntity(new ResourceLocation("size_change:shrink_charge"), EntityShrinkCharge.class, "shrink_charge", 0, INSTANCE, 80, 3, true);
        EntityRegistry.registerModEntity(new ResourceLocation("size_change:grow_charge"), EntityGrowCharge.class, "grow_charge", 1, INSTANCE, 80, 3, true);
        PROXY.render();
        ray = GameData.register_impl(new SoundEvent(new ResourceLocation("sizechange", "ray")).setRegistryName(new ResourceLocation("sizechange", "ray")));
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
    }
}
