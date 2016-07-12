package mrriegel.jjetpacks.items;

import net.minecraft.item.ItemStack;

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
			return 100;
		case 1:
			return 800;
		case 2:
			return 6400;
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
	public int reduceFuel(ItemStack stack, int amount, boolean hover, boolean simulate) {
		switch (stack.getItemDamage()) {
		case 0:
			return extractEnergy(stack, 10 * amount, simulate);
		case 1:
			return extractEnergy(stack, (hover ? 10 : 80) * amount, simulate);
		case 2:
			return extractEnergy(stack, (hover ? 10 : 640) * amount, simulate);
		default:
			break;
		}
		return 0;
	}

	@Override
	public double getMaxVerticalSpeed(ItemStack stack) {
		switch (stack.getItemDamage()) {
		case 0:
			return 0.25;
		case 1:
			return 0.35;
		case 2:
			return 0.5;
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
		return getEnergyStored(stack);
	}

	@Override
	public int getMaxFuel(ItemStack stack) {
		return getMaxEnergyStored(stack);
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

}
