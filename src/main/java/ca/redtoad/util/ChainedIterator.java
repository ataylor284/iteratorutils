// -*- mode: Java; c-basic-offset: 8; tab-width: 8; indent-tabs-mode: t; -*-

package ca.redtoad.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * An iterator that chains together other iteratables.
 */
public class ChainedIterator implements IterableIterator {

	private Iterator iterablesIter;
	private Iterator currIterableIter;

	public ChainedIterator(Iterable iterables) {
		iterablesIter = iterables.iterator();
		try {
			currIterableIter = ((Iterable)iterablesIter.next()).iterator();
		} catch (NoSuchElementException e) {
			currIterableIter = null;
		}
	}

	public boolean hasNext() {
		try {
			while (currIterableIter != null) {
				if (currIterableIter.hasNext()) {
					return true;
				}
				currIterableIter = ((Iterable)iterablesIter.next()).iterator();
			}
			return false;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	public Object next() {
		if (!hasNext()) {
			throw new NoSuchElementException();
		}
		return currIterableIter.next();
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}

	public Iterator iterator() {
		return this;
	}
}
