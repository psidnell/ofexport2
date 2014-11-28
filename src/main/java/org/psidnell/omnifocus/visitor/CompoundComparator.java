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
package org.psidnell.omnifocus.visitor;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.psidnell.omnifocus.model.Node;

public class CompoundComparator<T extends Node> implements Comparator<T> {

    private final Comparator<T> RANK_COMPARATOR = (a, b) -> cmp(a.getRank(), b.getRank());

    private List<Comparator<T>> comparators = new LinkedList<>();

    @Override
    public int compare(T o1, T o2) {
        // Run through all the comparators in order
        // stopping when we find one with an opinion
        for (Comparator<T> cmp : comparators) {
            int result = cmp.compare(o1, o2);
            if (result != 0) {
                return result;
            }
        }

        // Tie breaker: finally if we get to the end just use rank
        return RANK_COMPARATOR.compare(o1, o2);
    }

    public void add(Comparator<T> cmp) {
        comparators.add(cmp);
    }

    private static int cmp(int i1, int i2) {
        if (i1 < i2) {
            return -1;
        } else if (i1 > i2) {
            return 1;
        } else {
            return 0;
        }
    }
}