package mrriegel.jjetpacks.gui;

import mrriegel.jjetpacks.items.ItemJetpackBase;
import mrriegel.jjetpacks.network.MessageButton;
import mrriegel.limelib.gui.CommonGuiContainer;
import mrriegel.limelib.network.PacketHandler;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.client.config.GuiSlider;

public class GuiJetpack extends CommonGuiContainer {

	private final InventoryPlayer playerInventory;
	private GuiSlider vspeed, hspeed, acce;

	public GuiJetpack(ContainerJetpack inventorySlotsIn, InventoryPlayer playerInv) {
		super(inventorySlotsIn);
		this.playerInventory = playerInv;
	}

	@Override
	public void updateScreen() {
		super.updateScreen();
		vspeed.displayString = "Vertical Speed " + String.format("%.2f", vspeed.sliderValue);
		hspeed.displayString = "Horizontal Speed " + String.format("%.2f", hspeed.sliderValue);
		acce.displayString = "Accelaration " + String.format("%.2f", acce.sliderValue);
	}

	@Override
	public void initGui() {
		super.initGui();
		ItemStack jetpack = ItemJetpackBase.getJetpack(playerInventory.player);
		buttonList.add(vspeed = new GuiSlider(0, guiLeft + 7, guiTop + 5, 130, 20, "Verctical", "", 0.0, 1.0, ((ItemJetpackBase) jetpack.getItem()).getVSpeed(jetpack), false, false));
		vspeed.sliderValue = ((ItemJetpackBase) jetpack.getItem()).getVSpeed(jetpack);
		buttonList.add(hspeed = new GuiSlider(1, guiLeft + 7, guiTop + 30, 130, 20, "Horizontal", "", 0.0, 1.0, ((ItemJetpackBase) jetpack.getItem()).getHSpeed(jetpack), false, false));
		hspeed.sliderValue = ((ItemJetpackBase) jetpack.getItem()).getHSpeed(jetpack);
		buttonList.add(acce = new GuiSlider(2, guiLeft + 7, guiTop + 55, 130, 20, "Accelaration", "", 0.0, 1.0, ((ItemJetpackBase) jetpack.getItem()).getAcce(jetpack), false, false));
		acce.sliderValue = ((ItemJetpackBase) jetpack.getItem()).getAcce(jetpack);
	}

	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
		PacketHandler.sendToServer(new MessageButton(hspeed.sliderValue, vspeed.sliderValue, acce.sliderValue));
	}
	
	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {
		super.mouseReleased(mouseX, mouseY, state);
		PacketHandler.sendToServer(new MessageButton(hspeed.sliderValue, vspeed.sliderValue, acce.sliderValue));
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		drawer.drawBackgroundTexture();
		drawer.drawPlayerSlots(7, 83);
	}

}
