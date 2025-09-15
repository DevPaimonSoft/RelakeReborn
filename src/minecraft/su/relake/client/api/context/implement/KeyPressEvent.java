package su.relake.client.api.context.implement;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import su.relake.client.api.context.EventBase;

@Getter
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class KeyPressEvent extends EventBase {
    int id;
}