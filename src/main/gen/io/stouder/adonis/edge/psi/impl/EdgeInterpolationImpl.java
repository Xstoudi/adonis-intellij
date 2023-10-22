// This is a generated file. Not intended for manual editing.
package io.stouder.adonis.edge.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static io.stouder.adonis.edge.psi.EdgeTokenTypes.*;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import io.stouder.adonis.edge.psi.*;

public class EdgeInterpolationImpl extends ASTWrapperPsiElement implements EdgeInterpolation {

  public EdgeInterpolationImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull EdgeVisitor visitor) {
    visitor.visitInterpolation(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof EdgeVisitor) accept((EdgeVisitor)visitor);
    else super.accept(visitor);
  }

}
