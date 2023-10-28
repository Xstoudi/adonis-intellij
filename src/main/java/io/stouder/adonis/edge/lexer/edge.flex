package io.stouder.adonis.edge.lexer;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.xml.XmlTokenType;
import io.stouder.adonis.edge.psi.EdgeTokenTypes;
import com.intellij.lexer.FlexLexer;

// suppress various warnings/inspections for the generated class
// @SuppressWarnings ({"FieldCanBeLocal", "UnusedDeclaration", "UnusedAssignment", "AccessStaticViaInstance", "MismatchedReadAndWriteOfArray", "WeakerAccess", "SameParameterValue", "CanBeFinal", "SameReturnValue", "RedundantThrows", "ConstantConditions"})
%%

%unicode

%{
  public _EdgeLexer() {
    this((java.io.Reader)null);
  }
%}



%class _EdgeLexer
%implements FlexLexer
%public
%final
%function advance
%type IElementType
%eof{  return;
%eof}

%state DOC_TYPE
%state COMMENT
%state START_TAG_NAME
%state END_TAG_NAME
%state BEFORE_TAG_ATTRIBUTES
%state TAG_ATTRIBUTES
%state ATTRIBUTE_VALUE_START
%state ATTRIBUTE_VALUE_DQ
%state ATTRIBUTE_VALUE_SQ
%state PROCESSING_INSTRUCTION
%state TAG_CHARACTERS
%state C_COMMENT_START
%state C_COMMENT_END
%state MUSTACHE_OPEN
%state MUSTACHE
%state SAFE_MUSTACHE_OPEN
%state SAFE_MUSTACHE
%state ESCAPED_MUSTACHE_OPEN
%state ESCAPED_MUSTACHE
%state ESCAPED_SAFE_MUSTACHE_OPEN
%state ESCAPED_SAFE_MUSTACHE
%state MUSTACHE_COMMENT_OPEN
%state MUSTACHE_COMMENT
/* IMPORTANT! number of states should not exceed 16. See JspHighlightingLexer. */

ALPHA=[:letter:]
DIGIT=[0-9]
WHITE_SPACE_CHARS=[ \n\r\t\f\u2028\u2029\u0085]+

TAG_NAME=({ALPHA}|"_"|":")({ALPHA}|{DIGIT}|"_"|":"|"."|"-")*
/* see http://www.w3.org/TR/html5/syntax.html#syntax-attribute-name */
ATTRIBUTE_NAME=([^ \n\r\t\f\"\'<>/=])+

DTD_REF= "\"" [^\"]* "\"" | "'" [^']* "'"
DOCTYPE= "<!" (D|d)(O|o)(C|c)(T|t)(Y|y)(P|p)(E|e)
HTML= (H|h)(T|t)(M|m)(L|l)
PUBLIC= (P|p)(U|u)(B|b)(L|l)(I|i)(C|c)
END_COMMENT="-->"

CONDITIONAL_COMMENT_CONDITION=({ALPHA})({ALPHA}|{WHITE_SPACE_CHARS}|{DIGIT}|"."|"("|")"|"|"|"!"|"&")*

%%

<YYINITIAL> "<?" { yybegin(PROCESSING_INSTRUCTION); return XmlTokenType.XML_PI_START; }
<PROCESSING_INSTRUCTION> "?"? ">" { yybegin(YYINITIAL); return XmlTokenType.XML_PI_END; }
<PROCESSING_INSTRUCTION> ([^\?\>] | (\?[^\>]))* { return XmlTokenType.XML_PI_TARGET; }

<YYINITIAL> {DOCTYPE} { yybegin(DOC_TYPE); return XmlTokenType.XML_DOCTYPE_START; }
<DOC_TYPE> {HTML} { return XmlTokenType.XML_NAME; }
<DOC_TYPE> {PUBLIC} { return XmlTokenType.XML_DOCTYPE_PUBLIC; }
<DOC_TYPE> {DTD_REF} { return XmlTokenType.XML_ATTRIBUTE_VALUE_TOKEN;}
<DOC_TYPE> ">" { yybegin(YYINITIAL); return XmlTokenType.XML_DOCTYPE_END; }
<YYINITIAL> {WHITE_SPACE_CHARS} { return XmlTokenType.XML_REAL_WHITE_SPACE; }
<DOC_TYPE,TAG_ATTRIBUTES,ATTRIBUTE_VALUE_START,PROCESSING_INSTRUCTION, START_TAG_NAME, END_TAG_NAME, TAG_CHARACTERS> {WHITE_SPACE_CHARS} { return XmlTokenType.XML_WHITE_SPACE; }
<YYINITIAL> "<" {TAG_NAME} { yybegin(START_TAG_NAME); yypushback(yylength()); }
<START_TAG_NAME, TAG_CHARACTERS> "<" { return XmlTokenType.XML_START_TAG_START; }

<YYINITIAL> "</" {TAG_NAME} { yybegin(END_TAG_NAME); yypushback(yylength()); }
<YYINITIAL, END_TAG_NAME> "</" { return XmlTokenType.XML_END_TAG_START; }

<YYINITIAL> "<!--" { yybegin(COMMENT); return XmlTokenType.XML_COMMENT_START; }
<COMMENT> "[" { yybegin(C_COMMENT_START); return XmlTokenType.XML_CONDITIONAL_COMMENT_START; }
<COMMENT> "<![" { yybegin(C_COMMENT_END); return XmlTokenType.XML_CONDITIONAL_COMMENT_END_START; }
<COMMENT> {END_COMMENT} | "<!-->" { yybegin(YYINITIAL); return XmlTokenType.XML_COMMENT_END; }
<COMMENT> "<!--" { return XmlTokenType.XML_BAD_CHARACTER; }
<COMMENT> "<!--->" | "--!>" { yybegin(YYINITIAL); return XmlTokenType.XML_BAD_CHARACTER; }
<COMMENT> ">" {
  // according to HTML spec (http://www.w3.org/html/wg/drafts/html/master/syntax.html#comments)
  // comments should start with <!-- and end with -->. The comment <!--> is not valid, but should terminate
  // comment token. Please note that it's not true for XML (http://www.w3.org/TR/REC-xml/#sec-comments)
  int loc = getTokenStart();
  char prev = zzBuffer.charAt(loc - 1);
  char prevPrev = zzBuffer.charAt(loc - 2);
  if (prev == '-' && prevPrev == '-') {
    yybegin(YYINITIAL); return XmlTokenType.XML_BAD_CHARACTER;
  }
  return XmlTokenType.XML_COMMENT_CHARACTERS;
}
<COMMENT> [^] { return XmlTokenType.XML_COMMENT_CHARACTERS; }

<C_COMMENT_START,C_COMMENT_END> {CONDITIONAL_COMMENT_CONDITION} { return XmlTokenType.XML_COMMENT_CHARACTERS; }
<C_COMMENT_START> [^] { yybegin(COMMENT); return XmlTokenType.XML_COMMENT_CHARACTERS; }
<C_COMMENT_START> "]>" { yybegin(COMMENT); return XmlTokenType.XML_CONDITIONAL_COMMENT_START_END; }
<C_COMMENT_START,C_COMMENT_END> {END_COMMENT} { yybegin(YYINITIAL); return XmlTokenType.XML_COMMENT_END; }
<C_COMMENT_END> "]" { yybegin(COMMENT); return XmlTokenType.XML_CONDITIONAL_COMMENT_END; }
<C_COMMENT_END> [^] { yybegin(COMMENT); return XmlTokenType.XML_COMMENT_CHARACTERS; }

<YYINITIAL> \\\$ {
  return XmlTokenType.XML_DATA_CHARACTERS;
}

// EDGE START
<YYINITIAL> "@{{{" {
    yybegin(ESCAPED_SAFE_MUSTACHE_OPEN);
    return EdgeTokenTypes.ESCAPED_SAFE_MUSTACHE_OPEN;
}

<YYINITIAL> "@{{" {
    yybegin(ESCAPED_MUSTACHE_OPEN);
    return EdgeTokenTypes.ESCAPED_MUSTACHE_OPEN;
}

<YYINITIAL> "{{{" {
    yybegin(SAFE_MUSTACHE_OPEN);
    return EdgeTokenTypes.SAFE_MUSTACHE_OPEN;
}

<YYINITIAL> "{{--" {
    yybegin(MUSTACHE_COMMENT_OPEN);
    return EdgeTokenTypes.MUSTACHE_COMMENT_OPEN;
}

<YYINITIAL> "{{" {
    yybegin(MUSTACHE_OPEN);
    return EdgeTokenTypes.MUSTACHE_OPEN;
}

<MUSTACHE_OPEN> {
    [^] {
        yybegin(MUSTACHE);
        yypushback(yylength());
    }
}

<SAFE_MUSTACHE_OPEN> {
    [^] {
        yybegin(SAFE_MUSTACHE);
        yypushback(yylength());
    }
}

<ESCAPED_MUSTACHE_OPEN> {
    [^] {
        yybegin(ESCAPED_MUSTACHE);
        yypushback(yylength());
    }
}

<ESCAPED_SAFE_MUSTACHE_OPEN> {
    [^] {
        yybegin(ESCAPED_SAFE_MUSTACHE);
        yypushback(yylength());
    }
}

<MUSTACHE_COMMENT_OPEN> {
    [^] {
        yybegin(MUSTACHE_COMMENT);
        yypushback(yylength());
    }
}

// EDGE END

<START_TAG_NAME, END_TAG_NAME> {TAG_NAME} { yybegin(BEFORE_TAG_ATTRIBUTES); return XmlTokenType.XML_NAME; }

<BEFORE_TAG_ATTRIBUTES, TAG_ATTRIBUTES, TAG_CHARACTERS> ">" { yybegin(YYINITIAL); return XmlTokenType.XML_TAG_END; }
<BEFORE_TAG_ATTRIBUTES, TAG_ATTRIBUTES, TAG_CHARACTERS> "/>" { yybegin(YYINITIAL); return XmlTokenType.XML_EMPTY_ELEMENT_END; }
<BEFORE_TAG_ATTRIBUTES> {WHITE_SPACE_CHARS} { yybegin(TAG_ATTRIBUTES); return XmlTokenType.XML_WHITE_SPACE;}
<TAG_ATTRIBUTES> {ATTRIBUTE_NAME} { return XmlTokenType.XML_NAME; }
<TAG_ATTRIBUTES> "=" { yybegin(ATTRIBUTE_VALUE_START); return XmlTokenType.XML_EQ; }
<BEFORE_TAG_ATTRIBUTES, TAG_ATTRIBUTES, START_TAG_NAME, END_TAG_NAME> [^] { yybegin(YYINITIAL); yypushback(1); break; }

<TAG_CHARACTERS> [^] { return XmlTokenType.XML_TAG_CHARACTERS; }

<ATTRIBUTE_VALUE_START> ">" { yybegin(YYINITIAL); return XmlTokenType.XML_TAG_END; }
<ATTRIBUTE_VALUE_START> "/>" { yybegin(YYINITIAL); return XmlTokenType.XML_EMPTY_ELEMENT_END; }

<ATTRIBUTE_VALUE_START> [^ \n\r\t\f'\"\>]([^ \n\r\t\f\>]|(\/[^\>]))* { yybegin(TAG_ATTRIBUTES); return XmlTokenType.XML_ATTRIBUTE_VALUE_TOKEN; }
<ATTRIBUTE_VALUE_START> "\"" { yybegin(ATTRIBUTE_VALUE_DQ); return XmlTokenType.XML_ATTRIBUTE_VALUE_START_DELIMITER; }
<ATTRIBUTE_VALUE_START> "'" { yybegin(ATTRIBUTE_VALUE_SQ); return XmlTokenType.XML_ATTRIBUTE_VALUE_START_DELIMITER; }

<ATTRIBUTE_VALUE_DQ> {
  "\"" { yybegin(TAG_ATTRIBUTES); return XmlTokenType.XML_ATTRIBUTE_VALUE_END_DELIMITER; }
  \\\$ { return XmlTokenType.XML_ATTRIBUTE_VALUE_TOKEN; }
  [^] { return XmlTokenType.XML_ATTRIBUTE_VALUE_TOKEN;}
}

<ATTRIBUTE_VALUE_SQ> {
  "'" { yybegin(TAG_ATTRIBUTES); return XmlTokenType.XML_ATTRIBUTE_VALUE_END_DELIMITER; }
  \\\$ { return XmlTokenType.XML_ATTRIBUTE_VALUE_TOKEN; }
  [^] { return XmlTokenType.XML_ATTRIBUTE_VALUE_TOKEN;}
}

// EDGE START
<MUSTACHE> {
    "}}" {
        yybegin(YYINITIAL);
        return EdgeTokenTypes.MUSTACHE_CLOSE;
    }
}
<SAFE_MUSTACHE> {
    "}}}" {
        yybegin(YYINITIAL);
        return EdgeTokenTypes.SAFE_MUSTACHE_CLOSE;
    }
}
<ESCAPED_MUSTACHE> {
    "}}" {
        yybegin(YYINITIAL);
        return EdgeTokenTypes.ESCAPED_MUSTACHE_CLOSE;
    }
}
<ESCAPED_SAFE_MUSTACHE> {
    "}}}" {
        yybegin(YYINITIAL);
        return EdgeTokenTypes.ESCAPED_SAFE_MUSTACHE_CLOSE;
    }
}
<MUSTACHE_COMMENT> {
    "--}}" {
        yybegin(YYINITIAL);
        return EdgeTokenTypes.MUSTACHE_COMMENT_CLOSE;
    }
}
<MUSTACHE, SAFE_MUSTACHE, ESCAPED_MUSTACHE, ESCAPED_SAFE_MUSTACHE, MUSTACHE_COMMENT> {
    "<" {TAG_NAME} {
        yybegin(START_TAG_NAME);
        yypushback(yylength());
      }
    "</" {TAG_NAME} {
        yybegin(END_TAG_NAME);
        yypushback(yylength());
    }
    [^] {
        if(yystate() == MUSTACHE) return EdgeTokenTypes.MUSTACHE;
        if(yystate() == SAFE_MUSTACHE) return EdgeTokenTypes.MUSTACHE;
        if(yystate() == ESCAPED_MUSTACHE) return EdgeTokenTypes.MUSTACHE;
        if(yystate() == ESCAPED_SAFE_MUSTACHE) return EdgeTokenTypes.MUSTACHE;
        if(yystate() == MUSTACHE_COMMENT) return EdgeTokenTypes.MUSTACHE_COMMENT;
    }
}
// EDGE END

"&lt;" |
"&gt;" |
"&apos;" |
"&quot;" |
"&nbsp;" |
"&amp;" |
"&#"{DIGIT}+";" |
"&#"[xX]({DIGIT}|[a-fA-F])+";" { return XmlTokenType.XML_CHAR_ENTITY_REF; }
"&"{TAG_NAME}";" { return XmlTokenType.XML_ENTITY_REF_TOKEN; }

<YYINITIAL> ([^<&\$# \n\r\t\f]|(\\\$)|(\\#))* { return XmlTokenType.XML_DATA_CHARACTERS; }
<YYINITIAL> [^] { return XmlTokenType.XML_DATA_CHARACTERS; }
[^] { return XmlTokenType.XML_BAD_CHARACTER; }



