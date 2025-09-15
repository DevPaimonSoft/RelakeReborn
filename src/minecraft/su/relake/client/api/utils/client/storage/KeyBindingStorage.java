package su.relake.client.api.utils.client.storage;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.resources.language.I18n;
import org.lwjgl.glfw.GLFW;

import java.util.HashMap;
import java.util.Map;

public class KeyBindingStorage {
    private static final Map<String, Integer> keyMap = new HashMap<>();
    private static final Map<Integer, String> reverseKeyMap = new HashMap<>();

    public static String getKey(int integer) {
        if (integer < 0) {
            return switch (integer) {
                case -100 -> I18n.get("key.mouse.left");
                case -99 -> I18n.get("key.mouse.right");
                case -98 -> I18n.get("key.mouse.middle");
                default -> "Mouse" + (integer + 101);
            };
        } else {
            String key = InputConstants.getKey(integer, -1).getName();
            int keyboardIndex = key.indexOf("keyboard.");
            String result = key.substring(keyboardIndex + "keyboard.".length());
            result = result.replace(".", "_");
            result = result.replace("grave_accent", "`");

            return capitalize(result);
        }
    }

    private static String capitalize(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }

    public static Integer getKey(String key) {
        return keyMap.getOrDefault(key, -1);
    }

    static {
        putMappings();
        reverseMappings();
    }

    private static void putMappings() {
        keyMap.put("A", GLFW.GLFW_KEY_A);
        keyMap.put("B", GLFW.GLFW_KEY_B);
        keyMap.put("C", GLFW.GLFW_KEY_C);
        keyMap.put("D", GLFW.GLFW_KEY_D);
        keyMap.put("E", GLFW.GLFW_KEY_E);
        keyMap.put("F", GLFW.GLFW_KEY_F);
        keyMap.put("G", GLFW.GLFW_KEY_G);
        keyMap.put("H", GLFW.GLFW_KEY_H);
        keyMap.put("I", GLFW.GLFW_KEY_I);
        keyMap.put("J", GLFW.GLFW_KEY_J);
        keyMap.put("K", GLFW.GLFW_KEY_K);
        keyMap.put("L", GLFW.GLFW_KEY_L);
        keyMap.put("M", GLFW.GLFW_KEY_M);
        keyMap.put("N", GLFW.GLFW_KEY_N);
        keyMap.put("O", GLFW.GLFW_KEY_O);
        keyMap.put("P", GLFW.GLFW_KEY_P);
        keyMap.put("Q", GLFW.GLFW_KEY_Q);
        keyMap.put("R", GLFW.GLFW_KEY_R);
        keyMap.put("S", GLFW.GLFW_KEY_S);
        keyMap.put("T", GLFW.GLFW_KEY_T);
        keyMap.put("U", GLFW.GLFW_KEY_U);
        keyMap.put("V", GLFW.GLFW_KEY_V);
        keyMap.put("W", GLFW.GLFW_KEY_W);
        keyMap.put("X", GLFW.GLFW_KEY_X);
        keyMap.put("Y", GLFW.GLFW_KEY_Y);
        keyMap.put("Z", GLFW.GLFW_KEY_Z);
        keyMap.put("0", GLFW.GLFW_KEY_0);
        keyMap.put("1", GLFW.GLFW_KEY_1);
        keyMap.put("2", GLFW.GLFW_KEY_2);
        keyMap.put("3", GLFW.GLFW_KEY_3);
        keyMap.put("4", GLFW.GLFW_KEY_4);
        keyMap.put("5", GLFW.GLFW_KEY_5);
        keyMap.put("6", GLFW.GLFW_KEY_6);
        keyMap.put("7", GLFW.GLFW_KEY_7);
        keyMap.put("8", GLFW.GLFW_KEY_8);
        keyMap.put("9", GLFW.GLFW_KEY_9);
        keyMap.put("F1", GLFW.GLFW_KEY_F1);
        keyMap.put("F2", GLFW.GLFW_KEY_F2);
        keyMap.put("F3", GLFW.GLFW_KEY_F3);
        keyMap.put("F4", GLFW.GLFW_KEY_F4);
        keyMap.put("F5", GLFW.GLFW_KEY_F5);
        keyMap.put("F6", GLFW.GLFW_KEY_F6);
        keyMap.put("F7", GLFW.GLFW_KEY_F7);
        keyMap.put("F8", GLFW.GLFW_KEY_F8);
        keyMap.put("F9", GLFW.GLFW_KEY_F9);
        keyMap.put("F10", GLFW.GLFW_KEY_F10);
        keyMap.put("F11", GLFW.GLFW_KEY_F11);
        keyMap.put("F12", GLFW.GLFW_KEY_F12);
        keyMap.put("Numpad1", GLFW.GLFW_KEY_KP_1);
        keyMap.put("Numpad2", GLFW.GLFW_KEY_KP_2);
        keyMap.put("Numpad3", GLFW.GLFW_KEY_KP_3);
        keyMap.put("Numpad4", GLFW.GLFW_KEY_KP_4);
        keyMap.put("Numpad5", GLFW.GLFW_KEY_KP_5);
        keyMap.put("Numpad6", GLFW.GLFW_KEY_KP_6);
        keyMap.put("Numpad7", GLFW.GLFW_KEY_KP_7);
        keyMap.put("Numpad8", GLFW.GLFW_KEY_KP_8);
        keyMap.put("Numpad9", GLFW.GLFW_KEY_KP_9);
        keyMap.put("Space", GLFW.GLFW_KEY_SPACE);
        keyMap.put("Enter", GLFW.GLFW_KEY_ENTER);
        keyMap.put("Escape", GLFW.GLFW_KEY_ESCAPE);
        keyMap.put("Home", GLFW.GLFW_KEY_HOME);
        keyMap.put("Insert", GLFW.GLFW_KEY_INSERT);
        keyMap.put("Delete", GLFW.GLFW_KEY_DELETE);
        keyMap.put("End", GLFW.GLFW_KEY_END);
        keyMap.put("Pageup", GLFW.GLFW_KEY_PAGE_UP);
        keyMap.put("Pagedown", GLFW.GLFW_KEY_PAGE_DOWN);
        keyMap.put("Right", GLFW.GLFW_KEY_RIGHT);
        keyMap.put("Left", GLFW.GLFW_KEY_LEFT);
        keyMap.put("Down", GLFW.GLFW_KEY_DOWN);
        keyMap.put("Up", GLFW.GLFW_KEY_UP);
        keyMap.put("Right_shift", GLFW.GLFW_KEY_RIGHT_SHIFT);
        keyMap.put("Left_shift", GLFW.GLFW_KEY_LEFT_SHIFT);
        keyMap.put("Right_control", GLFW.GLFW_KEY_RIGHT_CONTROL);
        keyMap.put("Left_control", GLFW.GLFW_KEY_LEFT_CONTROL);
        keyMap.put("Right_alt", GLFW.GLFW_KEY_RIGHT_ALT);
        keyMap.put("Left_alt", GLFW.GLFW_KEY_LEFT_ALT);
        keyMap.put("Right_super", GLFW.GLFW_KEY_RIGHT_SUPER);
        keyMap.put("Left_super", GLFW.GLFW_KEY_LEFT_SUPER);
        keyMap.put("Menu", GLFW.GLFW_KEY_MENU);
        keyMap.put("Caps_lock", GLFW.GLFW_KEY_CAPS_LOCK);
        keyMap.put("Num_lock", GLFW.GLFW_KEY_NUM_LOCK);
        keyMap.put("Scroll_lock", GLFW.GLFW_KEY_SCROLL_LOCK);
        keyMap.put("Kp_decimal", GLFW.GLFW_KEY_KP_DECIMAL);
        keyMap.put("Kp_divide", GLFW.GLFW_KEY_KP_DIVIDE);
        keyMap.put("Kp_multiply", GLFW.GLFW_KEY_KP_MULTIPLY);
        keyMap.put("Kp_subtract", GLFW.GLFW_KEY_KP_SUBTRACT);
        keyMap.put("Kp_plus", GLFW.GLFW_KEY_KP_ADD);
        keyMap.put("Kp_enter", GLFW.GLFW_KEY_KP_ENTER);
        keyMap.put("Kp_equal", GLFW.GLFW_KEY_KP_EQUAL);
        keyMap.put("'", GLFW.GLFW_KEY_APOSTROPHE);
        keyMap.put("/", GLFW.GLFW_KEY_SLASH);
        keyMap.put("-", GLFW.GLFW_KEY_MINUS);
        keyMap.put("+", GLFW.GLFW_KEY_EQUAL);
        keyMap.put("Back", GLFW.GLFW_KEY_BACKSPACE);
        keyMap.put("Backslash", GLFW.GLFW_KEY_BACKSLASH);
        keyMap.put(".", GLFW.GLFW_KEY_PERIOD);
        keyMap.put("Comma", GLFW.GLFW_KEY_COMMA);
        keyMap.put("Pause", GLFW.GLFW_KEY_PAUSE);
        keyMap.put("`", 96);
    }

    private static void reverseMappings() {
        for (Map.Entry<String, Integer> entry : keyMap.entrySet()) {
            reverseKeyMap.put(entry.getValue(), entry.getKey());
        }
    }
}
