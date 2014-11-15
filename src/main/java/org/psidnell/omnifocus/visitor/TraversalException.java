package org.psidnell.omnifocus.visitor;

public class TraversalException extends RuntimeException {
    
    public TraversalException (Exception e) {
        super (e);
    }
}
