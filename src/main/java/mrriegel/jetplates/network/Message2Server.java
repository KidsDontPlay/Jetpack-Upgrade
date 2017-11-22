package mrriegel.jetplates.network;

import mrriegel.jetplates.Jetpack;
import mrriegel.jetplates.Jetpack.GuiPos;
import mrriegel.limelib.helper.NBTHelper;
import mrriegel.limelib.network.AbstractMessage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;

public class Message2Server extends AbstractMessage {

	public Message2Server() {
	}

	public Message2Server(NBTTagCompound nbt, MessageAction action) {
		this.nbt = NBTHelper.set(nbt, "action", action);
	}

	@Override
	public void handleMessage(EntityPlayer player, NBTTagCompound nbt, Side side) {
		Jetpack jp = Jetpack.getJetpack(player);
		if (jp == null)
			return;
		switch (NBTHelper.get(nbt, "action", MessageAction.class)) {
		case GUI:
			if (NBTHelper.hasTag(nbt, "hspeed")) {
				jp.HSpeed = NBTHelper.get(nbt, "hspeed", Double.class);
				jp.VSpeed = NBTHelper.get(nbt, "vspeed", Double.class);
			} else if (NBTHelper.hasTag(nbt, "index")) {
				jp.guiPos = NBTHelper.get(nbt, "index", GuiPos.class);
			} else if (NBTHelper.hasTag(nbt, "hide")) {
				jp.hideGui = NBTHelper.get(nbt, "hide", Boolean.class);
			}
			break;
		case HOVER:
			NBTHelper.getSafe(nbt, "hover", boolean.class).ifPresent(b -> jp.hover = b);
			NBTHelper.getSafe(nbt, "active", boolean.class).ifPresent(b -> jp.active = b);
			if (jp.hover)
				player.fallDistance = 0f;
			Jetpack.syncDirty = true;
			break;
		case REDUCE:
			jp.reduceFuel(NBTHelper.get(nbt, "amount", Integer.class), false);
			player.fallDistance = 0f;
			break;
		}
	}

	public enum MessageAction {
		HOVER, REDUCE, GUI;
	}

}
