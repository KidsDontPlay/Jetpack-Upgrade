package mrriegel.jjetpacks.items;

import java.util.List;

import mrriegel.limelib.util.EnergyStorageExt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import cofh.api.energy.IEnergyContainerItem;

public abstract class ItemJetpackEnergy extends ItemJetpackBase implements IEnergyContainerItem {

	public ItemJetpackEnergy(String name) {
		super(name);
	}

	@Override
	public int receiveEnergy(ItemStack container, int maxReceive, boolean simulate) {
		if (!container.hasTagCompound()) {
			container.setTagCompound(new NBTTagCompound());
		}
		int energy = container.getTagCompound().getInteger("Energy");
		int energyReceived = Math.min(getMaxEnergyStored(container) - energy, Math.min(maxTransfer(container), maxReceive));

		if (!simulate) {
			energy += energyReceived;
			container.getTagCompound().setInteger("Energy", energy);
		}
		return energyReceived;
	}

	@Override
	public int extractEnergy(ItemStack container, int maxExtract, boolean simulate) {
		if (container.getTagCompound() == null || !container.getTagCompound().hasKey("Energy")) {
			return 0;
		}
		int energy = container.getTagCompound().getInteger("Energy");
		int energyExtracted = Math.min(energy, Math.min(maxTransfer(container), maxExtract));

		if (!simulate) {
			energy -= energyExtracted;
			container.getTagCompound().setInteger("Energy", energy);
		}
		return energyExtracted;
	}

	@Override
	public int getEnergyStored(ItemStack container) {
		if (container.getTagCompound() == null || !container.getTagCompound().hasKey("Energy")) {
			return 0;
		}
		return container.getTagCompound().getInteger("Energy");
	}

	@Override
	public ICapabilityProvider initCapabilities(final ItemStack stack, NBTTagCompound nbt) {
		return new ICapabilityProvider() {

			@Override
			public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
				return capability == CapabilityEnergy.ENERGY;
			}

			@Override
			public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
				if (capability == CapabilityEnergy.ENERGY) {
					return (T) new EnergyStorageExt(getMaxFuel(stack), maxTransfer(stack));
				}
				return null;
			}
		};
	}

	public abstract int maxTransfer(ItemStack container);

	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
		super.addInformation(stack, playerIn, tooltip, advanced);
		tooltip.add("RF: " + getFuel(stack) + "/" + getMaxFuel(stack));
	}

	@Override
	public void setMaxFuel(ItemStack stack) {
		while (receiveEnergy(stack, Integer.MAX_VALUE, false) > 0)
			;
	}

}
