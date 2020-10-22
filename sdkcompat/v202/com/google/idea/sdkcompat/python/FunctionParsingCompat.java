package com.google.idea.sdkcompat.python;

import com.intellij.lang.SyntaxTreeBuilder;
import com.intellij.lang.SyntaxTreeBuilder.Marker;
import com.intellij.psi.tree.IElementType;
import com.jetbrains.python.parsing.FunctionParsing;
import com.jetbrains.python.parsing.ParsingContext;

/** #api201: Compat class for {@link FunctionParsing}. */
public class FunctionParsingCompat extends FunctionParsing {

  public FunctionParsingCompat(ParsingContext context) {
    super(context);
  }

  protected MarkerCompat getMarker() {
    return new MarkerCompat(myBuilder.mark());
  }

  /** #api201: Compat class for marker which is represented by a new interface in 2020.2. */
  protected static class MarkerCompat {
    private final SyntaxTreeBuilder.Marker marker;

    private MarkerCompat(Marker marker) {
      this.marker = marker;
    }

    public void done(IElementType type) {
      marker.done(type);
    }
  }
}
