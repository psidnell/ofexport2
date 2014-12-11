# Options

    OFEXPORT2
    
    Version: 1.0.11
    Build Date: 2014-12-10
    
    usage: ofexport2
     -h                print help.
     -i                print additional help information.
     -pc <arg>         include and cascade: projects and all beneath where expression is true.
     -pi <arg>         include: projects where expression is true.
     -px <arg>         exclude: projects where expression is true.
     -pn <arg>         include project specified by name.
     -ps <arg>         sort projects by field.
     -fc <arg>         include and cascade: folders and all beneath where expression is true.
     -fi <arg>         include: folders where expression is true.
     -fx <arg>         exclude: folders where expression is true.
     -fn <arg>         include folder specified by name.
     -fs <arg>         sort folders by field.
     -tc <arg>         include and cascade: tasks and all beneath where expression is true.
     -ti <arg>         include: tasks where expression is true.
     -tx <arg>         exclude: tasks where expression is true.
     -tn <arg>         include tasks specified by name.
     -ts <arg>         sort tasks by field
     -cc <arg>         include and cascade: contexts and all beneath where expression is true.
     -ci <arg>         include: contexts where expression is true.
     -cx <arg>         exclude: contexts where expression is true.
     -cn <arg>         include contexts specified by name.
     -cs <arg>         sort contexts by field.
     -ac <arg>         include and cascade: items and all beneath where expression is true.
     -ai <arg>         include: items where expression is true.
     -ax <arg>         exclude: items where expression is true.
     -an <arg>         include items specified by name.
     -m <arg>          modify a node value.
     -p                prune empty folders, projects and contexts.
     -S                Simplify hierarchies.
     -F                Flatten hierarchies.
     -c                context mode: filter and display context hierarchy instead of project hierarchy.
     -f <arg>          output in this format.
     -o <arg>          write output to the file.
     -O <arg>          write output to the file and open it.
     -D <arg>          set property: name=value.
     -export <arg>     export data to JSON file (for testing).
     -import <arg>     import data from JSON file instead of database (for testing).
     -loglevel <arg>   set log level [debug,info,warn,error].
