# Home

## ofexport2

### ofexport2

- Bugs
  - Paused projects seem to be considered "available"

> > https://github.com/psidnell/ofexport2/issues/1

  - Empty single action lists appear in contexts when empty

> > https://github.com/psidnell/ofexport2/issues/2

  - of2 -tx '!available' -p

> > https://github.com/psidnell/ofexport2/issues/3
> > Doesnt prune final Anywhere folder
> > 
> > AHHH - pruning needs to dynamically mod not juts set exclude flag:
> > 
> > setting a subnode to excluded still means its there and the logic fails.

  - Why traversing a node (folder) twice?
- Longer Term
  - Should the date logic not ignore time or does OF help out with blocked flag?
  - How to determine first available?
  - Decode attributes on contexts
  - sort XML/json field output

> > May not be easy:
> > http://stackoverflow.com/questions/9476426/serialized-json-with-sorted-keys-using-jackson



