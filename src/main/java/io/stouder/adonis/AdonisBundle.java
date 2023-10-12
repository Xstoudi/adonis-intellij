package io.stouder.adonis;

import com.intellij.DynamicBundle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.PropertyKey;

import java.util.Arrays;

public class AdonisBundle extends DynamicBundle {
    public static final AdonisBundle INSTANCE = new AdonisBundle();

    private AdonisBundle() {
        super("messages.AdonisBundle");
    }

    public static String message(@PropertyKey(resourceBundle = "messages.AdonisBundle") @NotNull String key, @NotNull Object... params) {
        return INSTANCE.getMessage(key, Arrays.copyOf(params, params.length));
    }
}
