package com.github.alexthe666.sizechange;

import net.minecraftforge.common.MinecraftForge;

import com.github.alexthe666.sizechange.event.ClientEvents;

public class ClientProxy extends CommonProxy {

	public void render() {
		MinecraftForge.EVENT_BUS.register(new ClientEvents());
	}
}
