package io.stouder.adonis.edge.psi;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.lang.Language;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import io.stouder.adonis.edge.file.EdgeFileType;
import io.stouder.adonis.edge.EdgeLanguage;
import org.jetbrains.annotations.NotNull;

public class EdgePsiFile extends PsiFileBase {

    public EdgePsiFile(@NotNull FileViewProvider viewProvider) {
        super(viewProvider, EdgeLanguage.INSTANCE);
    }

    public EdgePsiFile(@NotNull FileViewProvider viewProvider, @NotNull Language language) {
        super(viewProvider, language);
    }

    @Override
    public @NotNull FileType getFileType() {
        return EdgeFileType.INSTANCE;
    }

    @Override
    public String toString() {
        return "EdgeFile:" + getName();
    }
}
