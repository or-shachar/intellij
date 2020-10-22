package com.google.idea.sdkcompat.python;

import com.intellij.lang.SyntaxTreeBuilder;

/**
 * Compat class to support that constructor of ParsingContext uses new interface SyntaxTreeBuilder
 * in 2020.2. #api201
 */
public class SyntaxTreeBuilderCompat {

  private final SyntaxTreeBuilder builder;

  public SyntaxTreeBuilderCompat(SyntaxTreeBuilder builder) {
    this.builder = builder;
  }

  public SyntaxTreeBuilder getBuilder() {
    return builder;
  }
}
