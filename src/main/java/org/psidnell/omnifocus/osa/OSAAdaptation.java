package org.psidnell.omnifocus.osa;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface OSAAdaptation {
    String pattern();
}
