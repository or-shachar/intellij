package com.google.idea.sdkcompat.python;

import com.intellij.lang.PsiParser;
import com.intellij.lang.SyntaxTreeBuilder;
import com.intellij.openapi.project.Project;
import com.jetbrains.python.PythonParserDefinition;
import com.jetbrains.python.parsing.ParsingContext;
import com.jetbrains.python.parsing.PyParser;
import com.jetbrains.python.parsing.StatementParsing;
import com.jetbrains.python.psi.LanguageLevel;

/** Compat class for {@link PythonParserDefinition}. #api201 */
public abstract class PythonParserDefinitionCompat extends PythonParserDefinition {

  protected abstract ParsingContext createCustomParsingContext(
      Project project,
      SyntaxTreeBuilderCompat builderCompat,
      LanguageLevel languageLevel,
      StatementParsing.FUTURE futureFlag);

  @Override
  public PsiParser createParser(Project project) {
    return new PyParser() {
      /** #api201: Super method uses new interface SyntaxTreeBuilder in 2020.2 */
      @Override
      protected ParsingContext createParsingContext(
          SyntaxTreeBuilder builder,
          LanguageLevel languageLevel,
          StatementParsing.FUTURE futureFlag) {
        return createCustomParsingContext(
            project, new SyntaxTreeBuilderCompat(builder), languageLevel, futureFlag);
      }
    };
  }
}
