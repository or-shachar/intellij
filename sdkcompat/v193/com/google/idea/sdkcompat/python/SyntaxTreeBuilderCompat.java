package com.google.idea.sdkcompat.python;

import com.intellij.lang.PsiBuilder;

/**
 * Compat class to support that constructor of ParsingContext uses new interface SyntaxTreeBuilder
 * in 2020.2. #api201
 */
public class SyntaxTreeBuilderCompat {

  private final PsiBuilder builder;

  public SyntaxTreeBuilderCompat(PsiBuilder builder) {
    this.builder = builder;
  }

  public PsiBuilder getBuilder() {
    return builder;
  }
}
