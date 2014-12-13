# OFEXPORT2

[Release Notes](RELEASE-NOTES.md)

Export from OmniFocus to various other formats:

![](doc/images/Diagram.png)

![](doc/images/Screen.png)

These above example files (and more) can be found here:

- [HTML (violently coloured)](http://htmlpreview.github.io/?https://github.com/psidnell/ofexport2/blob/master/src/test/data/example-p.html)
- [CSV](src/test/data/example-p.csv)
- [Markdown](src/test/data/example-p.md)
- [OPML](src/test/data/example-p.opml)
- [TaskPaper](src/test/data/example-p.taskpaper)
- [Report (a compact variant of Taskpaper)](src/test/data/example-p.report)
- [Text](src/test/data/example-p.txt)
- [XML](src/test/data/example-p.xml)
- [JSON](src/test/data/example-p.json)

## Support

- [Open a Bug](https://github.com/psidnell/ofexport2/issues)
- [Blog: Poor Signal](http://poor-signal.blogspot.co.uk)
- [Twitter: @psidnell](http://twitter.com/psidnell)

## Table of Contents

- [README](README.md)
    - [Support](README.md#support)
    - [Table of Contents](README.md#table-of-contents)
    - [Introduction](README.md#introduction)
    - [Audience](README.md#audience)
    - [How it works](README.md#how-it-works)
    - [Installation](README.md#installation)
        - [1. You should have Java 8 already installed.](README.md#1.-you-should-have-java-8-already-installed.)
        - [2. Download:](README.md#2.-download:)
        - [3. Set Execute Permission on the ofexport2 Shell Script](README.md#3.-set-execute-permission-on-the-ofexport2-shell-script)
        - [4. Add ofexport2 to your path](README.md#4.-add-ofexport2-to-your-path)
    - [Uninstallation](README.md#uninstallation)
    - [Usage](README.md#usage)
        - [Overview](README.md#overview)
        - [Filtering](README.md#filtering)
            - [Project vs Context Mode](README.md#project-vs-context-mode)
            - [Filtering by Text](README.md#filtering-by-text)
            - [Include or Exclude](README.md#include-or-exclude)
            - [Cascading](README.md#cascading)
            - [Filtering by Date](README.md#filtering-by-date)
            - [Useful Filtering Attributes](README.md#useful-filtering-attributes)
        - [Output and Formats](README.md#output-and-formats)
        - [Sorting](README.md#sorting)
        - [Pruning](README.md#pruning)
        - [Simplifying](README.md#simplifying)
        - [Flattening](README.md#flattening)
        - [Inbox and No Context](README.md#inbox-and-no-context)
        - [Command Line Options](README.md#command-line-options)
        - [Full Attribute List](README.md#full-attribute-list)
    - [Configuration](README.md#configuration)
        - [Tips](README.md#tips)
            - [Include Projects with Tags](README.md#include-projects-with-tags)
            - [Save Useful Commands as Scripts](README.md#save-useful-commands-as-scripts)
            - [Solving Problems](README.md#solving-problems)
            - [Modifying Node Values](README.md#modifying-node-values)
    - [Writing a Template](README.md#writing-a-template)
    - [Building It Yourself](README.md#building-it-yourself)
    - [ofexport vs ofexport2](README.md#ofexport-vs-ofexport2)
    - [Other Approaches Considered](README.md#other-approaches-considered)
    - [Known Issues](README.md#known-issues)

## Introduction

**ofexport2** is a command line tool for exporting OmniFocus data to a variety of file formats and is a complete re-write of the original [ofexport](https://github.com/psidnell/ofexport/blob/master/DOCUMENTATION.md).

## Audience ##

To be able to use ofexport2 there are some pre-requisites, you need to:

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

The Omnifocus database is read but **never modified**, the filters and modifications work only on the tools copy of the OmniFocus data.

Currently supported export formats are:

1. Plain Text
2. Markdown
3. TaskPaper
4. HTML
5. CSV
6. OPML
7. XML
8. JSON

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

To get the required files, either:

- Download a stable version from a link in the [Release Notes](RELEASE-NOTES.md).
- Download the current development version: [master.zip](https://github.com/psidnell/ofexport2/archive/master.zip)
- Check out the current development so you can take updates with git.

If you downloaded a zip, unzip it and move/rename the root folder as you wish.

For now I'm going to assume you moved and renamed it to **~/Applications/ofexport2**.

### 3. Set Execute Permission on the ofexport2 Shell Script ###

On the command line type:

    chmod +x ~/Applications/ofexport2/bin/ofexport2

Make sure it's working by running:

    ~/Applications/ofexport2/bin/ofexport2 -h

It should print it's command line options.

### 4. Add ofexport2 to your path

Create/Edit your **~/.bash_profile** and **add** the following lines:

    export PATH=$PATH:~/Applications/ofexport2/bin
    alias of2=ofexport2

The second line above isn't necessary but creates a convenient alias for the program and makes the examples in this document more concise.

When done reload your environment by typing:

    . ~/.bash_profile

Finally verify everything has worked by typing either:

    ofexport2 -h
    of2 -h

## Uninstallation ###

Simply delete the ofexport2 folder and remove the lines you added to your .bash_profile.

## Usage ##

### Overview ###

Simply typing:

    of2

Will dump the entire project hierarchy, for example:

    Get to Mars
      Build a Rocket Ship
        Learn Engineering
          [ ] Buy Engineering For Dummies
          [ ] Read Engineering For Dummies
        Learn Orbital Mechanics
          [ ] Buy Orbital Mechanics For Dummies
          [ ] Read Orbital Mechanics For Dummies
        Build Ship
          [ ] Buy Parts FLAGGED
            [ ] Buy Rocket Ship Kit FLAGGED
            [ ] Buy Glue FLAGGED
          [ ] Assemble Parts
          [ ] Let Glue Dry
          [ ] Apply Decals
      Launch Rocket Ship
        Prepare For Launch
          [ ] Buy Supplies
            [ ] Buy Space Suit - 34" Waist (eBay?, Amazon?)
            [ ] Buy Pop-Tarts (approx 2000)
            [ ] Buy Tea - Earl Grey (Hot)
          [ ] Load Supplies
          [ ] Aim at Mars
        Launch due:2025-12-01
          [ ] Light blue touch-paper due:2025-12-01
          [ ] Get in due:2025-12-01
          [ ] Close door due:2025-12-01
      Flight
        [ ] Give ships computer a name (Dave? Mother?)
        [ ] Enter hyper-sleep
        [ ] Wake up
        [ ] Check date, location
        [ ] Ignore any distress signals, don't land on Monkey planets
      Landing due:2027-12-01
        [ ] AAAAAAARGH! due:2027-12-01
        [ ] Say something profound due:2027-12-01
        [ ] Plant flag due:2027-12-01
        [ ] See if rocks made of polystyrene due:2027-12-01

The default output format is a simple text list where uncompleted tasks are prefixed with a [ ] and completed tasks are prefixed with a [X]. Items are indented to indicate the original OmniFocus hierarchy.

Basic usage of the tool is fairly straight-forward. If you're familiar with OmniFocus you can probably guess what these commands will show:

    of2 -ti all
    of2 -ti available
    of2 -ti remaining
    of2 -ti flagged
    of2 -ti completed

For example, if we ran:

    of2 -ti flagged

We'd see:

    Get to Mars
      Build a Rocket Ship
        Build Ship
          [ ] Buy Parts FLAGGED
            [ ] Buy Rocket Ship Kit FLAGGED
            [ ] Buy Glue FLAGGED

It's possible to isolate specific Folders or Projects etc. For example to print the contents of just one of the Folders in the above example:

    of2 -fn 'Launch Rocket Ship'

This outputs the following:

     Get to Mars
      Launch Rocket Ship
        Prepare For Launch
          [ ] Buy Supplies
            [ ] Buy Space Suit - 34" Waist (eBay?, Amazon?)
            [ ] Buy Pop-Tarts (approx 2000)
            [ ] Buy Tea - Earl Grey (Hot)
          [ ] Load Supplies
          [ ] Aim at Mars
        Launch due:2025-12-01
          [ ] Light blue touch-paper due:2025-12-01
          [ ] Get in due:2025-12-01
          [ ] Close door due:2025-12-01
          
So far we've just seen tasks in their project hierarchy. To see them in their context hierarchy add the "-c" option:

    of2 -c -cn 'Buy'

This isolates the "Buy" Context within the Context hierarchy:

    On Earth
      Buy
        [ ] Buy Parts FLAGGED
        [ ] Buy Space Suit - 34" Waist (eBay?, Amazon?)
        [ ] Buy Orbital Mechanics For Dummies
        [ ] Buy Rocket Ship Kit FLAGGED
        [ ] Buy Engineering For Dummies
        [ ] Buy Supplies
        [ ] Buy Glue FLAGGED
        [ ] Buy Pop-Tarts (approx 2000)
        [ ] Buy Tea - Earl Grey (Hot)

The options being supplied are called **filters** and they operate on **attributes** of the items in OmniFocus.

The attributes you select on can be combined by combining filters or using expressions. Filters typically take expressions (written in [OGNL](http://commons.apache.org/proper/commons-ognl/) syntax) as arguments, here we're using the remaining and due attributes as very simple expressions:

    of2 -c -ti 'remaining' -ti 'due.soon'

### Filtering ###

Advanced queries can be assembled with filters:

- Filters are expressions used to limit what Folders, Projects, Tasks or Contexts appear in the output.
- Filters can be applied to specific note types (Folders, Projects etc.)
- Filters can be simple like a text search.
- Filters can be complex expressions that make use of various attributes of an item.
- Filters can either include items of interest or exclude unwanted items.
- Filters can select a just a node, or a node and all those beneath it.
- Any number of filters can be used.
- Filters are executed in order, each filter is run on the results of the last, thus filters can only reduce what appears in the output.

#### Project vs Context Mode ####

Normally ofexport2 is in project mode, i.e. the project hierachy is used for filterng and output:

    of2 -ti available

By using the "-c" option, the tool operates in context mode:

    of2 -c -ti available

It's an error to try and use a project filter in context mode and vice versa.

#### Filtering by Text

To search for items with a specific exact names:

    of2 -fn "My Folder"
    of2 -pn "My Project"
    of2 -tn "My Task"
    of2 -c -cn "My Context"
    of2 -an "Anything Called This"

These name filters are actually a convienient shorthand for an expression that uses the name attribute of items, for example:

    of2 -fc 'name=="My Folder"'

By using the attribute in your own expressions, much more sophisticated filters can be created.

To search for a task that contains text:

    of2 -tc 'name.contains("X")'
    of2 -tc 'name.equalsIgnoreCase("X")'
    of2 -tc 'name.equalsIgnoreCase("ofexpoRt2")'
    of2 -tc 'name.toLowerCase().contains("x")'

(The functions/methods available in these expressions are from the [Java String class](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html)).
    
To use regular expressions:

    of2 -tc 'name.matches(".*X.*")'
    
These expressions can be fairly complex:

    of2 -tc 'name.matches(".*(Call|Phone).*") && name.contains("Dave")'

It's also possible to access the text of a note or project in filters:

    of2 -ti 'note!=null && note.contains("x")'

(That the **note!=null** check is necessary here because (unlike name) the note may be absent and there would be a Java error applying the **contains** method to the missing value.)

#### Include or Exclude

The difference between include and exclude filters is:

- **include**: if the expression matches a node then it then it's included, otherwsise it's eliminated.
- **exclude**: if the expression matches a node then it and it's descendents are eliminated.
- These filters are applied only to a specifc item type, others are ignored.

To illustrate the difference for a folder:

    of2 -fc 'name="MyFolder"'
    of2 -fx 'name="MyFolder"'

The first shows "MyFolder", the second shows everything except "MyFolder".

#### Cascading

Include filters come in two flavours, cascading and non cascading.

- **cascading**: include the item and everything beneath it.
- **non-cascading**: include just the item.

For example:

    of2 -fn "My Folder"
    of2 -fc 'name=="My Folder"'
    of2 -fi 'name=="My Folder"'

The first two are cascading and equivalent, they show matching folders and everything within them.

The third shows only the matching folders and nothing beneath them.

#### Filtering by Date ####

Tasks and Projects have several dates:

- **completion** - date when the item was completed.
- **completed** - true if item is complete.
- **defer** - date when the item becomes available.
- **due** - date when the item is due.
- **modified** - date when the item was last modified.
- **added** - date when the item was added.

There are various ways to match on dates and dateRanges:

    of2 -ti 'completion.is("today")'
    of2 -ti 'completion.between("5th","today")'
    of2 -ti '!completed && defer.is("today")'
    of2 -ti '!completed && due.after("2015-01-01")'
    of2 -ti '!completed && due.onOrAfter("2015-01-01")'
    of2 -ti '!completed && due.before("2015-01-01")'
    of2 -ti '!completed && due.onOrBefore("2015-01-01")'
    of2 -ti '!completed && due.soon'
    of2 -ti 'due.set'

Some of the above filters also include a check that the item is not completed (**!completed**). This is because completed items retain their defer and due dates. Typically when we want to know what's due or starting we're not interested in what we've already done. However if you did want to see them just remove the check.

Not all "completed" items have a completion date - this is the way OmniFocus works. For example if a Folder is dropped then the the Projects/Tasks within it can reasonably be said to be completed (i.e. neither available or remaining), but OmniFocus does not give them a completion date or delete them. The reason is probably so that the Folder status can be later changed back to active and the sub items will return from the dead.

When using '**.soon**', the value is set in the dueSoon configuration variable see [Configuration](#configuration). This can be applied to any of the dates but clearly makes no sense for the completion attribute!.

The '**.set**' is a way of determining if the item has the date set at all.

The strings formats of dates that are accepted in these filters are:

- **"yesterday"**,**"today"**,**"tomorrow"**
- **"2014-11-19"**: specific date (yyyy-mm-dd).
- **"Mon"**, **"mon"**, **"Monday"**, **"monday"**: the monday of this week (weeks start on Monday).
- **"-mon"**: the monday of last week.
- **"+mon"**: the monday of next week.
- **"Jan"**,**"jan"**,**"January"**,**"january"**: the 1st of January of this year.
- **"-Jan"**,**"-jan"**,**"-January"**,**"-january"**: the 1st of January last year.
- **"+Jan"**,**"+jan"**,**"+January"**,**"+january"**: the 1st of January next year.
- **"1d"**,"**+1day"**,**"-2days"**: days in the future/past.
- **"1w"**,"**+1week"**,**"-2weeks"**: weeks in the future/past.
- **"1m"**,"**+1month"**,**"-2months"**: months in the future/past.
- **"1y"**,"**+1year"**,**"-2years"**: months in the future/past.
- **"1st"**,"**2nd"**,**"23rd"**: day of this month.

#### Useful Filtering Attributes ####

Filtering **Tasks/Projects** on **status**:

    of2 -ti flagged
    of2 -ti unflagged
    of2 -ti '!flagged
    of2 -tx flagged
    of2 -pi flagged
    of2 -pi unflagged
    of2 -pc flagged

Filtering **Tasks/Projects** on **availability**:

    of2 -ti all
    of2 -ti available
    of2 -ti remaining
    of2 -ti completed
    of2 -pi remaining
    of2 -pc available

Filtering **Tasks/Projects** on **duration**:

    of2 -ti 'estimatedMinutes > 20'
    of2 -ti 'estimatedMinutes==-1' (unestimated)
    of2 -pi 'estimatedMinutes > 5 && estimatedMinutes < 10'

Filtering **Projects** on their **status**:

    of2 -pi active
    of2 -pc onHold
    of2 -px completed
    of2 -pi dropped

*Note: stalled and pending are not yet supported*

Filtering items by their **text**/**note**:

    of2 -c -cn 'exact name of context'
    of2 -pc 'name.contains("text in folder name")'
    of2 -fc 'name.matches(".*regularExpressionOnFolderName.*")'
    of2 -ti 'note!=null && note.contains("stuff")'

Filtering **Tasks**/**Projects** by **dates**:

    of2 -ti 'completion.is("today")'
    of2 -ti 'completion.between("5th","today")'
    of2 -ti '!completed && defer.is("today")'
    of2 -ti '!completed && due.after("2015-01-01")'
    of2 -ti '!completed && due.onOrAfter("2015-01-01")'
    of2 -ti '!completed && due.before("2015-01-01")'
    of2 -ti '!completed && due.onOrBefore("2015-01-01")'
    of2 -ti '!completed && due.soon'

Filtering **Folders** by **status**:

     of2 -fi active
     of2 -fx dropped

Filtering **Contexts** by **status**:

    of2 -c -ci active
    of2 -c -cc onHold
    of2 -c -cx dropped

Useful **Combinations**:

    of2 -ti 'available && defer.is("today")'
    of2 -ti 'available && (due.soon || flagged)'
    of2 -ti 'completion.between("mon","today")'
    of2 -ti 'due.set && remaining' -F -ts due

What I do to generate weekly reports. I want a flattened list of work tasks completed this week:

    of2 -fn 'Work' -px 'name.contains("Routine")' -ti 'completion.between("mon","today")' -p -F -f report -O ~/Desktop/Report.taskpaper

### Output and Formats ###

Output can be written to a file by using the "-o" option, e.g.

    of2 -pn "My Project" -o myproj.md
    
The output will be automatically be in "Markdown" format because the file suffix is "md".

The supported suffixes are:

- md: Markdown format.
- taskpaper: TaskPaper format.
- report: A specific TaskPaper format I use for generating reports.
- txt: Text format
- opml: OPML format.
- csv: CSV format.
- html: HTML format.
- debug: A text format that contains all attributes of nodes.

If you want to specify a format different from the one derived from the output file (or are printing to the console) you can override it:

    of2 -pn "My Project" -f md"
    of2 -pn "My Project" -f report -o myproj.taskpaper

The format name being applied is used to find a FreeMarker template file in **config/templates**.

It's possible to modify these or add your own.

### Sorting ###

The natural sort order of items is based on their position in OmniFocus. It's possible to specify
any attribute as the one to use for an item type:

    of2 -ti available -ts name
    of2 -ti available -ts r:name

The above sorts tasks by their name, with the second being a reverse sort.

It's possible to have chain the sorts much like filters:

    of2 -ts completion -ts name

This sorts items by their completion date, and for those with the same date it then sorts by name.

Items are sorted at a particular level. If a task has subtasks then the subtasks will be sorted but they will stay in their position beneath their parent task. This can be overcome by [Simplifying](#simplifying) or [Flattening](#flattening) the hierarchy.

It's possible to sort by any attribute, including flagged status. The following displays flagged items above unflagged ones, and within that sorts by due:

    of2 -ti 'remaining && due.soon' -ts r:flagged -ts due

### Pruning ###

Sometimes the output is cluttered with empty Contexts, Folders or Projects that you want to keep in OmniFocus but not see in the output.

Using the **-p** option eliminates them.

### Simplifying ###

Sometimes what is a useful Folder/Context hierarchy in OmniFocus ends up making reports look cluttered.

The **-S** option simplifies the nested the hierarchy to just leaf Folder/Projects/Contexts and lifts sub tasks up to the level of their parent.

All projects and contexts are moved to the root level, and sub tasks moved up to the level of their parent.

Simplifying our Mars project example:

    of2 -S

Would give us:

    Learn Engineering
      [ ] Buy Engineering For Dummies
      [ ] Read Engineering For Dummies
    Learn Orbital Mechanics
      [ ] Buy Orbital Mechanics For Dummies
      [ ] Read Orbital Mechanics For Dummies
    Build Ship
      [ ] Buy Parts FLAGGED
      [ ] Buy Rocket Ship Kit FLAGGED
      [ ] Buy Glue FLAGGED
      [ ] Assemble Parts
      [ ] Let Glue Dry
      [ ] Apply Decals
    Prepare For Launch
      [ ] Buy Supplies
      [ ] Buy Space Suit - 34" Waist (eBay?, Amazon?)
      [ ] Buy Pop-Tarts (approx 2000)
      [ ] Buy Tea - Earl Grey (Hot)
      [ ] Load Supplies
      [ ] Aim at Mars
    Launch due:2025-12-01
      [ ] Light blue touch-paper due:2025-12-01
      [ ] Get in due:2025-12-01
      [ ] Close door due:2025-12-01
    Flight
      [ ] Give ships computer a name (Dave? Mother?)
      [ ] Enter hyper-sleep
      [ ] Wake up
      [ ] Check date, location
      [ ] Ignore any distress signals, don't land on Monkey planets
    Landing due:2027-12-01
      [ ] AAAAAAARGH! due:2027-12-01
      [ ] Say something profound due:2027-12-01
      [ ] Plant flag due:2027-12-01
      [ ] See if rocks made of polystyrene due:2027-12-01

### Flattening

Flattening with **-F** is an extreme form of simplifying. All existing hierachies are erased leaving a flat list of tasks under a new Project/Context called "Tasks".

Flattening our Mars project example:

    of2 -F

Would give us:

    Tasks
      [ ] Buy Engineering For Dummies
      [ ] Read Engineering For Dummies
      [ ] Buy Orbital Mechanics For Dummies
      [ ] Read Orbital Mechanics For Dummies
      [ ] Buy Parts FLAGGED
      [ ] Buy Rocket Ship Kit FLAGGED
      [ ] Buy Glue FLAGGED
      [ ] Assemble Parts
      [ ] Let Glue Dry
      [ ] Apply Decals
      [ ] Buy Supplies
      [ ] Buy Space Suit - 34" Waist (eBay?, Amazon?)
      [ ] Buy Pop-Tarts (approx 2000)
      [ ] Buy Tea - Earl Grey (Hot)
      [ ] Load Supplies
      [ ] Aim at Mars
      [ ] Light blue touch-paper due:2025-12-01
      [ ] Get in due:2025-12-01
      [ ] Close door due:2025-12-01
      [ ] Give ships computer a name (Dave? Mother?)
      [ ] Enter hyper-sleep
      [ ] Wake up
      [ ] Check date, location
      [ ] Ignore any distress signals, don't land on Monkey planets
      [ ] AAAAAAARGH! due:2027-12-01
      [ ] Say something profound due:2027-12-01
      [ ] Plant flag due:2027-12-01
      [ ] See if rocks made of polystyrene due:2027-12-01

The name of the resultant root node ("Tasks") can be modified by changing the **flattenedRootName** configuration value.

### Inbox and No Context

OmniFocus has special pseudo items **Inbox** and **No Context**. The ofexport2 tool displays both of these as if they were a normal Project and Context so they can be used in filters and templates.

### Command Line Options

The full options list be displayed by typing:

    of2 -h

or [here](doc/Options.md).

### Full Attribute List

The full list of attributes that can be used in expressions or filters can be displayed by typing:

    of2 -i

or [here](doc/Attributes.md).

## Configuration ##

Configuration files you might need to modify are:

    /config/templates/... - FreeMarker templates.
    /config/config.properties - Config values for the application and templates.
    /config/config.xml - contains the possible locations for the OmniFocus database.

It's possible to override values in config.properties from the command line, for example reducing the dueSoon time to 2 days:

    of2 -D 'dueSoon=2d' -ti dueSoon

### Tips ###

#### Include Projects with Tags ####

Generate a report containing projects whose note contains "#report#".

    of2 -pi 'note!=null && note.contains("#report")'

#### Save Useful Commands as Scripts ####

Keep regularly used commands as scripts to save time.

Here's one of my base scripts as an example.

    #!/bin/bash
    
    FOLDER="$1"
    WHEN="$2"
    
    FILE="$HOME/Desktop/REPORT-$FOLDER-`date +"%Y-W%V-%h-%d"`.taskpaper"
    
    ofexport2 \
     -ac "(type==\"Folder\" && name.matches(\"$FOLDER|Anywhere\")) || (type==\"Project\" && name==\"Inbox\")" \
     -fx 'name.contains("Routine Maint")' \
     -F \
     -ti "completion.onOrAfter(\"$WHEN\")" \
     -p \
     -f report \
     -O "$FILE"

#### Solving Problems

- Add filters one at time until you get the required output.
- Use the **-f debug** to use the debug format that lists all attributes of an item.

#### Modifying Node Values

Suppose you want a report but don't want to see Task notes:

    of2 -m 'type=="Task" && note=null'

Note the use of the single '=' in the second part of the expression, this actually modifies (deletes) the note. All manner of lunacy is possible:

    of2 -m 'name.contains("Fight Club") && name=name.replaceAll("Fight", "XXXXX")'

## Writing a Template ##

- The templates are written in [FreeMarker](http://freemarker.org) syntax.
- The templates live in **config/templates**.
- Using the options "-f xxx" or "-o file.xxx" will cause ofexport to look for a template ***config/templates/xxx.ftl***.
- The object model available in the templates can be printed using **of2 -i**.

Copying and experimenting on an existing template is the best way to start.

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
such as FreeMarker, OGNL and Jackson. I also write better Java than Python.

## Other Approaches Considered

I originally wanted to access the OmniFocus data using AppleScript (or JavaScript
on Yosemite) and did actually get as far as a working prototype that serialised
JSON data from the osascript command back to the controlling program. While it was
in the end quite simple to do it was unbelievably slow, taking sometimes minutes for
a large export rather than about a second when accessing the database directly. While this approach would have been officialy suppotable by Omni it was unfotunately impractical. 

## Known Issues ##

- Task/Project notes are stripped back to ASCII on export because wide characters seem corrupted when I retrieve them. This could be down to the encoding OmniFocus uses or it could be an issue with the SQLite Java driver.
- Perspective data is something I haven't managed to decode.
- In  OmniFocus, child Contexts/Tasks are interleaved, as are child Projects/Folders. In ofexport they are not.
- There is currently no way to detect if Projects are **stalled** or **pending**.