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
package org.psidnell.omnifocus.organise;

import java.util.Collections;
import java.util.Comparator;

import org.psidnell.omnifocus.model.Context;
import org.psidnell.omnifocus.model.Node;
import org.psidnell.omnifocus.model.Project;
import org.psidnell.omnifocus.model.Task;
import org.psidnell.omnifocus.visitor.Traverser;
import org.psidnell.omnifocus.visitor.Visitor;

public class TaskSorter implements Organiser {

    @Override
    public Node organise(Node root) {
        
        Traverser.traverse(new SortingVisitor(), root);
        
        return root;
    }

    private static class SortingVisitor implements Visitor { 
        
        @Override
        public void enter(Project node) {
            Collections.sort(node.getTasks(), new TaskComparator ());
        }
        
        @Override
        public void enter(Context node) {
            Collections.sort(node.getTasks(), new TaskComparator ());
        }
    }
    
    private static class TaskComparator implements Comparator<Task> {

        @Override
        public int compare(Task o1, Task o2) {
            int result = cmp (o1.getCompletionDateAsDate(), o2.getCompletionDateAsDate());
            if (result != 0) {
                return result;
            }
            result = cmp (o1.getDeferDateAsDate(), o2.getCompletionDateAsDate());
            
            return 0;
        }
    }
    
    private static <T extends Comparable<T>> int cmp (T o1, T o2) {
        if (o1 == null && o2 == null) {
            return 0;
        }
        if (o1 == null && o2 != null) {
            return 1;
        }
        if (o1 != null && o2 == null) {
            return -1;
        }
        return o1.compareTo(o2);
    }
}
