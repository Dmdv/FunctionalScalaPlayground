package patmat

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

import patmat.Huffman._

@RunWith(classOf[JUnitRunner])
class HuffmanSuite extends FunSuite {
	trait TestTrees {
		val t1 = Fork(Leaf('a',2), Leaf('b',3), List('a','b'), 5)
		val t2 = Fork(Fork(Leaf('a',2), Leaf('b',3), List('a','b'), 5), Leaf('d',4), List('a','b','d'), 9)
    val pairsOfNils = List()
    val pairs = List(('a', 1), ('b', 2), ('c', 3))
	}

  test("empty list should return (char, 0) "){
    new TestTrees {

      val (found, rest) = findAndRemove('a', Nil, Nil)

      assert(found._1 === 'a')
      assert(found._2 === 0)
      assert(rest === pairsOfNils)
    }
  }

  test("findAndRemove should return ('a', 1) "){
    new TestTrees {

      val (found, rest) = findAndRemove('a', List(), pairs)

      assert(found._1 === 'a')
      assert(found._2 === 1)
      assert(rest.length === 2)
      assert(rest === List(('b', 2), ('c', 3)))
    }
  }

  test("findAndRemove should return ('b', 2) "){
    new TestTrees {

      val (found, rest) = findAndRemove('b', List(), pairs)

      assert(found._1 === 'b')
      assert(found._2 === 2)
      assert(rest.length === 2)
      assert(rest === List(('a', 1), ('c', 3)))
    }
  }

  test("findAndRemove should return ('z', 0) "){
    new TestTrees {

      val (found, rest) = findAndRemove('z', List(), pairs)

      assert(found._1 === 'z')
      assert(found._2 === 0)
      assert(rest === pairs)
    }
  }

  test("weight of a larger tree") {
    new TestTrees {
      assert(weight(t1) === 5)
    }
  }


  test("chars of a larger tree") {
    new TestTrees {
      assert(chars(t2) === List('a','b','d'))
    }
  }

  test("string2chars(\"hello, world\")") {
    assert(string2Chars("hello, world") === List('h', 'e', 'l', 'l', 'o', ',', ' ', 'w', 'o', 'r', 'l', 'd'))
  }


  test("makeOrderedLeafList for some frequency table") {
    assert(makeOrderedLeafList(List(('t', 2), ('e', 1), ('x', 3))) === List(Leaf('e',1), Leaf('t',2), Leaf('x',3)))
  }


  test("combine of some leaf list") {
    val leaflist = List(Leaf('e', 1), Leaf('t', 2), Leaf('x', 4))
    assert(combine(leaflist) === List(Fork(Leaf('e',1),Leaf('t',2),List('e', 't'),3), Leaf('x',4)))
  }


  test("decode and encode a very short text should be identity") {
    new TestTrees {
      assert(decode(t1, encode(t1)("ab".toList)) === "ab".toList)
    }
  }
}
