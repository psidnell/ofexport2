/*
 * Copyright 2015 Paul Sidnell
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.psidnell.omnifocus.expr;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author psidnell
 *
 * Identifies an accessor (getter) as an attribute that can be used for expressions.
 * Only used to provide help information.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ExprAttribute {
    String help();
    String[] args() default {};
}
