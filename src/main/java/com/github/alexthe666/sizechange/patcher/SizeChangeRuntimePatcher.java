package com.github.alexthe666.sizechange.patcher;

import com.github.alexthe666.sizechange.asm.SizeChangeHooks;
import com.github.alexthe666.sizechange.asm.SizeChangeLoadingPlugin;
import net.ilexiconn.llibrary.server.asm.RuntimePatcher;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.renderer.EntityRenderer;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;

public class SizeChangeRuntimePatcher extends RuntimePatcher {
    @Override
    public void onInit() {
        this.patchClass(PlayerControllerMP.class)
                .patchMethod("getBlockReachDistance", float.class)
                    .apply(Patch.REPLACE_NODE, data -> {
                        if (data.node.getOpcode() == LDC) {
                            LdcInsnNode ldcNode = (LdcInsnNode) data.node;
                            return ldcNode.cst instanceof Float && (Float) ldcNode.cst == 5.0F;
                        }
                        return false;
                    }, method -> method.method(INVOKESTATIC, SizeChangeHooks.class, "getReachDistanceCreative", float.class))
                    .apply(Patch.REPLACE_NODE, data -> {
                        if (data.node.getOpcode() == LDC) {
                            LdcInsnNode ldcNode = (LdcInsnNode) data.node;
                            return ldcNode.cst instanceof Float && (Float) ldcNode.cst == 4.5F;
                        }
                        return false;
                    }, method -> method.method(INVOKESTATIC, SizeChangeHooks.class, "getReachDistanceSurvival", float.class));
        this.patchClass(EntityRenderer.class)
                .patchMethod("getMouseOver", void.class)
                .apply(Patch.REPLACE_NODE, data -> {
                    if (data.node.getOpcode() == LDC) {
                        LdcInsnNode ldcNode = (LdcInsnNode) data.node;
                        return ldcNode.cst instanceof Boolean && (Boolean) ldcNode.cst == true;
                    }
                    return false;
                }, method -> method.method(INVOKESTATIC, SizeChangeHooks.class, "canReachFlag", boolean.class));
        this.patchClass(Minecraft.class)
                .patchMethod("runTick", void.class)
                    .apply(Patch.REPLACE_NODE, data -> {
                        if (data.node.getOpcode() == INVOKEVIRTUAL) {
                            MethodInsnNode methodNode = (MethodInsnNode) data.node;
                            if (methodNode.name.equals(SizeChangeLoadingPlugin.development ? "getMouseOver" : "a") && methodNode.desc.equals("(F)V")) {
                                return true;
                            }
                        }
                        return false;
                    }, method -> method.method(INVOKESTATIC, SizeChangeHooks.class, "getMouseOver", void.class));
        this.patchClass(EntityRenderer.class)
                .patchMethod("orientCamera", void.class)
                .apply(Patch.REPLACE_NODE, data -> {
                    if (data.node.getOpcode() == LDC) {
                        LdcInsnNode ldcNode = (LdcInsnNode) data.node;
                        return ldcNode.cst instanceof Float && (Float) ldcNode.cst == 4.0F;
                    }
                    return false;
                }, method -> method.method(INVOKESTATIC, SizeChangeHooks.class, "get3rdPersonDistance", float.class));
        this.patchClass(EntityRenderer.class)
                .patchMethod("updateRenderer", void.class)
                .apply(Patch.REPLACE_NODE, data -> {
                    if (data.node.getOpcode() == LDC) {
                        LdcInsnNode ldcNode = (LdcInsnNode) data.node;
                        return ldcNode.cst instanceof Float && (Float) ldcNode.cst == 4.0F;
                    }
                    return false;
                }, method -> method.method(INVOKESTATIC, SizeChangeHooks.class, "get3rdPersonDistance", float.class));
    }
}
