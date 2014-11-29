# OFEXPORT2

*Updated $DATE.*

**Index**

- [Introduction](#introduction)
- [Audience](#audience)
- [How it works](#how-it-works)
- [Installation](#installation)
- [Ininstallation](#uninstallation)
- [Usage Overview](#usage-overview)
- [Basic Usage](#basic-usage)
- [Advanced Usage](#advanced-usage)

Before proceeding, please select the required version of this document:

- [Latest Stable Release: $VERSION](https://github.com/psidnell/ofexport2/blob/ofexport-v2-$VERSION/README.md)
- [Development Version](https://github.com/psidnell/ofexport2/blob/master/README.md)

## Introduction

**ofexport2** is a tool for exporting OmniFocus data to a variety of file formats, a succesor to [ofexport](https://github.com/psidnell/ofexport/blob/master/DOCUMENTATION.md).

This is an early version and at the time of writing I'm making major changes. If you need something reliable and with decent documentation then the original [ofexport](https://github.com/psidnell/ofexport/blob/master/DOCUMENTATION.md) may be the safer bet.

## Audience ##

To be able to use ofexport there are some pre-requisites. You need to:

- Have OmniFocus installed.
- Be comfortable using bash and the command line.
- Have Java 8 or know how to install it.
- Have read and appreciated The Hitchhikers Guide to the Galaxy.

Without all of the above I want nothing more to do with you. Goodbye.

## How it works

1. The tool reads the entire OmniFocus database.
2. Various command line filters are applied to eliminate unwanted data, sort, etc.
3. The remaining data is printed to the console or saved to a file in some specific format.

Currently supported export formats are:

1. Plain Text
2. Markdown
3. TaskPaper
4. CSV
5. OPML
6. XML
7. JSON

The key technologies used are:

1. [OGNL](http://commons.apache.org/proper/commons-ognl/) for specifying filters.
2. [FreeMarker](http://http://freemarker.org) for writing templates.
3. [Java 8](https://java.com/en/download/index.jsp) for the main command line program.

Other formats can be created by simply creating new FreeMarker templates.

## Installation ##

Installation is entirely manual and done from the command line, but just a matter or downloading/unpacking the zip and adding it's bin directory to your path.

### 1. You should have Java 8 already installed.

You can verify this by typing:

    java -version

You should see output similar to:

    java version "1.8.0_25"  
    Java(TM) SE Runtime Environment (build 1.8.0_25-b17)  
    Java HotSpot(TM) 64-Bit Server VM (build 25.25-b02, mixed mode)  

### 2. Download:

Download either:

- The latest stable version: [ofexport-v2-$VERSION.zip](https://github.com/psidnell/ofexport2/archive/ofexport-v2-$VERSION.zip)
- The current development version: [master.zip](https://github.com/psidnell/ofexport2/archive/master.zip)

Unzip this file and move/rename the root folder as you wish. For now I'm going to assume you moved and renamed it to **~/Applications/ofexport2**.

### 3. Set Execute Permission on the ofexport Shell Script ###

On the command line type:

    chmod +x ~/Applications/ofexport2/bin/ofexport

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

And verify everything has worked by typing **ofexport2** (or **of2**) and ensuring it prints it's command line options.

## Uninstallation ###

Simply delete the ofexport2 folder and remove the lines you added to your .bash_profile.

## Usage Overview ##

To print the contents of a named project (In this case I have a project called ofexport2) type:

    of2 -pn ofexport2

This outputs the following (explanations added):

    Home
      ofexport2
        [ ] Create "installer"
          [ ] need this month date option for TODO/DONE
          [ ] Print version in help
          [X] Copy in license, docs etc.- everything?
          [X] Create "release process" - versioning?
          [X] Freemarker to process README?
          [ ] maven site?
        [X] Filters (available etc) - finish - test
        [ ] Code Quality
          [ ] Coverage
          [X] Address TODOs
          [ ] Timing and stats
          [ ] Add logging
          [X] basic javadoc
          [X] Only integration tests should use real DB
          [X] format code

The default output format is a simple text list where uncompleted tasks are prefixed with a [ ] and completed tasks are prefixed with a [X].

What has happened is that the tool has searched all the projects for those that have the name "ofexport2"  (-pn specifies project name). For any that match it shows all the items directly above it (in this case my "Home" folder) and any items beneath it.

It's possible to refine this to include only uncompleted items:

    of2 -pn ofexport2 -te '!completed'

The single quotes are to prevent bash from seeing the '!' - it has special meaning in bash. The output will be:

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

Now what has happened is that after the "-pn" option (a filter) has been exectuted, a second filter has been run. In the case the "-te" option is a task expression (actually an [OGNL](http://commons.apache.org/proper/commons-ognl/) expression) that is specifying tasks that have not been completed.

In OGNL '!' means "not" and "completed" is one of several attributes that a Task has.

Folders, Projects, Tasks and Contexts all have attributes that you can use in filters. To get a complete list of the attributes available you can type:

    of2 -i
    
This will print all the attributes for all the types, for example this is just some of the Task attributes:

    Task:
        available (boolean): item is available.
        blocked (boolean): item is blocked.
        completed (boolean): item is complete.
        completionDate (date): date item was completed.
        contextName (string): contextName.
        deferDate (date): date item is to start.
        dueDate (date): date item is due.
        flagged (boolean): item is flagged.
        etc ...

And typing simply:

    of2
    
Will list all of the filtering options currently available.

## Basic Usage

TBD - GOT HERE



##  Advanced Usage ##

Explain the filtering process wrt OGNL

TBD

### Filtering ###

TBD

### Sorting ###

TBD

## Examples/CheatSheet ##

TBD

## Writing a Template ##

TBD

## Building It Yourself ##

TBD

## OFEXPORT vs OFEXPORT2

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