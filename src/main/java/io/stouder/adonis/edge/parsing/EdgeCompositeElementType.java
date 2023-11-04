package io.stouder.adonis.edge.parsing;

import com.intellij.lang.Language;
import com.intellij.psi.tree.IElementType;
import io.stouder.adonis.edge.EdgeLanguage;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EdgeCompositeElementType extends IElementType {
    public EdgeCompositeElementType(@NonNls @NotNull String debugName) {
        super(debugName, EdgeLanguage.INSTANCE);
    }
}
