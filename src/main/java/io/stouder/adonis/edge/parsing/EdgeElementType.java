package io.stouder.adonis.edge.parsing;

import com.intellij.psi.tree.IElementType;
import io.stouder.adonis.edge.EdgeLanguage;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class EdgeElementType extends IElementType {
    public EdgeElementType(@NonNls @NotNull String debugName) {
        super(debugName, EdgeLanguage.INSTANCE);
    }

    @Override
    public String toString() {
        return "[Edge] " + super.toString();
    }
}
