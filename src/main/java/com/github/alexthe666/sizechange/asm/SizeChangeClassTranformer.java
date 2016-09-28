package com.github.alexthe666.sizechange.asm;

import net.minecraft.launchwrapper.IClassTransformer;

public class SizeChangeClassTranformer implements IClassTransformer {
    @Override
    public byte[] transform(String name, String transformedName, byte[] classBytes) {
        return classBytes;
    }
}
