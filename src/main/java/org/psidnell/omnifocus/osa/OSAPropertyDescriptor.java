package org.psidnell.omnifocus.osa;

public class OSAPropertyDescriptor {

    private String getter;
    private String adaptation;
    private String nullGetter;
    
    public OSAPropertyDescriptor (String getter, String nullGetter, String adaptation) {
        this.getter = getter;
        this.nullGetter = nullGetter;
        this.adaptation = adaptation;
    }

    public void setGetter(String getter) {
        this.getter = getter;
    }
    
    public String getAdaptedGetter () {        
        if (getter == null) {
            return nullGetter;
        }
        else {
            return String.format(adaptation, getter);
        }
    }
}
