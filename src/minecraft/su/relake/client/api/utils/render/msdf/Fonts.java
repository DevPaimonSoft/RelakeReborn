package su.relake.client.api.utils.render.msdf;

import com.google.common.base.Suppliers;


public class Fonts {
    public static final MsdfFont COMFORTAA_BOLD = Suppliers.memoize(() -> MsdfFont.builder().atlas("comfortaa_bold").data("comfortaa_bold").build()).get();
    public static final MsdfFont COMFORTAA_LIGHT = Suppliers.memoize(() -> MsdfFont.builder().atlas("comfortaa_light").data("comfortaa_light").build()).get();
    public static final MsdfFont COMFORTAA_MEDIUM = Suppliers.memoize(() -> MsdfFont.builder().atlas("comfortaa_medium").data("comfortaa_medium").build()).get();
    public static final MsdfFont COMFORTAA_REGULAR = Suppliers.memoize(() -> MsdfFont.builder().atlas("comfortaa_regular").data("comfortaa_regular").build()).get();
    public static final MsdfFont COMFORTAA_SEMIBOLD = Suppliers.memoize(() -> MsdfFont.builder().atlas("comfortaa_semibold").data("comfortaa_semibold").build()).get();
    public static final MsdfFont ICONS = Suppliers.memoize(() -> MsdfFont.builder().atlas("icons").data("icons").build()).get();
    public static final MsdfFont WT = Suppliers.memoize(() -> MsdfFont.builder().atlas("wt").data("wt").build()).get();
    public static final MsdfFont ICONS_WEXSIDE = Suppliers.memoize(() -> MsdfFont.builder().atlas("iconswex").data("iconswex").build()).get();
    public static final MsdfFont INTER_DISPLAY_MEDIUM = Suppliers.memoize(() -> MsdfFont.builder().atlas("inter_display_medium").data("inter_display_medium").build()).get();

}
