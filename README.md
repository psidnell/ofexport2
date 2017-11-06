# Update 2017-11-06

Sadly I can no longer provide support for this project owing to the demise of my Mac.

I'm happy to leave this here as a resource and merge in any pull requests but I'm no longer in a position to fix bugs or add features etc.

Significantly, [OmniFocus 3](https://www.omnigroup.com/blog/) has been announced for Q1 2018 which will almost certainly
break compatibility with ofexport2.


[Home](README.md) | [Release Notes](RELEASE-NOTES.md) | [Support](SUPPORT.md) | [Documentation](DOCUMENTATION.md)

# ofexport2

Export from OmniFocus to various other formats:

    of2 -o mindmap.opml
    of2 -duesoon -o todo.txt
    of2 -remaining -o whatsleft.taskpaper
    of2 -flagged -duesoon -o important.csv

![](doc/images/Diagram.png)

![](doc/images/Screen.png)

- [Licence: Apache License Version 2.0](LICENSE)
- [Blog: Poor Signal](http://poor-signal.blogspot.co.uk)
- [Twitter: @psidnell](http://twitter.com/psidnell)

## Table Of Contents

- [ofexport2](README.md#ofexport2)
- [Release Notes](RELEASE-NOTES.md#release-notes)
- [Support](SUPPORT.md#support)
- [Documentation](DOCUMENTATION.md#documentation)
    - [Overview](DOCUMENTATION.md#overview)
    - [Audience](DOCUMENTATION.md#audience)
    - [How it works](DOCUMENTATION.md#how-it-works)
    - [Installation](DOCUMENTATION.md#installation)
    - [Uninstallation](DOCUMENTATION.md#uninstallation)
    - [Quick Start](DOCUMENTATION.md#quick-start)
    - [Advanced Usage](DOCUMENTATION.md#advanced-usage)
        - [Filtering](DOCUMENTATION.md#filtering)
            - [Project vs Context Mode](DOCUMENTATION.md#project-vs-context-mode)
            - [Filtering by Text](DOCUMENTATION.md#filtering-by-text)
            - [Include or Exclude](DOCUMENTATION.md#include-or-exclude)
            - [Cascading](DOCUMENTATION.md#cascading)
            - [Filtering by Date](DOCUMENTATION.md#filtering-by-date)
            - [Useful Filtering Attributes](DOCUMENTATION.md#useful-filtering-attributes)
        - [Output and Formats](DOCUMENTATION.md#output-and-formats)
            - [Calendar Format](DOCUMENTATION.md#calendar-format)
        - [Sorting](DOCUMENTATION.md#sorting)
        - [Restructuring the Output](DOCUMENTATION.md#restructuring-the-output)
            - [Pruning](DOCUMENTATION.md#pruning)
            - [Simplifying](DOCUMENTATION.md#simplifying)
            - [Flattening](DOCUMENTATION.md#flattening)
        - [Inbox and No Context](DOCUMENTATION.md#inbox-and-no-context)
        - [Command Line Options](DOCUMENTATION.md#command-line-options)
        - [Full Attribute List](DOCUMENTATION.md#full-attribute-list)
        - [Configuration](DOCUMENTATION.md#configuration)
        - [Tips](DOCUMENTATION.md#tips)
            - [Include Projects with Tags](DOCUMENTATION.md#include-projects-with-tags)
            - [Save Useful Commands as Scripts](DOCUMENTATION.md#save-useful-commands-as-scripts)
            - [Solving Problems](DOCUMENTATION.md#solving-problems)
            - [Modifying Node Values](DOCUMENTATION.md#modifying-node-values)
        - [Writing a Template](DOCUMENTATION.md#writing-a-template)
        - [Building It Yourself](DOCUMENTATION.md#building-it-yourself)
    - [ofexport vs ofexport2](DOCUMENTATION.md#ofexport-vs-ofexport2)
    - [Other Approaches Considered](DOCUMENTATION.md#other-approaches-considered)
    - [Known Issues](DOCUMENTATION.md#known-issues)
- [FAQ](FAQ.md#faq)
    - [Exception in thread "main" java.lang.UnsupportedClassVersionError](FAQ.md#exception-in-thread-"main"-java.lang.unsupportedclassversionerror)
    - [java -version shows an older version of java](FAQ.md#java--version-shows-an-older-version-of-java)

