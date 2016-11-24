package mrriegel.jjetpacks.gui;

import java.util.List;

import mrriegel.jjetpacks.items.ItemJetpackBase;
import mrriegel.limelib.gui.CommonContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class ContainerJetpack extends CommonContainer {

	public ContainerJetpack(InventoryPlayer playerInventory) {
		super(playerInventory);
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return ItemJetpackBase.getJetpack(playerIn) != null;
	}

	@Override
	protected void initSlots() {
		initPlayerSlots(8, 84);
	}

	@Override
	protected List<Area> allowedSlots(ItemStack stack, IInventory inv, int index) {
		return null;
	}

}
