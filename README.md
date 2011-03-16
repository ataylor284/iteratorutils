Iterator Utils for Groovy
=========================

Iterator Utils for groovy is a package inspired by python iterutils,
in turn inspired by constructs from APL, Haskell, and SML.  It
provides tools for doing lazy evaluation on any collection or stream
that implements the Iterable or Iterator interface.

Iterables, Iterators, And Collections
-------------------------------------

The Iterable interface was introduced in Java 1.5 and was added all
the collection datatypes.  It defines a single method, `iterator()`,
that returns an iterator.  Groovy conveniently adds an iterator method
to the Iterator class (which just returns itself), and although it
doesn't make Iterator formally implement Iterable, it effectively does
using groovy duck typing.

This means that Iterators and Iterables can be used interchangably,
using the iterator method to on either an Iterable or Iterator to get
an Iterator.  All the IteratorUtils methods can take an Iterator or an
Iterable.

Examples
--------

    import static ca.redtoad.util.IteratorUtils.*

    // squares of "infinite" set of numbers calculated lazily when take called
    def numbers = count()
    def squareNumbers = map({x -> x * x}, numbers)
    assert take(4, squareNumbers).toList() == [0, 1, 4, 9]

    // substring on number doesn't fail since it's never evaluated
    def list = ["string", "string", "string", 1.0]
    def subStrings = map({x -> x.substring(0, 3)}, list)
    assert take(3, subStrings).toList() == ["str", "str", "str"]


See Also
--------

http://groovy.codehaus.org/Iterator+Tricks