# OFEXPORT2

A tool for exporting OmniFocus data to a variety of file formats.

This is an early version and at the time of writing I'm making major changes. If you need something reliable and with decent documentation then the original ofexport may be the safer bet.

## How it works

1. The tool reads the entire OmniFocus database.
2. The tree structure of the data is recreated.
3. Various command line filters are applied eliminate unwanted data, sort, etc.
4. The remaining data is printed to the console or saved to a file in some format.

Supported export formats are:

1. Plain Text
2. Markdown
3. TaskPaper
4. OPML
5. XML
6. JSON

Other formats can be created by dropping a new FreeMarker template into the **config/templates** directory.

The key technologies used are:

1. [OGNL](http://commons.apache.org/proper/commons-ognl/) for specifying filters.
2. [FreeMarker](http://http://freemarker.org) for writing templates.
3. [Java 8](https://java.com/en/download/index.jsp) for the main command line program.


TBD

## Installation ##


## Examples/CheatSheet ##

##  Advanced Usage ##

Explain the filtering process wrt OGNL

TBD

### Filtering ###

### Sorting ###


## Writing a Template ##


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
en the end quite simple to do it was unbelievably slow taking sometimes minutes for
a large export rather than about a second when accessing the database directly.

## Known Issues ##

- Task/Project notes are stripped back to ASCII on export because wide characters seem corrupted when I retrieve them. This could be down to the encoding OmniFocus uses or it could be an issue with the SQLite Java driver. I experimented with various obvious specific encodings but that didn't help. 