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
package org.psidnell.omnifocus.model;

import org.psidnell.omnifocus.ConfigParams;

/**
 * @author psidnell
 *
 *         Root node interface.
 */
public interface Node {

    String getName();

    void setName(String name);

    String getId();

    void setId(String id);

    int getRank();

    void setRank(int rank);

    String getType();

    boolean isRoot();

    boolean isMarked();

    void setMarked(boolean marked);

    void cascadeMarked();

    void setConfigParams(ConfigParams config);

    java.util.Date getDateAdded();

    void setDateAdded(java.util.Date date);

    org.psidnell.omnifocus.expr.Date getAdded();

    java.util.Date getDateModified();

    void setDateModified(java.util.Date date);

    org.psidnell.omnifocus.expr.Date getModified();
}