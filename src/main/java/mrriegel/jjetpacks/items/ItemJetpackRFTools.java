package mrriegel.jjetpacks.items;

import mrriegel.jjetpacks.network.MessageUpdate;
import mrriegel.jjetpacks.network.PacketHandler;
import net.minecraft.item.ItemStack;

public class ItemJetpackRFTools extends ItemJetpackRF {

	@Override
	public int getMaxEnergyStored(ItemStack container) {
		switch (container.getItemDamage()) {
		case 0:
			return 50000;
		case 1:
			return 250000;
		case 2:
			return 1500000;
		default:
			break;
		}
		return 0;
	}

	@Override
	public int maxTransfer(ItemStack container) {
		switch (container.getItemDamage()) {
		case 0:
			return 500;
		case 1:
			return 5000;
		case 2:
			return 50000;
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
	public void reduceFuel(ItemStack stack) {
		switch (stack.getItemDamage()) {
		case 0:
			extractEnergy(stack, 50, false);
		case 1:
			extractEnergy(stack, 100, false);
		case 2:
			extractEnergy(stack, 150, false);
		default:
			break;
		}
	}

	@Override
	public float getMaxVerticalSpeed(ItemStack stack) {
		switch (stack.getItemDamage()) {
		case 0:
			return 0.25f;
		case 1:
			return 0.4f;
		case 2:
			return 0.6f;
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
			return 0.30f;
		case 2:
			return 0.45f;
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
	public boolean enoughFuel(ItemStack stack) {
		switch (stack.getItemDamage()) {
		case 0:
			return getEnergyStored(stack) > 50;
		case 1:
			return getEnergyStored(stack) > 100;
		case 2:
			return getEnergyStored(stack) > 150;
		default:
			break;
		}
		return false;
	}

	@Override
	public double getDamageReduce(ItemStack stack) {
		switch (stack.getItemDamage()) {
		case 0:
			return 0.3;
		case 1:
			return 0.4;
		case 2:
			return 0.5;
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
			return 1;
		case 2:
			return 2;
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
	public void updateServer() {
		PacketHandler.INSTANCE.sendToServer(new MessageUpdate());
	}

}
