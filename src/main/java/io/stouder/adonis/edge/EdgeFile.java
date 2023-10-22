package io.stouder.adonis.edge;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.lang.Language;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import io.stouder.adonis.edge.EdgeFileType;
import io.stouder.adonis.edge.EdgeLanguage;
import org.jetbrains.annotations.NotNull;

public class EdgeFile extends PsiFileBase {
    public EdgeFile(@NotNull FileViewProvider viewProvider) {
        super(viewProvider, EdgeLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public FileType getFileType() {
        return EdgeFileType.INSTANCE;
    }
}
