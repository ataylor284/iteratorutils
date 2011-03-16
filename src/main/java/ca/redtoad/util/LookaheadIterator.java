// -*- mode: Java; c-basic-offset: 8; tab-width: 8; indent-tabs-mode: t; -*-

package ca.redtoad.util;

import java.util.Iterator;

/**
 * An iterator that allows peeking at the next element without consuming it.
 */
public class LookaheadIterator implements IterableIterator {

	private Iterator iter;
	private boolean hasLookahead;
	private Object lookahead;

	public LookaheadIterator(Iterator iter) {
		this.iter = iter;
		hasLookahead = false;
	}

	public boolean hasNext() {
		return hasLookahead || iter.hasNext();
	}

	public Object next() {
		if (hasLookahead) {
			hasLookahead = false;
			return lookahead;
		} else {
			return iter.next();
		}
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}

	public Iterator iterator() {
		return this;
	}

	/**
	 * Peek at the next element to be returned by next() without consuming it.
	 */
	public Object peek() {
		if (!hasLookahead) {
			lookahead = iter.next();
			hasLookahead = true;
		}
		return lookahead;
	}
}
