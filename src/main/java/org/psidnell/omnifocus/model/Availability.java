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
package org.psidnell.omnifocus.model;


public enum Availability {
//    FirstAvailable {
//        //TODO: How?
//    },
    Available
    {
        @Override
        public boolean is (Task t) {
            return !t.isCompleted() && !t.getBlocked();
        }
        @Override
        public boolean is (Project p) {
            return !p.isCompleted() && Status.Active.equals(p.getStatus ());
        }
    },
    Remaining {
        @Override
        public boolean is (Task t) {
            return !t.isCompleted();
        }
        @Override
        public boolean is (Project p) {
            return !p.isCompleted() && !Status.Done.equals(p.getStatus ());
        }
    },
    Completed {
        @Override
        public boolean is (Task t) {
            return t.isCompleted();
        }
        // TODO - Can't eliminate uncompleted projects since they may contain completed tasks
        //@Override
        //public boolean is (Project p) {
        //    // Not sure if I need both clauses
        //    return p.isCompleted() || Status.Done.equals(p.getStatus ());
        //}
    },
    All;
    
    public boolean is (Project p) {
        return true;
    }
    
    public boolean is (Task t) {
        return true;
    }
    
    public boolean is (Context c) {
        return true;
    }
    
    public boolean is (Folder f) {
        return true;
    }
}
