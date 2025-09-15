#version 150

in vec2 TexCoord;
in vec4 FragColor;

uniform sampler2D Sampler0;
uniform float Range;
uniform float Thickness;
uniform float Smoothness;
uniform bool Outline;
uniform float OutlineThickness;
uniform vec4 OutlineColor;

uniform float FadeStart;
uniform float FadeEnd;

out vec4 OutColor;

float median(vec3 color) {
    return max(min(color.r, color.g), min(max(color.r, color.g), color.b));
}

void main() {
    float dist = median(texture(Sampler0, TexCoord).rgb) - 0.5 + Thickness;
    vec2 h = vec2(dFdx(TexCoord.x), dFdy(TexCoord.y)) * textureSize(Sampler0, 0);
    float pixels = Range * inversesqrt(h.x * h.x + h.y * h.y);
    float baseAlpha = smoothstep(-Smoothness, Smoothness, dist * pixels);

    float fadeAlpha = 1.0;
    float fragX = gl_FragCoord.x;

    if (!(FadeStart == 0.0 && FadeEnd == 0.0) && FadeEnd > FadeStart) {
        fadeAlpha = 1.0 - smoothstep(FadeStart, FadeEnd, fragX);
    }

    float finalAlpha = baseAlpha * fadeAlpha;
    vec4 color = vec4(FragColor.rgb, FragColor.a * finalAlpha);

    if (Outline) {
        color = mix(OutlineColor, FragColor, baseAlpha);
        color.a *= finalAlpha * smoothstep(-Smoothness, Smoothness, (dist + OutlineThickness) * pixels);
    }

    OutColor = color;
}