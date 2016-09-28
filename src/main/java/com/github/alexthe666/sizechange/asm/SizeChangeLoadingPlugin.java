package com.github.alexthe666.sizechange.asm;

import com.github.alexthe666.sizechange.patcher.SizeChangeRuntimePatcher;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.MCVersion;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;

import java.util.Map;

@IFMLLoadingPlugin.Name("size_change")
@MCVersion("1.10.2")
@TransformerExclusions({ "com.github.alexthe666.sizechange.asm", "net.ilexiconn.llibrary.server.asm" })
@IFMLLoadingPlugin.SortingIndex(1002)
public class SizeChangeLoadingPlugin implements IFMLLoadingPlugin {
    public static boolean development;

    @Override
    public String[] getASMTransformerClass() {
        return new String[] { SizeChangeRuntimePatcher.class.getCanonicalName(), SizeChangeClassTranformer.class.getCanonicalName() };
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
        SizeChangeLoadingPlugin.development = !(Boolean) data.get("runtimeDeobfuscationEnabled");
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
