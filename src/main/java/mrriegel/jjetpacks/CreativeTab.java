package mrriegel.jjetpacks;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

public class CreativeTab {
	public static CreativeTabs tab1 = new CreativeTabs(JJetpacks.MODID) {
		@Override
		public Item getTabIconItem() {
			return Items.ARROW;
		}

		@Override
		public String getTranslatedTabLabel() {
			return JJetpacks.MODNAME;
		}
	};
}
