// This is a generated file. Not intended for manual editing.
package io.stouder.adonis.edge.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import io.stouder.adonis.edge.psi.impl.*;

public interface EdgeTokenTypes {

  IElementType INTERPOLATION = new EdgeElementType("INTERPOLATION");

  IElementType EXPRESSION = new EdgeTokenType("expression");
  IElementType HTML_FRAGMENT = new EdgeTokenType("HTML_FRAGMENT");
  IElementType MUSTACHE = new EdgeTokenType("<mustache>");
  IElementType MUSTACHE_CLOSE = new EdgeTokenType("}}");
  IElementType MUSTACHE_OPEN = new EdgeTokenType("{{");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
      if (type == INTERPOLATION) {
        return new EdgeInterpolationImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
