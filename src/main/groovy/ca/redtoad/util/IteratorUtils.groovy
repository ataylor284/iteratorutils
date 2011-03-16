// -*- mode: Groovy; c-basic-offset: 8; tab-width: 8; indent-tabs-mode: t; -*-

package ca.redtoad.util

class IteratorUtils {
	
	static Iterator repeat(obj) {
		[
			hasNext: { -> true },
			next: { -> obj },
			remove: { -> throw new UnsupportedOperationException() },
			iterator: { -> delegate },
			toString: { -> "repeat iterator (obj = $obj)" as String },
		] as IterableIterator
	}

	/**
	 * An iterator that returns start + (n * step) on the nth call.
	 */
	static Iterator count(start=0, step=1) {
		def value = start
		[
			hasNext: { -> true },
			next: { ->
				def saved = value
				value = value + step
				saved
			},
			remove: { -> throw new UnsupportedOperationException() },
			iterator: { -> delegate },
			toString: { -> "count iterator (value = $value)" as String },
		] as IterableIterator
	}

	static Iterator cycle(/*Iterable*/ iterable) {
		def iter = iterable.iterator()
		def originalIter = true
		def saved = []
		[
			hasNext: { -> true },
			next: { -> 
				if (iter.hasNext()) {
					if (originalIter) {
						saved << iter.next()
						saved[-1]
					} else {
						iter.next()
					}
				} else {
					iter = saved.iterator()
					originalIter = false
					iter.next()
				}
			},
			remove: { -> throw new UnsupportedOperationException() },
			iterator: { -> delegate },
			toString: { -> "cycle iterator (saved = $saved)" as String },
		] as IterableIterator
	}

	/**
	 * Chains together a list of iterators.
	 */
	static Iterator chain(/*Iterable*/ iterables) {
		new ChainedIterator(iterables)
	}

	/**
	 * Takes the first n elements from iterable.
	 */
	static Iterator take(int n, /*Iterable*/ iterable) {
		def iter = iterable.iterator()
		[
			hasNext: { -> iter.hasNext() && n > 0 },
			next: { -> 
				if (n <= 0) { throw new NoSuchElementException() }
				n--
				iter.next()
			},
			remove: { -> throw new UnsupportedOperationException() },
			iterator: { -> delegate },
			toString: { -> "take iterator (n = $n)" as String },
		] as IterableIterator
	}

	/**
	 * Takes elements from iterable while the predicate is true.
	 */
	static Iterator takeWhile(Closure predicate, /*Iterable*/ iterable) {
		def iter = new LookaheadIterator(iterable.iterator())
		[
			hasNext: { -> iter.hasNext() && predicate.call(iter.peek()) },
			next: { -> 
				if (!predicate.call(iter.peek())) {
					throw new NoSuchElementException()
				}
				iter.next()
			},
			remove: { -> throw new UnsupportedOperationException() },
			iterator: { -> delegate },
			toString: { -> "takeWhile iterator" },
		] as IterableIterator
	}

	/**
	 * Drops the first n elements from iterable.
	 */
	static Iterator drop(int n, /*Iterable*/ iterable) {
		def iter = iterable.iterator()
		try {
			for (int i = 0; i < n; i++) {
				iter.next()
			}
		} catch (NoSuchElementException e) {
			// ignore
		}
		iter
	}

	/**
	 * Drops elements from iterable while the predicate is true.
	 */
	static Iterator dropWhile(Closure predicate, /*Iterable*/ iterable) {
		def iter = new LookaheadIterator(iterable.iterator())
		try {
			while (predicate.call(iter.peek())) {
				iter.next()
			}
		} catch (NoSuchElementException e) {
			// ignore
		}
		iter
	}

	static Iterator map(Closure function, /*Iterable*/ iterable) {
		def iter = iterable.iterator()
		[
			hasNext: { -> iter.hasNext() },
			next: { -> 
				function.call(iter.next())
			},
			remove: { -> throw new UnsupportedOperationException() },
			iterator: { -> delegate },
			toString: { -> "map iterator" },
		] as IterableIterator
	}

	static Iterator filter(Closure predicate, /*Iterable*/ iterable) {
		def iter = new LookaheadIterator(iterable.iterator())
		[
			hasNext: { -> 
				while(iter.hasNext() && !predicate.call(iter.peek())) { 
					iter.next()
				}
				iter.hasNext()
			},
			next: { -> 
				while (!predicate.call(iter.peek())) {
					iter.next()
				}
				iter.next()
			},
			remove: { -> throw new UnsupportedOperationException() },
			iterator: { -> delegate },
			toString: { -> "filter iterator" },
		] as IterableIterator
	}

	static Iterator zip(/*Iterable*/ iterables) {
		def iterators = iterables.iterator().toList()*.iterator()
		[
			hasNext: { -> 
				iterators && iterators.every{ it.hasNext() }
			},
			next: { -> 
				iterators*.next()
			},
			remove: { -> throw new UnsupportedOperationException() },
			iterator: { -> delegate },
			toString: { -> "zip iterator" },
		] as IterableIterator
	}

	static List tee(Iterator iter, int n) {
		List deques = (0..<n).collect{new ArrayDeque()}
		deques.collect { mydeque ->
			[
				hasNext: { -> 
					(!mydeque.isEmpty()) || iter.hasNext()
				},
				next: {	-> 
					if (mydeque.isEmpty()) {
						def newVal = iter.next()
						deques.each { d ->
							d.addLast(newVal)
						}
					}
					mydeque.removeFirst()
				},
				remove: { -> throw new UnsupportedOperationException() },
				iterator: { -> delegate },
				toString: { -> "tee iterator" },
			] as IterableIterator
		}
	}
}
