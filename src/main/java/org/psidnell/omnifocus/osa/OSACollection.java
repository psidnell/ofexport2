package org.psidnell.omnifocus.osa;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.psidnell.omnifocus.model.Node;


@Retention(RetentionPolicy.RUNTIME)
public @interface OSACollection {
    public Class<? extends Node> type();
            
}
