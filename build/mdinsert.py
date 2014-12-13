#!/usr/bin/python

# mdinsert fileA.md fileB.md start end
# insert fileA.md into fileB.md by replacing the text between start and end

import sys
import re

if __name__ == "__main__":
    fFrom = sys.argv[1]
    fTo = sys.argv[2]
    start = sys.argv[3]
    end = sys.argv[4]
    
    with open(fFrom) as f:
        textFrom = f.readlines()
        
    with open(fTo) as f:
        textTo = f.readlines()
        
    found_start = False
    for line in textTo:
        if not found_start:
            if re.search(start, line):
                found_start = True
                for from_line in textFrom:
                    print from_line,
            else:
                print line,
        else:
            if re.search(end, line):
                print line,
                found_start=False