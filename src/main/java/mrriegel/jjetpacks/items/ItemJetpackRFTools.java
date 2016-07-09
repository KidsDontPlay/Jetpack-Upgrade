package mrriegel.jjetpacks.items;

import mrriegel.jjetpacks.network.MessageReduce;
import mrriegel.jjetpacks.network.PacketHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;

public class ItemJetpackRFTools extends ItemJetpackRF {

	@Override
	public int getMaxEnergyStored(ItemStack container) {
		switch (container.getItemDamage()) {
		case 0:
			return 50000;
		case 1:
			return 400000;
		case 2:
			return 3200000;
		default:
			break;
		}
		return 0;
	}

	@Override
	public int maxTransfer(ItemStack container) {
		switch (container.getItemDamage()) {
		case 0:
			return 50;
		case 1:
			return 400;
		case 2:
			return 3200;
		default:
			break;
		}
		return 0;
	}

	@Override
	public String getName() {
		return "rftoolsjetpack";
	}

	@Override
	public int getNumber() {
		return 3;
	}

	@Override
	public int reduceFuel(ItemStack stack, int amount, boolean simulate) {
		switch (stack.getItemDamage()) {
		case 0:
			return extractEnergy(stack, 10 * amount, simulate);
		case 1:
			return extractEnergy(stack, 80 * amount, simulate);
		case 2:
			return extractEnergy(stack, 640 * amount, simulate);
		default:
			break;
		}
		return 0;
	}

	@Override
	public float getMaxVerticalSpeed(ItemStack stack) {
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
	public float getMaxHorizontalSpeed(ItemStack stack) {
		switch (stack.getItemDamage()) {
		case 0:
			return 0.15f;
		case 1:
			return 0.35f;
		case 2:
			return 0.55f;
		default:
			break;
		}
		return 0;
	}

	@Override
	public float getAcceleration(ItemStack stack) {
		switch (stack.getItemDamage()) {
		case 0:
			return 0.1f;
		case 1:
			return 0.2f;
		case 2:
			return 0.3f;
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
	public boolean hover(ItemStack stack) {
		return false;
	}

	@Override
	public int getFuel(ItemStack stack) {
		return getEnergyStored(stack);
	}

	@Override
	public int getMaxFuel(ItemStack stack) {
		return getMaxEnergyStored(stack);
	}

}
