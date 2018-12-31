package com.github.alexthe666.sizechange.items;

import com.github.alexthe666.sizechange.SizeChange;
import net.minecraft.item.Item;

public class ItemCore extends Item {

    public ItemCore(boolean shrink) {
        this.setUnlocalizedName(shrink ? "sizechange.shrink_core" : "sizechange.growth_core");
        this.setCreativeTab(SizeChange.tab);
    }
}
