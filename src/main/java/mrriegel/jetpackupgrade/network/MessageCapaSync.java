package mrriegel.jetpackupgrade.network;

import mrriegel.jetpackupgrade.Jetpack;
import mrriegel.limelib.helper.NBTHelper;
import mrriegel.limelib.network.AbstractMessage;
import mrriegel.limelib.util.EnergyStorageExt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.relauncher.Side;

public class MessageCapaSync extends AbstractMessage {

	public MessageCapaSync() {
	}

	public MessageCapaSync(EntityPlayer player) {
		Jetpack jp = Jetpack.getJetpack(player);
		if (jp == null) {
			shouldSend = false;
			return;
		}
		nbt = jp.serializeNBT();
		IEnergyStorage en = jp.stack.getCapability(CapabilityEnergy.ENERGY, null);
		if (en instanceof EnergyStorageExt)
			NBTHelper.set(nbt, "energy", en.getEnergyStored());
	}

	@Override
	public void handleMessage(EntityPlayer player, NBTTagCompound nbt, Side side) {
		Jetpack jp = Jetpack.getJetpack(player);
		if (jp == null)
			return;
		jp.deserializeNBT(nbt);
		if (NBTHelper.hasTag(nbt, "energy"))
			((EnergyStorageExt) jp.stack.getCapability(CapabilityEnergy.ENERGY, null)).setEnergyStored(NBTHelper.get(nbt, "energy", Integer.class));
		//		System.out.println(jp.getFuel() + " fuel");
	}

}
