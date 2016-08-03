package com.github.alexthe666.sizechange.items;

import com.github.alexthe666.sizechange.SizeChange;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemCore extends Item {

    public ItemCore(boolean shrink) {
        this.setUnlocalizedName(shrink ? "sizechange.shrink_core" : "sizechange.growth_core");
        this.setCreativeTab(SizeChange.tab);
        GameRegistry.register(this, new ResourceLocation((shrink ? "sizechange:shrink_core" : "sizechange:growth_core")));
    }
}
