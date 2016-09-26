package com.github.alexthe666.sizechange.asm;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SizeChangeClassTranformer implements IClassTransformer {
	@Override
	public byte[] transform(String name, String transformedName, byte[] classBytes) {
		{
			boolean obf;
			ClassNode classNode = new ClassNode();
			if ((obf = "blm".equals(name)) || "net.minecraft.client.multiplayer.PlayerControllerMP".equals(name)) {
				ClassReader classReader = new ClassReader(classBytes);
				classReader.accept(classNode, 0);
				String searchingName = obf ? "d" : "getBlockReachDistance";
				String searchingDesc = "()F";
				for (int i = 0; i < classNode.methods.size(); i++) {
					MethodNode method = classNode.methods.get(i);
					if (searchingName.equals(method.name) && searchingDesc.equals(method.desc)) {
						InsnList insnList = method.instructions;
						for (int j = 0; j < insnList.size(); j++) {
								AbstractInsnNode insnNote = method.instructions.get(j);
								if(insnNote.getOpcode() == Opcodes.LDC){
									LdcInsnNode lcd_1 = (LdcInsnNode)insnNote;
									Float f = 5F;
									Float f1 = 4.5F;
									if(lcd_1.cst.equals(f)){
										MethodInsnNode method_1 = new MethodInsnNode(Opcodes.INVOKESTATIC, "com/github/alexthe666/sizechange/asm/SizeChangeHooks", "getReachDistanceCreative", "()F", false);
										insnList.insert(lcd_1, method_1);
									}
									if(lcd_1.cst.equals(f1)){
										MethodInsnNode method_1 = new MethodInsnNode(Opcodes.INVOKESTATIC, "com/github/alexthe666/sizechange/asm/SizeChangeHooks", "getReachDistanceSurvival", "()F", false);
										insnList.insert(lcd_1, method_1);
									}
								}
						}
						break;

					}
				}
			}
			if ((obf = "bnz".equals(name)) || "net.minecraft.client.renderer.EntityRenderer".equals(name)) {
				ClassReader classReader = new ClassReader(classBytes);
				classReader.accept(classNode, 0);
				String searchingName = obf ? "a" : "getMouseOver";
				String searchingDesc = "(F)V";
				System.out.print("1");
				for (int i = 0; i < classNode.methods.size(); i++) {
					MethodNode method = classNode.methods.get(i);
					if (searchingName.equals(method.name) && searchingDesc.equals(method.desc)) {
						InsnList insnList = method.instructions;
						for (int j = 0; j < insnList.size(); j++) {
							AbstractInsnNode insnNote = method.instructions.get(j);
							if(insnNote.getOpcode() == Opcodes.LDC){
								LdcInsnNode lcd_1 = (LdcInsnNode)insnNote;
								Double d = 3.0D;
								if(lcd_1.cst.equals(d)){
									MethodInsnNode method_1 = new MethodInsnNode(Opcodes.INVOKESTATIC, "com/github/alexthe666/sizechange/asm/SizeChangeHooks", "getFightingReach", "()D", false);
									insnList.insert(lcd_1, method_1);
								}
							}
						}
						break;

					}
				}
				ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
				classNode.accept(classWriter);
				return classWriter.toByteArray();
			}
		}
		return classBytes;
	}
}
