package io.stouder.adonis.edge.psi;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
public class EdgeMustacheElementType extends EdgeElementType {
    private final String matchingChars;

    public EdgeMustacheElementType(@NotNull String debugName, @NotNull String matchingChars) {
        super(debugName);
        this.matchingChars = matchingChars;
    }
}
