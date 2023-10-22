// This is a generated file. Not intended for manual editing.
package io.stouder.adonis.edge.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import static io.stouder.adonis.edge.psi.EdgeTokenTypes.*;
import static com.intellij.lang.parser.GeneratedParserUtilBase.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import com.intellij.lang.PsiParser;
import com.intellij.lang.LightPsiParser;

@SuppressWarnings({"SimplifiableIfStatement", "UnusedAssignment"})
public class EdgeParser implements PsiParser, LightPsiParser {

  public ASTNode parse(IElementType t, PsiBuilder b) {
    parseLight(t, b);
    return b.getTreeBuilt();
  }

  public void parseLight(IElementType t, PsiBuilder b) {
    boolean r;
    b = adapt_builder_(t, b, this, null);
    Marker m = enter_section_(b, 0, _COLLAPSE_, null);
    r = parse_root_(t, b);
    exit_section_(b, 0, m, t, r, true, TRUE_CONDITION);
  }

  protected boolean parse_root_(IElementType t, PsiBuilder b) {
    return parse_root_(t, b, 0);
  }

  static boolean parse_root_(IElementType t, PsiBuilder b, int l) {
    return edgeComponent(b, l + 1);
  }

  /* ********************************************************** */
  // privateScope
  static boolean edgeComponent(PsiBuilder b, int l) {
    return privateScope(b, l + 1);
  }

  /* ********************************************************** */
  // MUSTACHE_OPEN expression MUSTACHE_CLOSE
  public static boolean interpolation(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "interpolation")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, INTERPOLATION, "<interpolation>");
    r = consumeTokens(b, 1, MUSTACHE_OPEN, EXPRESSION, MUSTACHE_CLOSE);
    p = r; // pin = 1
    exit_section_(b, l, m, r, p, EdgeParser::mustache_recover);
    return r || p;
  }

  /* ********************************************************** */
  // !('{{' | HTML_FRAGMENT)
  static boolean mustache_recover(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "mustache_recover")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !mustache_recover_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // '{{' | HTML_FRAGMENT
  private static boolean mustache_recover_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "mustache_recover_0")) return false;
    boolean r;
    r = consumeToken(b, MUSTACHE_OPEN);
    if (!r) r = consumeToken(b, HTML_FRAGMENT);
    return r;
  }

  /* ********************************************************** */
  // (interpolation|HTML_FRAGMENT)*
  static boolean privateScope(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "privateScope")) return false;
    while (true) {
      int c = current_position_(b);
      if (!privateScope_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "privateScope", c)) break;
    }
    return true;
  }

  // interpolation|HTML_FRAGMENT
  private static boolean privateScope_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "privateScope_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = interpolation(b, l + 1);
    if (!r) r = consumeToken(b, HTML_FRAGMENT);
    exit_section_(b, m, null, r);
    return r;
  }

}
