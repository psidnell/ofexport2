#!/usr/bin/python

#
# Generate a Table Of Contents from the md files passed on the command line.
#

import sys

def gen_entry (mdFile, line):
    count = 0
    for c in line:
        if c == '#':
            count = count + 1
        else:
            break
        
    title = line[count:].strip()
    
    while title.endswith ('#'):
        title = title[0:len(title)-1]
    
    title = title.strip()

    link = mdFile + '#' + title.replace(' ','-').lower()
    
    indent = '    ' * (count - 1)
    
    print indent + '- [' + title + '](' + link + ')'
    
def process (mdFile):
    with open(mdFile) as f:
        content = f.readlines()
    for line in content:
        if line.startswith("#"):
            gen_entry (mdFile, line)

if __name__ == "__main__":
    print '## Table Of Contents'
    print
    for arg in sys.argv:
        if arg.endswith('.md'):
            process (arg)
    print