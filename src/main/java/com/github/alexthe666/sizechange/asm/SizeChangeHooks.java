package com.github.alexthe666.sizechange.asm;

import com.github.alexthe666.sizechange.SizeChangeUtils;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import net.ilexiconn.llibrary.LLibrary;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.*;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.List;

public class SizeChangeHooks {

    public static float getReachDistanceCreative() {
        float scale = SizeChangeUtils.getScale(Minecraft.getMinecraft().player);
        return MathHelper.clamp(scale * 5F, 2, 18);
    }

    public static boolean canReachFlag() {
        return false;
    }


    public static float getReachDistanceSurvival() {
        float scale = SizeChangeUtils.getScale(Minecraft.getMinecraft().player);
        return MathHelper.clamp(scale * 4.5F, 2, 18);
    }

    public static float get3rdPersonDistance() {
        float scale = SizeChangeUtils.getScale(Minecraft.getMinecraft().player);
        return 10;
    }

    public static float get3rdPersonDistanceD3() {
        float scale = SizeChangeUtils.getScale(Minecraft.getMinecraft().player);
        float thirdPersonDistancePrev = 4 * scale;
        double d3 = (double)(thirdPersonDistancePrev + ((4.0F * scale) - thirdPersonDistancePrev) * LLibrary.PROXY.getPartialTicks());
        return 10;
    }

    public static void getMouseOver() {
        Entity entity = Minecraft.getMinecraft().getRenderViewEntity();
        float ticks = LLibrary.PROXY.getPartialTicks();
        if (entity != null) {
            if (Minecraft.getMinecraft().world != null) {
                Minecraft.getMinecraft().mcProfiler.startSection("pick");
                Minecraft.getMinecraft().pointedEntity = null;
                double d0 = (double) Minecraft.getMinecraft().playerController.getBlockReachDistance();
                Minecraft.getMinecraft().objectMouseOver = entity.rayTrace(d0, ticks);
                Vec3d vec3d = entity.getPositionEyes(ticks);
                boolean flag = false;
                int i = 3;
                double d1 = d0;
                float scale = Math.max(SizeChangeUtils.getScale(Minecraft.getMinecraft().player), 0.5F);

                if (Minecraft.getMinecraft().playerController.extendedReach()) {
                    d1 = 6.0D * scale;
                    d0 = d1;
                } else {
                    if (d0 > 3.0D * scale) {
                        flag = true;
                    }
                }

                if (Minecraft.getMinecraft().objectMouseOver != null) {
                    d1 = Minecraft.getMinecraft().objectMouseOver.hitVec.distanceTo(vec3d);
                }

                Vec3d vec3d1 = entity.getLook(ticks);
                Vec3d vec3d2 = vec3d.addVector(vec3d1.x * d0, vec3d1.y * d0, vec3d1.z * d0);
                setPointedEntity(null);
                Vec3d vec3d3 = null;
                List<Entity> list = Minecraft.getMinecraft().world.getEntitiesInAABBexcluding(entity, entity.getEntityBoundingBox().grow(vec3d1.x * d0, vec3d1.y * d0, vec3d1.z * d0).expand(1.0D, 1.0D, 1.0D), Predicates.and(EntitySelectors.NOT_SPECTATING, new Predicate<Entity>() {
                    public boolean apply(@Nullable Entity p_apply_1_) {
                        return p_apply_1_ != null && p_apply_1_.canBeCollidedWith();
                    }
                }));
                double d2 = d1;

                for (int j = 0; j < list.size(); ++j) {
                    Entity entity1 = (Entity) list.get(j);
                    AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().expand((double) entity1.getCollisionBorderSize(), (double) entity1.getCollisionBorderSize(), (double) entity1.getCollisionBorderSize());
                    RayTraceResult raytraceresult = axisalignedbb.calculateIntercept(vec3d, vec3d2);
                    if (axisalignedbb.contains(vec3d)) {
                        if (d2 >= 0.0D) {
                            setPointedEntity(entity1);
                            vec3d3 = raytraceresult == null ? vec3d : raytraceresult.hitVec;
                            d2 = 0.0D;
                        }
                    } else if (raytraceresult != null) {
                        double d3 = vec3d.distanceTo(raytraceresult.hitVec);

                        if (d3 < d2 || d2 == 0.0D) {
                            if (entity1.getLowestRidingEntity() == entity.getLowestRidingEntity() && !entity.canRiderInteract()) {
                                if (d2 == 0.0D) {
                                    setPointedEntity(entity1);
                                    vec3d3 = raytraceresult.hitVec;
                                }
                            } else {
                                setPointedEntity(entity1);
                                vec3d3 = raytraceresult.hitVec;
                                d2 = d3;
                            }
                        }
                    }
                }

                if (getPointedEntity() != null && vec3d.distanceTo(vec3d3) > 3.0D * scale) {
                    setPointedEntity(null);
                    Minecraft.getMinecraft().objectMouseOver = new RayTraceResult(RayTraceResult.Type.MISS, vec3d3, (EnumFacing) null, new BlockPos(vec3d3));
                }

                if (getPointedEntity() != null && (d2 < d1 || Minecraft.getMinecraft().objectMouseOver == null)) {
                    Minecraft.getMinecraft().objectMouseOver = new RayTraceResult(getPointedEntity(), vec3d3);

                    if (getPointedEntity() instanceof EntityLivingBase || getPointedEntity() instanceof EntityItemFrame) {
                        Minecraft.getMinecraft().pointedEntity = getPointedEntity();
                    }
                }

                Minecraft.getMinecraft().mcProfiler.endSection();
            }
        }
    }

    public static Entity getPointedEntity() {
        Field field = ReflectionHelper.findField(EntityRenderer.class, new String[]{"field_78528_u", "pointedEntity"});
        try {
            return (Entity) field.get(Minecraft.getMinecraft().entityRenderer);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void setPointedEntity(Entity entity) {
        Field field = ReflectionHelper.findField(EntityRenderer.class, new String[]{"field_78528_u", "pointedEntity"});
        try {
            field.set(Minecraft.getMinecraft().entityRenderer, entity);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
