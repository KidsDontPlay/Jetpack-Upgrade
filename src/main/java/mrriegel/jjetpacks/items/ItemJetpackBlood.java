package mrriegel.jjetpacks.items;

import java.util.List;

import mrriegel.jjetpacks.helper.NBTHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import WayofTime.bloodmagic.api.saving.SoulNetwork;
import WayofTime.bloodmagic.api.util.helper.NetworkHelper;

public class ItemJetpackBlood extends ItemJetpackBase {

	@Override
	public String getName() {
		return "bloodjetpack";
	}

	@Override
	public int getNumber() {
		return 3;
	}

	@Override
	public int reduceFuel(ItemStack stack, int amount, boolean hover, boolean simulate) {
		switch (stack.getItemDamage()) {
		case 0:
			return extractEssence(stack, amount, simulate);
		case 1:
			return extractEssence(stack, (hover ? 1 : 8) * amount, simulate);
		case 2:
			return extractEssence(stack, (hover ? 1 : 64) * amount, simulate);
		default:
			break;
		}
		return 0;
	}

	public int extractEssence(ItemStack container, int maxExtract, boolean simulate) {
		if (container.getTagCompound() == null || !container.getTagCompound().hasKey("essence")) {
			return 0;
		}
		int energy = container.getTagCompound().getInteger("essence");
		int energyExtracted = Math.min(energy, maxExtract);

		if (!simulate) {
			energy -= energyExtracted;
			container.getTagCompound().setInteger("essence", energy);
		}
		return energyExtracted;
	}

	@Override
	public double getMaxVerticalSpeed(ItemStack stack) {
		switch (stack.getItemDamage()) {
		case 0:
			return 0.25f;
		case 1:
			return 0.35f;
		case 2:
			return 0.5f;
		default:
			break;
		}
		return 0;
	}

	@Override
	public double getMaxHorizontalSpeed(ItemStack stack) {
		switch (stack.getItemDamage()) {
		case 0:
			return 0.15;
		case 1:
			return 0.35;
		case 2:
			return 0.55;
		default:
			break;
		}
		return 0;
	}

	@Override
	public double getAcceleration(ItemStack stack) {
		switch (stack.getItemDamage()) {
		case 0:
			return 0.1;
		case 1:
			return 0.2;
		case 2:
			return 0.3;
		default:
			break;
		}
		return 0;
	}

	@Override
	public int getDamageReduce(ItemStack stack) {
		switch (stack.getItemDamage()) {
		case 0:
			return 2;
		case 1:
			return 3;
		case 2:
			return 5;
		default:
			break;
		}
		return 0;
	}

	@Override
	public double getToughness(ItemStack stack) {
		switch (stack.getItemDamage()) {
		case 0:
			return 0;
		case 1:
			return 0;
		case 2:
			return 1;
		default:
			break;
		}
		return 0;
	}

	@Override
	public int getFuel(ItemStack stack) {
		return NBTHelper.getInteger(stack, "essence");
	}

	@Override
	public int getMaxFuel(ItemStack stack) {
		switch (stack.getItemDamage()) {
		case 0:
			return 5000;
		case 1:
			return 40000;
		case 2:
			return 320000;
		default:
			break;
		}
		return 0;
	}

	@Override
	public void setMaxFuel(ItemStack stack) {
		NBTHelper.setInteger(stack, "essence", getMaxFuel(stack));
	}

	@Override
	public double getHoverSink(ItemStack stack) {
		switch (stack.getItemDamage()) {
		case 0:
			return -0.05;
		case 1:
			return -0.03;
		case 2:
			return 0;
		default:
			break;
		}
		return Double.NaN;
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
		super.addInformation(stack, playerIn, tooltip, advanced);
		tooltip.add("Essence: " + getFuel(stack) / 10 + "/" + getMaxFuel(stack) / 10);
	}

	@Override
	public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
		if (!world.isRemote) {
			int need = getMaxFuel(itemStack) - getFuel(itemStack);
			need = Math.min(need, 100);
			if (need > 0) {
				int extracted = syphon(NetworkHelper.getSoulNetwork(player), need / 10);
				NBTHelper.setInteger(itemStack, "essence", NBTHelper.getInteger(itemStack, "essence") + extracted * 10);
			}
		}
		super.onArmorTick(world, player, itemStack);
	}

	private int syphon(SoulNetwork soul, int syphon) {
		int essence = soul.getCurrentEssence();
		int essenceExtracted = Math.min(essence, syphon);
		essence -= essenceExtracted;
		soul.setCurrentEssence(essence);
		return essenceExtracted;
	}

}
