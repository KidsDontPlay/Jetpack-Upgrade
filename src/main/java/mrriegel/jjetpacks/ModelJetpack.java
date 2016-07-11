package mrriegel.jjetpacks;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;

public class ModelJetpack extends ModelBiped {

	public static final ModelJetpack INSTANCE = new ModelJetpack();

	public ModelJetpack() {
		super(1.0F, 0, 64, 64);

		this.bipedBody.showModel = true;
		this.bipedRightArm.showModel = false;
		this.bipedLeftArm.showModel = false;
		this.bipedHead.showModel = false;
		this.bipedHeadwear.showModel = false;
		this.bipedRightLeg.showModel = false;
		this.bipedLeftLeg.showModel = false;

		/* empty */
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

}
