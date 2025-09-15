#version 150

#moj_import <relake:common.glsl>

in vec2 FragCoord;
in vec4 FragColor;

uniform vec2 Size;
uniform vec4 Radius;
uniform vec2 softness;
uniform float amount;
uniform float value;

out vec4 OutColor;

void main() {
    vec2 center = Size * 0.5;
    float dist = rdist(center - (FragCoord.xy * Size), center - 1.0 - value, Radius);
    float alpha = 1.0 - smoothstep(softness.x, softness.y, dist);

    vec4 color = vec4(FragColor.rgb, FragColor.a * pow(alpha, amount));

    if (color.a == 0.0) {
        discard;
    }

    OutColor = color;
}
