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
package org.psidnell.omnifocus.osa;

public class OSAPropertyDescriptor {

    private String accessor;
    private String adaptation;
    
    public OSAPropertyDescriptor (String accessor, String accessorAdaptation) {
        this.accessor = accessor;
        this.adaptation = accessorAdaptation;
    }

    public void setGetter(String getter) {
        this.accessor = getter;
    }
    
    public String getAdaptedGetter () {        
        if (isConstant()) {
            return accessor;
        }
        else {
            return String.format(adaptation, accessor);
        }
    }

    public String getGetter() {
        return accessor;
    }
    
    boolean isConstant() {
        return accessor.equals ("true") ||
               accessor.equals("false") ||
               accessor.startsWith("'") ||
               accessor.startsWith("\"") ||
               accessor.startsWith("[") ||
               accessor.startsWith("{") ||
               (accessor.length() > 0 && Character.isDigit(accessor.charAt(0)));
    }
}
