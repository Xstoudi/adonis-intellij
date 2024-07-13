package io.stouder.adonis.edge.parsing

import com.intellij.lexer.FlexAdapter

class EdgeRawLexer : FlexAdapter(_EdgeLexer(null))
