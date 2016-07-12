package mrriegel.jjetpacks.gui;

import java.io.IOException;

import mrriegel.jjetpacks.JJetpacks;
import mrriegel.jjetpacks.helper.Util;
import mrriegel.jjetpacks.items.ItemJetpackBase;
import mrriegel.jjetpacks.network.MessageButton;
import mrriegel.jjetpacks.network.PacketHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiSlider;

public class GuiJetpack extends GuiContainer {

	private static final ResourceLocation GUI_TEXTURES = new ResourceLocation(JJetpacks.MODID + ":textures/gui/jetpack.png");
	private final InventoryPlayer playerInventory;
	private GuiSlider vspeed, hspeed, acce;

	public GuiJetpack(ContainerJetpack inventorySlotsIn, InventoryPlayer playerInv) {
		super(inventorySlotsIn);
		this.playerInventory = playerInv;
	}

	@Override
	public void updateScreen() {
		super.updateScreen();
		vspeed.displayString = "Vertical " + String.format("%.2f", vspeed.sliderValue);
		hspeed.displayString = "Horizontal " + String.format("%.2f", hspeed.sliderValue);
		acce.displayString = "Accelaration " + String.format("%.2f", acce.sliderValue);
	}

	@Override
	public void initGui() {
		super.initGui();
		ItemStack jetpack = Util.getJetpack(playerInventory.player);
		buttonList.add(vspeed = new GuiSlider(0, guiLeft + 12, guiTop + 5, 130, 20, "Verctical", "", 0.0, 1.0, ((ItemJetpackBase) jetpack.getItem()).getVSpeed(jetpack), false, false));
		vspeed.sliderValue = ((ItemJetpackBase) jetpack.getItem()).getVSpeed(jetpack);
		buttonList.add(hspeed = new GuiSlider(1, guiLeft + 12, guiTop + 30, 130, 20, "Horizontal", "", 0.0, 1.0, ((ItemJetpackBase) jetpack.getItem()).getHSpeed(jetpack), false, false));
		hspeed.sliderValue = ((ItemJetpackBase) jetpack.getItem()).getHSpeed(jetpack);
		buttonList.add(acce = new GuiSlider(2, guiLeft + 12, guiTop + 55, 130, 20, "Accelaration", "", 0.0, 1.0, ((ItemJetpackBase) jetpack.getItem()).getAcce(jetpack), false, false));
		acce.sliderValue = ((ItemJetpackBase) jetpack.getItem()).getAcce(jetpack);
	}

	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
		PacketHandler.INSTANCE.sendToServer(new MessageButton(hspeed.sliderValue, vspeed.sliderValue, acce.sliderValue));
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		super.actionPerformed(button);
		PacketHandler.INSTANCE.sendToServer(new MessageButton(hspeed.sliderValue, vspeed.sliderValue, acce.sliderValue));
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		String s = Util.getJetpack(playerInventory.player).getDisplayName();
		this.fontRendererObj.drawString(s, this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
		this.fontRendererObj.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(GUI_TEXTURES);
		int i = (this.width - this.xSize) / 2;
		int j = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
	}

}
