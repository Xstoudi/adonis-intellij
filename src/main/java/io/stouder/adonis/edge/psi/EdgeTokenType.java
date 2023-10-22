package io.stouder.adonis.edge.psi;

import com.intellij.lang.Language;
import com.intellij.psi.tree.IElementType;
import io.stouder.adonis.edge.EdgeLanguage;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EdgeTokenType extends IElementType {
    public EdgeTokenType(@NonNls @NotNull String debugName) {
        super(debugName, EdgeLanguage.INSTANCE);
    }

    @Override
    public String toString() {
        return "EdgeTokenType." + super.toString();
    }
}
