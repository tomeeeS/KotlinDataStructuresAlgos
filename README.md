Data structures and algorithms in Kotlin for JVM development

## Features
### Bag
Or multiset. Basically a HashMap<T, Int> with the values being the cardinalities of the keys in it.
#### Properties
- cardinalities: Returns the collection of cardinalities (piece counts for the item keys)  
- keys: Returns the collection of keys (items without their cardinalities)  
- keyCount: Returns the number of different items in the bag  
- totalSize: Returns the sum of pieces in the bag respecting the cardinalities  
- most: Returns a pair of (key, piece count) for the item that has the most pieces for  

### IndefiniteSizeMatrix
We don't have to set any size in advance at all. With putting items in the matrix we can expand it arbitrarily. If we put to previously nonexistent indices, they get created in the process seamlessly, and it stays a valid rectangular matrix by setting a default value to the indices in between.

## Licence
Apache-2.0 (do with it whatever you please)
