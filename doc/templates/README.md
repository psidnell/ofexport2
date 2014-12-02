# OFEXPORT2

*Updated $DATE.*

## Table of Contents

- [Introduction](#introduction)
- [Audience](#audience)
- [How it works](#how-it-works)
- [Installation](#installation)
- [Uninstallation](#uninstallation)
- [Usage](#usage)
    - [Overview](#overview)
    - [Filtering](#filtering)
- [Reference](#reference)
    - [Output and Formats](#output-and-formats)
    - [Project vs Context Mode](#project-vs-context-mode)
    - [Matching and Regular Expressions](#matching-and-regular-expressions)
    - [Date Filters](#date-filters)
    - [Sorting](#sorting)
    - [Examples](#examples)
- [Writing a Template](#writing-a-template)
- [Building it Yourself](#building-it-yourself)
- [ofexport vs ofexport2](#ofexport-vs-ofexport2)
- [Other Approaches Considered](#other-approaches-considered)
- [Known Issues](#known-issues)

## Introduction

**ofexport2** is a command line tool for exporting OmniFocus data to a variety of file formats and is a complete re-write of the original [ofexport](https://github.com/psidnell/ofexport/blob/master/DOCUMENTATION.md).

Before proceeding, please select the required version of this document:

- [Latest Stable Release: $VERSION](https://github.com/psidnell/ofexport2/blob/ofexport-v2-$VERSION/README.md)
- [Development Version](https://github.com/psidnell/ofexport2/blob/master/README.md)

This is an early version and at the time of writing I'm making major changes. If you need something reliable and with good documentation then the original [ofexport](https://github.com/psidnell/ofexport/blob/master/DOCUMENTATION.md) is stable and functional.

## Audience ##

To be able to use ofexport there are some pre-requisites, you need to:

- Have [OmniFocus](https://www.omnigroup.com/omnifocus) installed.
- Be comfortable using bash and the command line.
- Know what an [expression](http://en.wikipedia.org/wiki/Expression_(computer_science)) is.
- Have [Java 8](https://www.oracle.com/java/index.html) or know how to install it.
- Have read and appreciated [The Hitchhikers Guide to the Galaxy](http://en.wikipedia.org/wiki/The_Hitchhiker's_Guide_to_the_Galaxy).

You're going to have to know [where your towel is](http://hitchhikers.wikia.com/wiki/Towel).

## How it works

1. The tool reads the entire OmniFocus SQLite database.
2. Various command line filters are applied to eliminate unwanted data, sort items, etc.
3. The remaining data is printed to the console or saved to a file in some specific format.

Currently supported export formats are:

1. Plain Text
2. Markdown
3. TaskPaper
4. CSV
5. OPML
6. XML
7. JSON

The key technologies used for the transformation are:

1. [Java 8](https://java.com/en/download/index.jsp) for the main command line program.
2. [OGNL](http://commons.apache.org/proper/commons-ognl/) for specifying filters and sorting.
3. [FreeMarker](http://http://freemarker.org) for the templates to provide the output.

## Installation ##

Installation is entirely manual and done from the command line. Essentially you will be downloading/unpacking the zip and adding it's bin directory to your path.

### 1. You should have Java 8 already installed.

You can verify this by typing:

    java -version

You should see output similar to:

    java version "1.8.0_25"  
    Java(TM) SE Runtime Environment (build 1.8.0_25-b17)  
    Java HotSpot(TM) 64-Bit Server VM (build 25.25-b02, mixed mode)  

### 2. Download:

To get the required files, (in increasing order of danger) either:

- Download the latest stable version: [ofexport-v2-$VERSION.zip](https://github.com/psidnell/ofexport2/archive/ofexport-v2-$VERSION.zip)
- Download the current development version: [master.zip](https://github.com/psidnell/ofexport2/archive/master.zip)
- If you want to stay on the bleeding edge, check out this git repository so you can take updates as you wish.

If you downloaded a zip, unzip it and move/rename the root folder as you wish.

For now I'm going to assume you moved and renamed it to **~/Applications/ofexport2**.

### 3. Set Execute Permission on the ofexport Shell Script ###

On the command line type:

    chmod +x ~/Applications/ofexport2/bin/ofexport2

Make sure it's working by running:

    ~/Applications/ofexport2/bin/ofexport2

It should print it's command line options.

### 4. Add ofexport2 to your path

Create/Edit your **~/.bash_profile** and **add** the following lines:

    export PATH=$PATH:~/Applications/ofexport2/bin
    alias of2=ofexport2

The second line above isn't necessary but creates an alias for the program and makes the examples in this document more concise.

When done reload your environment by typing:

    . ~/.bash_profile

Finally verify everything has worked by typing **ofexport2** (or **of2**) and ensuring it prints it's command line options.

## Uninstallation ###

Simply delete the ofexport2 folder and remove the lines you added to your .bash_profile.

## Usage ##

### Overview ###

To print the contents of a named project (In this case I have a project called ofexport2, you should supply your own) type:

    of2 -pn 'ofexport2'

This outputs the following:

    Home
      ofexport2
        [ ] Create "installer"
          [ ] Print version in help
          [X] Add license, docs etc.
          [X] Create "release process"
          [X] Generate README.md from template
          [ ] maven site generation
        [X] Filters - finish - test
        [ ] Code Quality
          [ ] Coverage
          [X] Address TODOs
          [ ] Timing and stats
          [ ] Add logging
          [X] basic Javadoc
          [X] Only integration tests should use real database
          [X] format code

The default output format is a simple text list where uncompleted tasks are prefixed with a [ ] and completed tasks are prefixed with a [X].

The tool has searched all the projects for those that have the exact name "ofexport2" (-pn specifies project name). For any project that matches it shows all the items directly above it (in this case my "Home" folder) and any items beneath it.

The "-pn" specifies a filter (project name) and the text after it ('ofexport2') is it's argument. The single quotes around the argument aren't strictly necessary here but stop bash from attempting to interpret the contents. It's good practice to put single quotes around filter arguments since in more advanced examples they're going to contain spaces and all manner of characters from the 2nd row of your keyboard that would otherwise get bash very excited indeeed.  

### Filtering ###

- Filters are expressions used to limit what Folders, Projects, Tasks or Contexts appear in the output.
- Filters can be simple like a text search.
- Filters can be complex expressions that make use of various attributes of an item.
- Filters can either include items of interest or exclude unwanted items.
- Any number of filters can be used.
- Filters are executed in order, each filter is run on the results of the last, thus filters can only reduce what appears in the output.

The difference between include and exclude filters is:

- **include**: if the expression matches a node then it, it's parent and all it's descendents will be included in the output.
- **exclude**: if the expression matches a node then it and it's descendents are eliminated.

This sounds complicated but what it means is that generally when we want to see something you get to see where it is and everything under it. Conversely when you don't want to see something you don't see it or anything under it.

Building on the previous example, here's an operation with two include filters that shows all completed tasks in the "ofexport2" project:

    of2 -pn 'ofexport2' -ti 'completed'

Produces:

    Home
      ofexport2
        [ ] Create "installer"
          [X] Add license, docs etc.
          [X] Create "release process"
          [X] Generate README.md from template
        [X] Filters - finish - test
        [ ] Code Quality
          [X] Address TODOs
          [X] basic Javadoc
          [X] Only integration tests should use real database
          [X] format code

If instead you wanted to only see only uncompleted tasks you can change an include filter into an exclude filter:

    of2 -pn ofexport2 -tx 'completed'

Produces:

    Home
      ofexport2
        [ ] Create "installer"
          [ ] need this month date option for TODO/DONE
          [ ] Print version in help
          [ ] maven site?
        [ ] Code Quality
          [ ] Coverage
          [ ] Timing and stats
          [ ] Add logging

The "-tx" option is a task exclude expression (actually an [OGNL](http://commons.apache.org/proper/commons-ognl/) expression) that is eliminating tasks that have been completed.

In OGNL "completed" is one of several attributes that a Task has.

Note that:

    of2 -pn 'ofexport2'

is actually shorthand for:

    of2 -pi 'name=="ofexport2"'

There are include/exclude options for each node type. To see all the options type:

    of2
    
You will see -fn, -fi and -fx for Folders, -tn, -ti and -tx for Tasks etc.

Folders, Projects, Tasks and Contexts all have attributes that you can use in filters. To get a complete list of the attributes available you can type:

    of2 -i
    
This will print all the attributes for all the types, for example here are some of the Task attributes:

    Task:
        available (boolean): item is available.
        blocked (boolean): item is blocked.
        completed (boolean): item is complete.
        completionDate (date): date item was completed or null.
        contextName (string): contextName.
        deferDate (date): date item is to start or null.
        dueDate (date): date item is due or null.
        flagged (boolean): item is flagged.
        etc ...

Any number of expressions can be provided and filtering expressions can be any valid OGNL expression. These can be complex logical expressions:

    of2 -pi 'flagged && !available && taskCount > 1'

These expressions can provide fine grained control of what's printed.

# Reference #

#### Output and Formats ####

Output can be written to a file by using the "-o" option, e.g.

    of2 -pn "My Project" -o pyproj.md
    
The output will be in "Markdown" format because the file suffix is "md".

The supported suffixes are:

- md: Markdown format
- taskpaper: TaskPaper format
- txt: Text format
- opml: OPML format
- csv: CSV format
- html: HTML format

If you want to specify a format different from the one derived from the output file (or are printing to the console) you can use "-f <fmt>".

The format name (specified or derived from the filename suffix) is used to find a FreeMarker template file in **config/templates**.

#### Project vs Context Mode ####

Normally ofexport2 is in project mode, i.e. the project hierachy is used for filterng and output.

By using the "-c" option, the tool operates in context mode.

#### Matching and Regular Expressions #####

To search for a task that contains a substring:

    of2 -te 'name.contains("X")'
    
To use regular expressions:

    of2 -te 'name.matches(".*ollocks.*")'
    
Note that the part after the "." here is a java method call from the String class, you can use any method or expression returns a boolean:

    of2 -te 'name.matches(".*ollocks.*") && name.contains("gly") && name.length()>4'

#### Date Filters ####

There are various ways to match on dates, for example:

    of2 -te 'completed && completionDate > date("2014-11-26")'
    of2 -te 'completionDate==date("today")'
    of2 -te 'within(completionDate,"25th","yesterday")'
 
We're making use of various special functions here:

- **date** takes a date in string form and converts it to a Date object for use in the expression.
- **within** expressions takes 3 arguments (attribName, fromString, toString)

The strings formats of dates that are accepted are:

- **"2014-11-19"**: specific date (yyyy-mm-dd).
- **"Mon"**, **"mon"**, **"Monday"**, **"monday"**: the monday of this week (weeks start on Monday).
- **"-mon"**: The monday of last week.
- **"+mon"**: the monday of next week.
- **"Jan"**,**"jan"**,**"January"**,**"january"**: the 1st of January of this year.
- **"-Jan"**,**"-jan"**,**"-January"**,**"-january"**: the 1st of January last year.
- **"+Jan"**,**"+jan"**,**"+January"**,**"+january"**: the 1st of January next year.
- **"1d"**,"**+1day"**,**"-2days"**: days in the future/past.
- **"1w"**,"**+1week"**,**"-2weeks"**: weeks in the future/past.
- **"1m"**,"**+1month"**,**"-2months"**: months in the future/past.
- **"1y"**,"**+1year"**,**"-2years"**: months in the future/past.
- **"1st"**,"**2nd"**,**"23rd"**: day of this month.

### Sorting ###

By default items are sorted in the order they appear in OmniFocus. To specify an alternate order, for example sort by flagged status (unflagged then flagged):

    of2 -pn proj -ts flagged
    
To reverse the order:

    of2 -pn proj -ts r:flagged
    
It's possible (much like with filters) to chain multiple sort fields, for example to sort by flagged and then due:

    of2 -pn proj -ts r:flagged -ts dueDate

### Examples ###

All flagged and available tasks:

    of2 -te 'flagged && available'

All available and tasks overdue tasks or tasks due within the next week:

    of2 -te 'available && within(dueDate,"-1y","7d")'

All contexts that have no tasks:

    of2 -c -ce 'taskCount==0'

All completed projects, but not their tasks:

    of2 -pe completed -te false

Anything (folder, project task - because we're in project mode) that contains "spark plug":

    of2 -e 'name.toLowerCase().contains("spark plugs")'

Any task with a note containing "towel":

    of2 -te 'note!=null && note.contains("towel")'

## Writing a Template ##

- The templates are written in [FreeMarker](http://freemarker.org) syntax.
- The templates live in **config/templates**.
- Using the options "-f xxx" or "-o file.xxx" will cause ofexport to look for a template ***config/templates/xxx.ftl***.
- The object model available in the templates can be printed using **of2 -i**.
- Copying and experimenting on an existing template is the best way to start.

## Building It Yourself ##

The build is a straight forward java [maven 3](http://maven.apache.org) build.

After installing maven, cd into the ofexport folder and run:

    mvn clean package 

The build folder contains two utility scripts:

- **pre-release.sh** recreates several files with versions/dates updated.
- **release.sh** runs the maven release goals.

Before releasing can succeed you will need to update the pom file with your own:

- developerConnection
- distributionManagement/repository/url

## ofexport vs ofexport2

The original ofexport was written in python since python comes supplied with
OS X.

However, this version is written in Java. While this is inconvenient in that it
requires installing java, it does provide access to a lot of useful technologies
such as FreeMarker and Jackson. I also write better Java than Python.

## Other Approaches Considered

I originally wanted to access the OmniFocus data using AppleScript (or JavaScript
on Yosemite) and did actually get as far as a working prototype that serialised
JSON data from the osascript command back to the controlling program. While it was
in the end quite simple to do it was unbelievably slow, taking sometimes minutes for
a large export rather than about a second when accessing the database directly.

## Known Issues ##

- Task/Project notes are stripped back to ASCII on export because wide characters seem corrupted when I retrieve them. This could be down to the encoding OmniFocus uses or it could be an issue with the SQLite Java driver. I experimented with various obvious specific encodings but that didn't help.
- Context availability is something I haven't been able to deduce from the database.
- Perspective data is something I haven't managed to decode.
- In  OmniFocus, child Contexts/Tasks are interleaved, as are child Projects/Folders. In ofexport they are not.