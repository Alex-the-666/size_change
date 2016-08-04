package com.github.alexthe666.sizechange.event;

import com.github.alexthe666.sizechange.SizeChangeUtils;
import com.github.alexthe666.sizechange.items.ItemRay;
import net.ilexiconn.llibrary.LLibrary;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ClientEvents {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final ResourceLocation GUARDIAN_BEAM_TEXTURE = new ResourceLocation("textures/entity/guardian_beam.png");

    @SubscribeEvent
    public void onLivingRender(RenderLivingEvent.Pre event) {
        event.setCanceled(true);
        doRender(event.getRenderer(), event.getEntity(), event.getX(), event.getY(), event.getZ(), event.getEntity().rotationYaw, LLibrary.PROXY.getPartialTicks());
    }

    public void doRender(RenderLivingBase render, EntityLivingBase entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.disableCull();
        // render.getMainModel().swingProgress = render.getSwingProgress(entity,
        // partialTicks);
        render.getMainModel().swingProgress = invokeFloatFromMethod(reflectMethod(render, new String[] { "getSwingProgress", "func_77040_d" }, EntityLivingBase.class, float.class), render, entity, partialTicks);
        boolean shouldSit = entity.isRiding() && (entity.getRidingEntity() != null && entity.getRidingEntity().shouldRiderSit());
        render.getMainModel().isRiding = shouldSit;
        render.getMainModel().isChild = entity.isChild();
        try {
            float f = invokeFloatFromMethod(reflectMethod(render, new String[] { "interpolateRotation", "func_77034_a" }, float.class, float.class, float.class), render, entity.prevRenderYawOffset, entity.renderYawOffset, partialTicks);
            // render.interpolateRotation(entity.prevRenderYawOffset,
            // entity.renderYawOffset, partialTicks);
            float f1 = invokeFloatFromMethod(reflectMethod(render, new String[] { "interpolateRotation", "func_77034_a" }, float.class, float.class, float.class), render, entity.prevRotationYawHead, entity.rotationYawHead, partialTicks);
            // float f1 = render.interpolateRotation(entity.prevRotationYawHead,
            // entity.rotationYawHead, partialTicks);
            float f2 = f1 - f;

            if (shouldSit && entity.getRidingEntity() instanceof EntityLivingBase) {
                EntityLivingBase entitylivingbase = (EntityLivingBase) entity.getRidingEntity();
                f = invokeFloatFromMethod(reflectMethod(render, new String[] { "interpolateRotation", "func_77034_a" }, float.class, float.class, float.class), render, entitylivingbase.prevRenderYawOffset, entitylivingbase.renderYawOffset, partialTicks);
                // f =
                // render.interpolateRotation(entitylivingbase.prevRenderYawOffset,
                // entitylivingbase.renderYawOffset, partialTicks);
                f2 = f1 - f;
                float f3 = MathHelper.wrapDegrees(f2);

                if (f3 < -85.0F) {
                    f3 = -85.0F;
                }

                if (f3 >= 85.0F) {
                    f3 = 85.0F;
                }

                f = f1 - f3;

                if (f3 * f3 > 2500.0F) {
                    f += f3 * 0.2F;
                }
            }

            float f7 = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
            invokeMethod(reflectMethod(render, new String[] { "renderLivingAt", "func_77039_a" }, EntityLivingBase.class, double.class, double.class, double.class), render, entity, x, y, z);
            GL11.glScalef(SizeChangeUtils.getScale(entity), SizeChangeUtils.getScale(entity), SizeChangeUtils.getScale(entity));
            // render.renderLivingAt(entity, x, y, z);
            float f8 = invokeFloatFromMethod(reflectMethod(render, new String[] { "handleRotationFloat", "func_77044_a" }, EntityLivingBase.class, float.class), render, entity, partialTicks);
            // float f8 = render.handleRotationFloat(entity, partialTicks);
            // render.rotateCorpse(entity, f8, f, partialTicks);
            invokeMethod(reflectMethod(render, new String[] { "rotateCorpse", "func_77043_a" }, EntityLivingBase.class, float.class, float.class, float.class), render, entity, f8, f, partialTicks);

            float f4 = render.prepareScale(entity, partialTicks);
            float f5 = 0.0F;
            float f6 = 0.0F;

            if (!entity.isRiding()) {
                f5 = entity.prevLimbSwingAmount + (entity.limbSwingAmount - entity.prevLimbSwingAmount) * partialTicks;
                f6 = entity.limbSwing - entity.limbSwingAmount * (1.0F - partialTicks);

                if (entity.isChild()) {
                    f6 *= 3.0F;
                }

                if (f5 > 1.0F) {
                    f5 = 1.0F;
                }
                f6 *= SizeChangeUtils.getScale(entity);
            }
            GlStateManager.enableAlpha();
            render.getMainModel().setLivingAnimations(entity, f6, f5, partialTicks);
            render.getMainModel().setRotationAngles(f6, f5, f8, f2, f7, f4, entity);
            if (entity.isPotionActive(MobEffects.GLOWING)) {
                // if (invokeBooleanFromField(reflectField(new
                // String[]{"renderOutlines", "field_178639_r"}), render)) {
                // if (invokeBooleanFromField(reflectField(new
                // String[]{"renderOutlines", "field_178639_r"}), render)) {
                // boolean flag1 = render.setScoreTeamColor(entity);
                boolean flag1 = invokeBooleanFromMethod(reflectMethod(render, new String[] { "setScoreTeamColor", "func_177088_c" }, EntityLivingBase.class), render, entity);
                GlStateManager.enableColorMaterial();
                GlStateManager.enableOutlineMode(this.getTeamColor(render, entity));
                if (!invokeBooleanFromField(reflectField(new String[] { "renderMarker", "field_188323_j" }), render)) {
                    invokeMethod(reflectMethod(render, new String[] { "renderModel", "func_77036_a" }, EntityLivingBase.class, float.class, float.class, float.class, float.class, float.class, float.class), render, entity, f6, f5, f8, f2, f7, f4);
                    // render.renderModel(entity, f6, f5, f8, f2, f7, f4);
                }

                if (!(entity instanceof EntityPlayer) || !((EntityPlayer) entity).isSpectator()) {
                    invokeMethod(reflectMethod(render, new String[] { "renderLayers", "func_177093_a" }, EntityLivingBase.class, float.class, float.class, float.class, float.class, float.class, float.class, float.class), render, entity, f6, f5, partialTicks, f8, f2, f7, f4);
                    // render.renderLayers(entity, f6, f5, partialTicks, f8, f2,
                    // f7, f4);
                }

                GlStateManager.disableOutlineMode();
                GlStateManager.disableColorMaterial();

                if (flag1) {
                    // render.unsetScoreTeamColor();
                    invokeMethod(reflectMethod(render, new String[] { "unsetScoreTeamColor", "func_180565_e" }), render);

                }
            } else {
                boolean flag = invokeBooleanFromMethod(reflectMethod(render, new String[] { "setDoRenderBrightness", "func_177090_c" }, EntityLivingBase.class, float.class), render, entity, partialTicks);
                // boolean flag = render.setDoRenderBrightness(entity,
                // partialTicks);

                // render.renderModel(entity, f6, f5, f8, f2, f7, f4);
                invokeMethod(reflectMethod(render, new String[] { "renderModel", "func_77036_a" }, EntityLivingBase.class, float.class, float.class, float.class, float.class, float.class, float.class), render, entity, f6, f5, f8, f2, f7, f4);

                if (flag) {
                    invokeMethod(reflectMethod(render, new String[] { "unsetBrightness", "func_177091_f" }), render);
                    // render.unsetBrightness();
                }

                GlStateManager.depthMask(true);

                if (!(entity instanceof EntityPlayer) || !((EntityPlayer) entity).isSpectator()) {
                    invokeMethod(reflectMethod(render, new String[] { "renderLayers", "func_177093_a" }, EntityLivingBase.class, float.class, float.class, float.class, float.class, float.class, float.class, float.class), render, entity, f6, f5, partialTicks, f8, f2, f7, f4);
                    // render.renderLayers(entity, f6, f5, partialTicks, f8, f2,
                    // f7, f4);
                }
            }

            GlStateManager.disableRescaleNormal();
        } catch (Exception exception) {
            LOGGER.error((String) "Couldn\'t render entity", (Throwable) exception);
        }

        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.enableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        GlStateManager.enableCull();
        GlStateManager.popMatrix();
        if (!entity.isPotionActive(MobEffects.GLOWING)) {
            render.renderName(entity, x, y, z);
        }
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.RenderLivingEvent.Post(entity, render, x, y, z));
    }

    public Method reflectMethod(RenderLivingBase render, String[] names, Class<?>... methodTypes) {
        Method method = ReflectionHelper.findMethod(RenderLivingBase.class, render, names, methodTypes);
        return method;
    }

    public Field reflectField(String[] names) {
        Field field = ReflectionHelper.findField(RenderLivingBase.class, names);
        return field;
    }

    public boolean invokeBooleanFromField(Field field, RenderLivingBase render) {
        boolean b = false;
        try {
            b = (Boolean) field.get(render);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return b;
    }

    public float invokeFloatFromField(Field field, RenderLivingBase render) {
        float f = 0;
        try {
            f = (Float) field.get(render);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return f;
    }

    public float invokeFloatFromMethod(Method method, RenderLivingBase render, Object... args) {
        float f = 0;
        try {
            f = (Float) method.invoke(render, args);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return f;
    }

    public boolean invokeBooleanFromMethod(Method method, RenderLivingBase render, Object... args) {
        boolean b = false;
        try {
            b = (Boolean) method.invoke(render, args);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return b;
    }

    public int invokeIntFromMethod(Method method, RenderLivingBase render, Object... args) {
        int i = 0;
        try {
            i = (Integer) method.invoke(render, args);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return i;
    }

    public void invokeMethod(Method method, RenderLivingBase render, Object... args) {
        try {
            method.invoke(render, args);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    protected int getTeamColor(RenderLivingBase render, EntityLivingBase entityIn) {
        int i = 16777215;
        ScorePlayerTeam scoreplayerteam = (ScorePlayerTeam) entityIn.getTeam();

        if (scoreplayerteam != null) {
            String s = FontRenderer.getFormatFromString(scoreplayerteam.getColorPrefix());

            if (s.length() >= 2) {
            //    i = render.getFontRendererFromRenderManager().getColorCode(s.charAt(1));
            }
        }

        return i;
    }
}
