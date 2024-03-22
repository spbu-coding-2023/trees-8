![MegaLinter](https://github.com/spbu-coding-2023/trees-8/workflows/MegaLinter/badge.svg)
![CI_Test](https://github.com/spbu-coding-2023/trees-8/actions/workflows/CI_Test.yml/badge.svg)
[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)
[![CodeFactor](https://www.codefactor.io/repository/github/spbu-coding-2023/trees-8/badge)](https://www.codefactor.io/repository/github/spbu-coding-2023/trees-8)

# Binary Trees Project

Team #8's project on binary trees.

## About

Three trees are implemented in this library: [Red-Black](https://en.wikipedia.org/wiki/Red–black_tree), [AVL](https://en.wikipedia.org/wiki/AVL_tree) and [Binary Search Tree](https://en.wikipedia.org/wiki/Binary_search_tree).

## Authors

- [Aleksey Dmitrievtsev](https://github.com/admitrievtsev) - Red-Black Tree
- [Gleb Nasretdinov](https://github.com/Ycyken) - AVL Tree
- [Azamat Ishbaev](https://github.com/odiumuniverse) - Binary Search Tree

## Technologies

- Kotlin 1.9.22
- JDK 17
- [Gradle 8.6](https://gradle.org)

## Library information

Three trees are implemented, choose which one you want.
Standard methods are implemented in each tree:

+ `set(key,value)` - Associates the specified value with the specified key in the tree.
+ `remove(key)` - Removes the specified key and its corresponding value from the tree.
+ `get(key)` -  Returns the value corresponding to the given key
+ `iterator()` - Iteration by key-value pairs in order of keys

There are also many others methods:

+ `setIfAbsent(key,value)`
+ `getOrDefault(key, defaultValue)`
+ `getOrSet(key,defaultValue)`
+ `min()`
+ `max()`
+ `isEmpty()`
+ `isNotEmpty()`
+ `containsKey(key)`
+ `clear()`

AVL and Red-Black trees are self-balancing

### Suggestions and wishes

Suggestions can be made in code review or [Issues](https://github.com/spbu-coding-2023/trees-8/issues) on GitHub

### Licence

The library is distributed under the [GNU GPLv3 licence](https://www.gnu.org/licenses/gpl-3.0.html)
