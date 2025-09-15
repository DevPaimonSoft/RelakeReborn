package su.relake.compiler.sdk.annotations;

import su.relake.compiler.sdk.enums.VMProtectType;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface VMProtect {
    VMProtectType type();
}
