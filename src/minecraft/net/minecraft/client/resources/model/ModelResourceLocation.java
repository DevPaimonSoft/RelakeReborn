package net.minecraft.client.resources.model;

import java.util.Locale;
import net.minecraft.resources.ResourceLocation;

public record ModelResourceLocation(ResourceLocation id, String variant) {
    public ModelResourceLocation(ResourceLocation id, String variant) {
        variant = lowercaseVariant(variant);
        this.id = id;
        this.variant = variant;
    }

    private static String lowercaseVariant(String pVariant) {
        return pVariant.toLowerCase(Locale.ROOT);
    }

    public String getVariant() {
        return this.variant;
    }

    @Override
    public String toString() {
        return this.id + "#" + this.variant;
    }
}