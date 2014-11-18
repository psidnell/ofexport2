/*
Copyright 2014 Paul Sidnell

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package org.psidnell.omnifocus.expr;

import java.util.BitSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.DefaultErrorStrategy;
import org.antlr.v4.runtime.DiagnosticErrorListener;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.misc.Nullable;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.psidnell.omnifocus.expr.OFExprParser.ExprContext;
import org.psidnell.omnifocus.expr.OFExprParser.NodeTypeContext;
import org.psidnell.omnifocus.expr.OFExprParser.PropertyContext;
import org.psidnell.omnifocus.expr.OFExprParser.PropertyNameContext;
import org.psidnell.omnifocus.expr.OFExprParser.PropertyValueContext;

public class ExprParser {

    public static void parse (String input) {
        OFExprLexer lexer = new OFExprLexer(new ANTLRInputStream(input));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        
        OFExprParser parser = new OFExprParser(tokens);
        parser.removeErrorListeners();
        parser.setErrorHandler(new CustomErrorStrategy());
        parser.addErrorListener(new DiagnosticErrorListener());
        parser.addErrorListener(new CustomErrorListener());
        
        ExprContext OFExprSentenceContext = parser.expr();
        ParseTreeWalker walker = new ParseTreeWalker();
        Listener listener = new Listener();
        walker.walk(listener, OFExprSentenceContext);
    }
    
    static class Listener extends OFExprBaseListener {
        private String propertyName;
        private String propertyValue;
       
    @Override
        public void exitNodeType(NodeTypeContext ctx) {
            String nodeType = ctx.getText();
           
        }
       
       @Override
        public void exitPropertyName(PropertyNameContext ctx) {
            propertyName = ctx.getText();
            System.out.println("propName=" + propertyName);
        }
       
       @Override
        public void exitPropertyValue(PropertyValueContext ctx) {
            propertyValue = ctx.getText().substring(1);
            System.out.println("propValue=" + propertyValue);
        }
       
       @Override
        public void exitProperty(PropertyContext ctx) {
            System.out.println("overriding " + propertyName + ":"+ propertyValue);
        }
    }
    
    private static class CustomErrorStrategy extends DefaultErrorStrategy {
        @Override
        public void recover(Parser recognizer, RecognitionException e) {
            super.recover(recognizer, e);
            throw e;
        }
    }
    
    private static class CustomErrorListener implements ANTLRErrorListener {

        @Override
        public void reportAmbiguity(@NotNull Parser arg0, @NotNull DFA arg1, int arg2, int arg3, boolean arg4, @Nullable BitSet arg5, @NotNull ATNConfigSet arg6) {
        }

        @Override
        public void reportAttemptingFullContext(@NotNull Parser arg0, @NotNull DFA arg1, int arg2, int arg3, @Nullable BitSet arg4, @NotNull ATNConfigSet arg5) {
        }

        @Override
        public void reportContextSensitivity(@NotNull Parser arg0, @NotNull DFA arg1, int arg2, int arg3, int arg4, @NotNull ATNConfigSet arg5) {
        }

        @Override
        public void syntaxError(@NotNull Recognizer<?, ?> recognizer, @Nullable Object offendingSymbol, int line, int charPositionInLine, @NotNull String msg,
                @Nullable RecognitionException e) {
            if (offendingSymbol instanceof Token) {
                System.err.println(msg);
                //Map<String, Integer> tokenTypeMap = getTokenTypeMap();
                //for (Entry<String, Integer> entry : tokenTypeMap.entrySet()) {
                //    if (token.getType() == entry.getValue().intValue()) {
                //        LOGGER.error("Mismatch on defined token " + entry.getValue());
                //    }
               //}
            }
            throw new IllegalStateException("error at line:" + line + " position:" + charPositionInLine + " " + msg, e);
        }
    }
}
