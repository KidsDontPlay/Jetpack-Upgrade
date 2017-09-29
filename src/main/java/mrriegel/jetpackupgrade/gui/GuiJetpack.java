package mrriegel.jetpackupgrade.gui;

import java.io.IOException;

import mrriegel.jetpackupgrade.Jetpack;
import mrriegel.jetpackupgrade.network.Message2Server;
import mrriegel.jetpackupgrade.network.Message2Server.MessageAction;
import mrriegel.limelib.gui.CommonGuiScreen;
import mrriegel.limelib.gui.button.CommonGuiButton;
import mrriegel.limelib.helper.NBTHelper;
import mrriegel.limelib.network.PacketHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.client.config.GuiCheckBox;
import net.minecraftforge.fml.client.config.GuiSlider;

public class GuiJetpack extends CommonGuiScreen {

	private GuiSlider vspeed, hspeed, acce;
	Jetpack jetpack;

	public GuiJetpack() {
		super();
		xSize = 145;
		ySize = 130;
	}

	@Override
	public void updateScreen() {
		super.updateScreen();
		vspeed.displayString = "Vertical Speed " + String.format("%.2f", vspeed.sliderValue);
		hspeed.displayString = "Horizontal Speed " + String.format("%.2f", hspeed.sliderValue);
		acce.displayString = "Accelaration " + String.format("%.2f", acce.sliderValue);
		//		buttonList.stream().filter(b -> b.id == jetpack.guiIndex).findAny().ifPresent(b -> b.enabled = false);
		buttonList.stream().filter(b -> b.id >= 0 && b.id <= 6).forEach(b -> b.enabled = b.id != jetpack.guiIndex);
	}

	@Override
	public void initGui() {
		super.initGui();
		jetpack = Jetpack.getJetpack(mc.player);
		buttonList.add(vspeed = new GuiSlider(100, guiLeft + 7, guiTop + 20, 130, 20, "Verctical", "", 0.0, 1.0, jetpack.VSpeed, false, false));
		vspeed.sliderValue = jetpack.VSpeed;
		buttonList.add(hspeed = new GuiSlider(101, guiLeft + 7, guiTop + 45, 130, 20, "Horizontal", "", 0.0, 1.0, jetpack.HSpeed, false, false));
		hspeed.sliderValue = jetpack.HSpeed;
		buttonList.add(acce = new GuiSlider(102, guiLeft + 7, guiTop + 70, 130, 20, "Accelaration", "", 0.0, 1.0, jetpack.acce, false, false));
		acce.sliderValue = jetpack.acce;
		buttonList.add(new CommonGuiButton(0, guiLeft + 100, guiTop + 93, 9, 9, null));
		buttonList.add(new CommonGuiButton(1, guiLeft + 110, guiTop + 93, 9, 9, null));
		buttonList.add(new CommonGuiButton(2, guiLeft + 120, guiTop + 93, 9, 9, null));
		buttonList.add(new CommonGuiButton(3, guiLeft + 100, guiTop + 103, 9, 9, null));
		//		buttonList.add(new CommonGuiButton(4, guiLeft + 110, guiTop + 103, 9, 9, null));
		buttonList.add(new CommonGuiButton(4, guiLeft + 120, guiTop + 103, 9, 9, null));
		buttonList.add(new CommonGuiButton(5, guiLeft + 100, guiTop + 113, 9, 9, null));
		//		buttonList.add(new CommonGuiButton(6, guiLeft + 110, guiTop + 113, 9, 9, null));
		buttonList.add(new CommonGuiButton(6, guiLeft + 120, guiTop + 113, 9, 9, null));

		buttonList.add(new GuiCheckBox(10, guiLeft + 10, guiTop + 100, "GUI Overlay", jetpack.guiIndex >= 0));

	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		super.actionPerformed(button);
		if (button.id < 7) {
			for (GuiButton b : buttonList)
				if (b.id < 7)
					b.enabled = true;
			button.enabled = false;
			jetpack.guiIndex = button.id;
			NBTTagCompound nbt = new NBTTagCompound();
			NBTHelper.set(nbt, "index", jetpack.guiIndex);
			PacketHandler.sendToServer(new Message2Server(nbt, MessageAction.GUI));
			buttonList.stream().filter(b -> b.id == 10).findAny().ifPresent(b -> ((GuiCheckBox) b).setIsChecked(true));
		} else if (button.id == 10) {
			jetpack.guiIndex = ((GuiCheckBox) button).isChecked() ? 0 : -1;
			NBTTagCompound nbt = new NBTTagCompound();
			NBTHelper.set(nbt, "index", jetpack.guiIndex);
			PacketHandler.sendToServer(new Message2Server(nbt, MessageAction.GUI));
		}
	}

	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
		send();
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {
		super.mouseReleased(mouseX, mouseY, state);
		send();
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		drawer.drawBackgroundTexture();
		super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
		fontRenderer.drawString("Jetpack", 7 + guiLeft, 7 + guiTop, 0x3e3e3e);
	}

	private void send() {
		NBTTagCompound nbt = new NBTTagCompound();
		NBTHelper.set(nbt, "hspeed", hspeed.sliderValue);
		NBTHelper.set(nbt, "vspeed", vspeed.sliderValue);
		NBTHelper.set(nbt, "acce", acce.sliderValue);
		PacketHandler.sendToServer(new Message2Server(nbt, MessageAction.GUI));
	}

}
