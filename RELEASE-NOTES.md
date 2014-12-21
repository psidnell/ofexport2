[Home](README.md) | [Release Notes](RELEASE-NOTES.md) | [Support](SUPPORT.md) | [Documentation](DOCUMENTATION.md)

# Release Notes

**[Development Version](https://github.com/psidnell/ofexport2/archive/master.zip)**

**[1.0.18 (2014-12-21)](https://github.com/psidnell/ofexport2/archive/ofexport-v2-1.0.18.zip)**

- Fix so that allday calendar events never have alarm (at midnight the night before? it makes no sense).
- Added -v option to print activity summary during processing. 

**[1.0.17 (2014-12-18)](https://github.com/psidnell/ofexport2/archive/ofexport-v2-1.0.17.zip)**

- Added control of calendar item export in task notes and global config.

**[1.0.16 (2014-12-18)](https://github.com/psidnell/ofexport2/archive/ofexport-v2-1.0.16.zip)**

- Adding calendar (ICS) output.

**[1.0.15 (2014-12-15)](https://github.com/psidnell/ofexport2/archive/ofexport-v2-1.0.15.zip)**

- Changed executable name to of2, you need to delete the "of2" alias if you have one.
- Added "easy" options and Quick Start documentation.

**[1.0.14 (2014-12-14)](https://github.com/psidnell/ofexport2/archive/ofexport-v2-1.0.14.zip)**

- Fixed a date range bug.
- Made expression date format configurable.
- Internal tidying.

**[1.0.13 (2014-12-13)](https://github.com/psidnell/ofexport2/archive/ofexport-v2-1.0.13.zip)**

- Adjusted date sort so items with date appear above those without by default.
- Considering items complete if folder dropped.
- Fixed missing colons in debug template.
- Made naming of config properties consistent.
- Generated table of contents with python script (build/toc.py).

**[1.0.12 (2014-12-11)](https://github.com/psidnell/ofexport2/archive/ofexport-v2-1.0.12.zip)**

- Fixed bug in CSV template that marked all items as flagged.
- Fixed a ranking bug that caused items to appear in the wrong order in Context mode or after flattening.
- Improved Markdown formatting.
- Fixed due/done confusion in Taskpaper template.
- Fixed HTML contexts indenting.

**[1.0.11 (2014-12-10)](https://github.com/psidnell/ofexport2/archive/ofexport-v2-1.0.11.zip)**

- Documentation Improvements
- Provided two variants of Flatten filter, -S and -F.

**[1.0.10 (2014-12-09)](https://github.com/psidnell/ofexport2/archive/ofexport-v2-1.0.10.zip)**

- Documentation improvements.
- Template fixes (null pointer exception).

**[1.0.9 (2014-12-08)](https://github.com/psidnell/ofexport2/archive/ofexport-v2-1.0.9.zip)**

- Added some missing attributes to the debug template.
- Major re-working of date logic to simplify expressions.

**[1.0.8 (2014-12-07)](https://github.com/psidnell/ofexport2/archive/ofexport-v2-1.0.8.zip)**

- Flagged now works hierarchically.
- Added estimatedMinutes.
- Big simplification of filter usage.
- Updated documentation.

**[1.0.7 (2014-12-05)](https://github.com/psidnell/ofexport2/archive/ofexport-v2-1.0.7.zip)**

- Fixed prune bug.

**[1.0.6 (2014-12-05)](https://github.com/psidnell/ofexport2/archive/ofexport-v2-1.0.6.zip)**

- Fixed bugs relating to cascading availability flag.
- Fixed bug affecting visibility of available projects in context mode.

**[1.0.5 (2014-12-04)](https://github.com/psidnell/ofexport2/archive/ofexport-v2-1.0.5.zip)**

- Added diagnostic template (debug.ftl).
- Fixed bug where -F caused duplication of Projects.
- Added -O fname that writes to a file then opens it.

**[1.0.4 (2014-12-03)](https://github.com/psidnell/ofexport2/archive/ofexport-v2-1.0.4.zip)**

- Fixed prune bug.
- Added -F flatten option.
- Added a "Tips" section in the documentation.

**[1.0.3 (2014-12-03)](https://github.com/psidnell/ofexport2/archive/ofexport-v2-1.0.3.zip)**

- Fixed bug when using "-pn x", was adding single quotes to expression instead of double.
- Added extensive logging.
- Improved resource usage during build/test.
- Changed structure of root model passed into freemarker template.
- Created report.ftl, a weekly report template.
- Added config.properties and way to access them from templates.
- Added missing -cn option.

**[1.0.2 (2014-12-02)](https://github.com/psidnell/ofexport2/archive/ofexport-v2-1.0.2.zip)**

- Ironing out release issues.

**[1.0.1 (2014-12-02)](https://github.com/psidnell/ofexport2/archive/ofexport-v2-1.0.1.zip)**

- Ironing out release issues.

**[1.0.0 (2014-12-02)](https://github.com/psidnell/ofexport2/archive/ofexport-v2-1.0.0.zip)**

- First release.