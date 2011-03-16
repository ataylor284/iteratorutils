// -*- mode: Groovy; c-basic-offset: 8; tab-width: 8; indent-tabs-mode: t; -*-

package ca.redtoad.util

import static ca.redtoad.util.IteratorUtils.*

class IteratorUtilsTests extends GroovyTestCase {

	void testRepeat() {
		def repeatIter = repeat(1)
		assert repeatIter.next() == 1
		assert repeatIter.next() == 1
		assert repeatIter.next() == 1
	}

	void testCountNoArgs() {
		def countIter = count()
		assert countIter.next() == 0
		assert countIter.next() == 1
		assert countIter.next() == 2
	}

	void testCountTwoStep() {
		def countIter = count(1, 2)
		assert countIter.next() == 1
		assert countIter.next() == 3
		assert countIter.next() == 5
	}

	void testCycleOne() {
		def cycleIter = cycle([1])
		assert cycleIter.next() == 1
		assert cycleIter.next() == 1
		assert cycleIter.next() == 1
	}

	void testCycleTwo() {
		def cycleIter = cycle([1, 2])
		assert cycleIter.next() == 1
		assert cycleIter.next() == 2
		assert cycleIter.next() == 1
		assert cycleIter.next() == 2
		assert cycleIter.next() == 1
		assert cycleIter.next() == 2
	}

	void testCycleLazy() {
		def cycleIter = cycle(repeat(1))
		assert cycleIter.next() == 1
		assert cycleIter.next() == 1
		assert cycleIter.next() == 1
	}

	void testTakeZero() {
		assert take(0, [1,2,3,4]).toList() == []
	}

	void testTakeOne() {
		assert take(1, [1,2,3,4]).toList() == [1]
	}

	void testTakeMore() {
		assert take(10, [1,2,3,4]).toList() == [1,2,3,4]
	}

	void testTakeLazy() {
		assert take(4, count(1)).toList() == [1,2,3,4]
	}

	void testTakeWhileZero() {
		assert takeWhile({ it -> it < 1 }, [1,2,3,4]).toList() == []
	}

	void testTakeWhileOne() {
		assert takeWhile({ it -> it == 1 }, [1,2,3,4]).toList() == [1]
	}

	void testTakeWhileMore() {
		assert takeWhile({ it -> it < 10 }, [1,2,3,4]).toList() == [1,2,3,4]
	}

	void testTakeWhileLazy() {
		assert takeWhile({ it -> it <= 4 }, count(1)).toList() == [1,2,3,4]
	}

	void testDropZero() {
		assert drop(0, [1,2,3,4]).toList() == [1,2,3,4]
	}

	void testDropOne() {
		assert drop(1, [1,2,3,4]).toList() == [2,3,4]
	}

	void testDropMore() {
		assert drop(10, [1,2,3,4]).toList() == []
	}

	void testDropLazy() {
		assert take(4, drop(4, count(1))).toList() == [5,6,7,8]
	}

	void testDropWhileZero() {
		assert dropWhile({ it -> it < 1 }, [1,2,3,4]).toList() == [1,2,3,4]
	}

	void testDropWhileOne() {
		assert dropWhile({ it -> it == 1 }, [1,2,3,4]).toList() == [2,3,4]
	}

	void testDropWhileMore() {
		assert dropWhile({ it -> it < 10 }, [1,2,3,4]).toList() == []
	}

	void testDropWhileLazy() {
		assert take(4, dropWhile({ it -> it <= 4 }, count(1))).toList() == [5,6,7,8]
	}

	void testMapZero() {
		assert map({ it -> it * it }, []).toList() == []
	}

	void testMapOne() {
		assert map({ it -> it * it }, [1]).toList() == [1]
	}

	void testMapSeveral() {
		assert map({ it -> it * it }, [1,2,3,4]).toList() == [1, 4, 9, 16]
	}

	void testMapLazy() {
		assert take(4, map({ it -> it * it }, count(1))).toList() == [1, 4, 9, 16]
	}

	void testFilterEmpty() {
		assert filter({ it -> it % 2 == 0 }, []).toList() == []
	}

	void testFilterMatchAll() {
		assert filter({ it -> it % 2 == 0 }, [0, 2, 4, 6]).toList() == [0, 2, 4, 6]
	}

	void testFilterMatchNone() {
		assert filter({ it -> it % 2 == 0 }, [1, 3, 5, 7]).toList() == []
	}

	void testFilterMatchSome() {
		assert filter({ it -> it % 2 == 0 }, [0, 1, 2, 3]).toList() == [0, 2]
	}

	void testFilterMatchLazy() {
		assert take(4, filter({ it -> it % 2 == 0 }, count())).toList() == [0, 2, 4, 6]
	}

	void testChainEmpty() {
		def chainedIter = chain([])
		assert chainedIter.toList() == []
	}

	void testChainOne() {
		def chainedIter = chain([[1,2,3]])
		assert chainedIter.toList() == [1,2,3]
	}

	void testChainTwo() {
		def chainedIter = chain([[1,2,3], [4,5,6]])
		assert chainedIter.toList() == [1,2,3,4,5,6]
	}

	void testZipEmpty() {
		def zipIter = zip([])
		assert zipIter.toList() == []
	}

	void testZipOneList() {
		def zipIter = zip([[1,2,3,4]])
		assert zipIter.toList() == [[1],[2],[3],[4]]
	}

	void testZipTwoLists() {
		def zipIter = zip([[1,2,3,4], [5, 6, 7, 8]])
		assert zipIter.toList() == [[1, 5],[2, 6],[3, 7],[4, 8]]
	}

	void testZipThreeLists() {
		def zipIter = zip([[1,2,3,4], [5, 6, 7, 8], [9, 10, 11, 12]])
		assert zipIter.toList() == [[1, 5, 9],[2, 6, 10],[3, 7, 11],[4, 8, 12]]
	}

	void testZipListsUnequalLengths() {
		def zipIter = zip([[1,2,3,4], [5, 6, 7], [9, 10]])
		assert zipIter.toList() == [[1, 5, 9],[2, 6, 10]]
	}

	void testZipListsOneInfinite() {
		def zipIter = zip([[1,2,3,4], count(5)])
		assert zipIter.toList() == [[1, 5],[2, 6],[3,7],[4,8]]
	}

	void testZipListsAllInfinite() {
		def zipIter = zip([count(1), count(5)])
		assert take(4, zipIter).toList() == [[1, 5],[2, 6],[3,7],[4,8]]
	}

	void testTeeOne() {
		def teeIters = tee([1,2,3,4].iterator(), 1)
		assert teeIters[0].toList() == [1,2,3,4]
	}

	void testTeeTwoSequential() {
		def teeIters = tee([1,2,3,4].iterator(), 2)
		assert teeIters[0].toList() == [1,2,3,4]
		assert teeIters[1].toList() == [1,2,3,4]
	}

	void testTeeTwoInterleaved() {
		def teeIters = tee([1,2,3,4].iterator(), 2)
		assert teeIters[0].next() == 1
		assert teeIters[1].next() == 1
		assert teeIters[0].next() == 2
		assert teeIters[1].next() == 2
		assert teeIters[0].next() == 3
		assert teeIters[1].next() == 3
		assert teeIters[0].next() == 4
		assert teeIters[1].next() == 4
		shouldFail(NoSuchElementException) {
			teeIters[0].next()
		}
		shouldFail(NoSuchElementException) {
			teeIters[1].next()
		}
	}

	void testTeeThree() {
		def teeIters = tee([1,2,3,4].iterator(), 3)
		assert teeIters[0].toList() == [1,2,3,4]
		assert teeIters[1].toList() == [1,2,3,4]
		assert teeIters[2].toList() == [1,2,3,4]
	}

	void testTeeTwoLazy() {
		def teeIters = tee(count(1), 2)
		assert take(4, teeIters[0]).toList() == [1,2,3,4]
		assert take(4, teeIters[1]).toList() == [1,2,3,4]
	}
}
