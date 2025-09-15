package net.minecraft.client.data.models.model;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;

public class DelegatedModel implements ModelInstance {
    private final ResourceLocation parent;

    public DelegatedModel(ResourceLocation pParent) {
        this.parent = pParent;
    }

    public JsonElement get() {
        JsonObject jsonobject = new JsonObject();
        jsonobject.addProperty("parent", this.parent.toString());
        return jsonobject;
    }
}