// We base our lexer directly on the official handlebars.l lexer definition,
// making some modifications to account for Jison/JFlex syntax and functionality differences
//
// Revision ported: https://github.com/wycats/handlebars.js/blob/408192ba9f262bb82be88091ab3ec3c16dc02c6d/src/handlebars.l

package io.stouder.adonis.edge.parsing;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.containers.Stack;
import io.stouder.adonis.edge.parsing.EdgeTokenTypes;

// suppress various warnings/inspections for the generated class
@SuppressWarnings ({"FieldCanBeLocal", "UnusedDeclaration", "UnusedAssignment", "AccessStaticViaInstance", "MismatchedReadAndWriteOfArray", "WeakerAccess", "SameParameterValue", "CanBeFinal", "SameReturnValue", "RedundantThrows", "ConstantConditions"})
%%

%class _EdgeLexer
%implements FlexLexer
%final
%unicode
%function advance
%type IElementType

%{
    private Stack<Integer> stack = new Stack<>();

    public void yypushState(int newState) {
      stack.push(yystate());
      yybegin(newState);
    }

    public void yypopState() {
      yybegin(stack.pop());
    }
%}

%state MUSTACHE
%state SAFE_MUSTACHE

%%

<YYINITIAL> {
    "{{" {
        yypushState(MUSTACHE);
        return EdgeTokenTypes.MUSTACHE_OPEN;
    }
    "{{{" {
        yypushState(SAFE_MUSTACHE);
        return EdgeTokenTypes.SAFE_MUSTACHE_OPEN;
    }
    [^] { return EdgeTokenTypes.CONTENT; }
}

<MUSTACHE> {
    "}}" { yypopState(); return EdgeTokenTypes.MUSTACHE_CLOSE; }
    [^] {
        return EdgeTokenTypes.MUSTACHE_CONTENT;
    }
}

<SAFE_MUSTACHE> {
    "}}}" { yypopState(); return EdgeTokenTypes.SAFE_MUSTACHE_CLOSE; }
    [^] {
        return EdgeTokenTypes.MUSTACHE_CONTENT;
    }
}

