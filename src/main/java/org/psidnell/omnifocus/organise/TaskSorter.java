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
