package by.errortest.decorations;

import by.errortest.decorations.client.render.ModelWrapperDisplayList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.modelObj.obj.WavefrontObject;
import ru.d33.graphics.model.IModelCustom;

public class DecorModel {

    private IModelCustom model;
    private ResourceLocation modelTexture;
    public int id;
    private String nameArray[];
    public String name;
    int slashCount;

    public DecorModel(String modelPath, String modelTexturePath, int id) {
        this.id = id;
        this.model = new ModelWrapperDisplayList(new WavefrontObject(new ResourceLocation(Main.MODID, modelPath)));
        this.modelTexture = new ResourceLocation(Main.MODID, modelTexturePath);

        for (char obj : modelPath.toCharArray()) {
            if (obj == '/')
                slashCount++;
        }

        nameArray = modelPath.split("\\/");

        name = nameArray[slashCount];

    }

    public IModelCustom getModel() {
        return model;
    }

    public ResourceLocation getModelTexture() {
        return modelTexture;
    }

}
