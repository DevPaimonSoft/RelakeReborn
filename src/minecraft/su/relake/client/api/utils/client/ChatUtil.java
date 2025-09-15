//package ru.kotopushka.client.api.utils.client;
//
//import com.mojang.brigadier.LiteralMessage;
//import net.minecraft.network.chat.Component;
//import net.minecraft.network.chat.HoverEvent;
//import net.minecraft.network.chat.MutableComponent;
//import ru.kotopushka.client.api.utils.render.util.ColorUtil;
//import ru.kotopushka.client.api.utils.render.util.IMinecraft;
//
//import java.awt.*;
//
//public class ChatUtil implements IMinecraft {
//
//
//    public static void sendChatMessage(String message) {
//        if (mc.player != null) {
//            Component prefixComponent = prefix();
//            Component messageComponent = Component.literal(message);
//            Component fullMessage = prefixComponent.copy().append(messageComponent);
//       //     mc.player.sendSystemMessage(fullMessage);
//        }
//    }
//
//
//
//    private static MutableComponent prefix() {
//        // Fallback color if Client is unavailable
//        int rgb = 0xFFFFFF; // Default white
//        try {
//        //    rgb = UColor.get();
//        } catch (Exception e) {
//            System.out.println("Failed to get HUD color, using default: " + e.getMessage());
//        }
//
//        int[] color = {
//         //       ColorUtil.darker(rgb, 35),
//    //            ColorUtil.darker(rgb, 75)
//        };
//
//     //   Color color1 = new Color(ColorUtil.getRed(color[0]), ColorUtil.getGreen(color[0]), ColorUtil.getBlue(color[0]));
//    //    Color color2 = new Color(ColorUtil.getRed(color[1]), ColorUtil.getGreen(color[1]), ColorUtil.getBlue(color[1]));
//
//        return genGradientText("[Relake] ", color1.getRGB(), color2.getRGB());
//    }
//
//
//    public static void sendHoverText(String text, String triggerText, String hoverText) {
//        MutableComponent itextcomponent = prefix().append(text);
//        itextcomponent.append(Component.literal(" " + triggerText)
//                .withStyle(style -> style.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal(hoverText)))));
//        mc.gui.getChat().addMessage(itextcomponent);
//    }
//
//
//    private static MutableComponent genGradientText(String text, int startColor, int endColor) {
//        if (text.isEmpty()) {
//            return Component.literal("");
//        }
//
//        MutableComponent result = Component.literal("");
//        int length = text.length();
//        for (int i = 0; i < length; i++) {
//            float progress = (float) i / (length - 1);
//            int r = (int) (((startColor >> 16) & 0xFF) * (1 - progress) + ((endColor >> 16) & 0xFF) * progress);
//            int g = (int) (((startColor >> 8) & 0xFF) * (1 - progress) + ((endColor >> 8) & 0xFF) * progress);
//            int b = (int) ((startColor & 0xFF) * (1 - progress) + (endColor & 0xFF) * progress);
//            result.append(Component.literal(String.valueOf(text.charAt(i)))
//                    .withStyle(style -> style.withColor((r << 16) + (g << 8) + b)));
//        }
//        return result;
//    }
//}
