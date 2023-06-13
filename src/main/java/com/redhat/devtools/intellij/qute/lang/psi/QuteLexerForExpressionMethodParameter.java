/*******************************************************************************
 * Copyright (c) 2023 Red Hat Inc. and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0, or the Apache License, Version 2.0
 * which is available at https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0 OR Apache-2.0
 *
 * Contributors:
 *     Red Hat Inc. - initial API and implementation
 *******************************************************************************/
package com.redhat.devtools.intellij.qute.lang.psi;

import com.redhat.qute.parser.expression.scanner.ExpressionScanner;

/**
 * Qute lexer to parse a given Qute method parameter content.
 *
 * <code>
 *      {foo.bar(|'foo'|,true)}
 * </code>
 */
public class QuteLexerForExpressionMethodParameter extends QuteLexerForExpressionContent {

    QuteLexerForExpressionMethodParameter(String text, int startExpressionOffset, int endExpressionOffset) {
        super(text);
        initialize(ExpressionScanner.createScanner(text, false, startExpressionOffset, endExpressionOffset));
    }
}
