package su.relake.client.api.utils.render.msdf;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexConsumer;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;
import su.relake.client.api.utils.render.providers.ResourceProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public final class MsdfFont {

    private final String name;
    private final AbstractTexture texture;
    private final FontData.AtlasData atlas;
    private final FontData.MetricsData metrics;
    private final Map<Integer, MsdfGlyph> glyphs;
    private final Map<Integer, Map<Integer, Float>> kernings;

    public MsdfFont(String name, AbstractTexture texture, FontData.AtlasData atlas, FontData.MetricsData metrics, Map<Integer, MsdfGlyph> glyphs, Map<Integer, Map<Integer, Float>> kernings) {
        this.name = name;
        this.texture = texture;
        this.atlas = atlas;
        this.metrics = metrics;
        this.glyphs = glyphs;
        this.kernings = kernings;
    }

    public int getTextureId() {
        return this.texture.getId();
    }


    public void applyGlyphs(Matrix4f matrix, VertexConsumer consumer, String text, float size, float thickness, float spacing, float x, float y, float z, int color) {
        int prevChar = -1;
        for (int i = 0; i < text.length(); i++) {
            int character = text.charAt(i);
            MsdfGlyph glyph = this.glyphs.get(character);

            if (glyph == null) continue;

            Map<Integer, Float> kerning = this.kernings.get(prevChar);
            if (kerning != null) {
                x += kerning.getOrDefault(character, 0.0f) * size;
            }

            x += glyph.apply(matrix, consumer, size, x, y, z, color) + thickness + spacing;
            prevChar = character;
        }
    }


    public float getWidth(String text, float size) {
        int prevChar = -1;
        float width = 0.0f;
        for (int i = 0; i < text.length(); i++) {
            int character = text.charAt(i);
            MsdfGlyph glyph = this.glyphs.get(character);

            if (glyph == null)
                continue;

            Map<Integer, Float> kerning = this.kernings.get(prevChar);
            if (kerning != null) {
                width += kerning.getOrDefault(character, 0.0f) * size;
            }

            width += glyph.getWidth(size); /* + thickness + spacing */
            prevChar = character;
        }

        return width;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String name = "?";
        private ResourceLocation dataIdentifer;
        private ResourceLocation atlasIdentifier;

        private Builder() {
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }


        public Builder data(String dataFileName) {
            this.dataIdentifer = ResourceLocation.fromNamespaceAndPath("relake", "fonts/" + dataFileName + ".json");
            return this;
        }


        public Builder atlas(String atlasFileName) {
            this.atlasIdentifier = ResourceLocation.fromNamespaceAndPath("relake", "fonts/" + atlasFileName + ".png");
            return this;
        }


        public MsdfFont build() {
            FontData data = ResourceProvider.fromJsonToInstance(this.dataIdentifer, FontData.class);
            AbstractTexture texture = Minecraft.getInstance().getTextureManager().getTexture(this.atlasIdentifier);

            if (data == null) {
                throw new RuntimeException("Failed to read font data file: " + this.dataIdentifer.toString() +
                        "; Are you sure this is json file? Try to check the correctness of its syntax.");
            }

            RenderSystem.recordRenderCall(() -> texture.setFilter(true, false));

            float aWidth = data.atlas().width();
            float aHeight = data.atlas().height();
            Map<Integer, MsdfGlyph> glyphs = data.glyphs().stream()
                    .collect(Collectors.<FontData.GlyphData, Integer, MsdfGlyph>toMap(
                            FontData.GlyphData::unicode,
                            (glyphData) -> new MsdfGlyph(glyphData, aWidth, aHeight)
                    ));

            Map<Integer, Map<Integer, Float>> kernings = new HashMap<>();
            data.kernings().forEach((kerning) -> {
                Map<Integer, Float> map = kernings.computeIfAbsent(kerning.leftChar(), k -> new HashMap<>());
                map.put(kerning.rightChar(), kerning.advance());
            });

            return new MsdfFont(this.name, texture, data.atlas(), data.metrics(), glyphs, kernings);
        }

    }

}