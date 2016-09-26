package com.github.alexthe666.sizechange.message;

import com.github.alexthe666.sizechange.SizeChangeUtils;
import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageUseWeapon extends AbstractMessage<MessageUseWeapon> {

    private int entityId;

    public MessageUseWeapon(){

    }

    @Override
    public void onClientReceived(Minecraft client, MessageUseWeapon message, EntityPlayer player, MessageContext messageContext) {

    }

    @Override
    public void onServerReceived(MinecraftServer server, MessageUseWeapon message, EntityPlayer player, MessageContext messageContext) {
        Entity target = player.worldObj.getEntityByID(message.entityId);
        System.out.println("Entity = " + target);
        double dist = player.getDistanceSqToEntity(target);
        double reach = Math.max(5 * SizeChangeUtils.getScale(player), 3);
        if (dist >= reach) {
            player.attackTargetEntityWithCurrentItem(target);
        }
    }

    public MessageUseWeapon(int entityId){
        this.entityId = entityId;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        entityId = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(entityId);
    }

}
