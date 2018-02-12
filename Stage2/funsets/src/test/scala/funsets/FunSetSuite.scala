package funsets

import org.scalatest.FunSuite


import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

/**
 * This class is a test suite for the methods in object FunSets. To run
 * the test suite, you can either:
 *  - run the "test" command in the SBT console
 *  - right-click the file in eclipse and chose "Run As" - "JUnit Test"
 */
@RunWith(classOf[JUnitRunner])
class FunSetSuite extends FunSuite {

  /**
   * Link to the scaladoc - very clear and detailed tutorial of FunSuite
   *
   * http://doc.scalatest.org/1.9.1/index.html#org.scalatest.FunSuite
   *
   * Operators
   *  - test
   *  - ignore
   *  - pending
   */

  /**
   * Tests are written using the "test" operator and the "assert" method.
   */
  test("string take should return 'Hello'") {
   val message = "hello, world"
   assert(message.take(5) == "hello")
  }

  test("'|' as union should result to 1, 2, 3, 4"){
    val left = Set(1,2)
    val right = Set(3,4)
    val result = left | right
    assert(result === Set(1,2,3,4))
  }

  test("'&' as intersect should contain 2"){
    val left = Set(1,2)
    val right = Set(3,4,2)
    val result = left & right
    assert(result === Set(2))
  }

  /**
   * For ScalaTest tests, there exists a special equality operator "===" that
   * can be used inside "assert". If the assertion fails, the two values will
   * be printed in the error message. Otherwise, when using "==", the test
   * error message will only say "assertion failed", without showing the values.
   *
   * Try it out! Change the values so that the assertion fails, and look at the
   * error message.
   */
  // test("adding ints") {
  //   assert(1 + 2 === 3)
  // }


  import FunSets._

  test("contains is implemented") {
    assert(contains(_ => true, 100))
  }


  /**
   * When writing tests, one would often like to re-use certain values for multiple
   * tests. For instance, we would like to create an Int-set and have multiple test
   * about it.
   *
   * Instead of copy-pasting the code for creating the set into every test, we can
   * store it in the test class using a val:
   *
   *   val s1 = singletonSet(1)
   *
   * However, what happens if the method "singletonSet" has a bug and crashes? Then
   * the test methods are not even executed, because creating an instance of the
   * test class fails!
   *
   * Therefore, we put the shared values into a separate trait (traits are like
   * abstract classes), and create an instance inside each test method.
   *
   */

  trait TestSets {
    val s1: Set = singletonSet(1)
    val s2: Set = singletonSet(2)
    val s3: Set = singletonSet(3)
    val all: Set = union(s1, union(s2, s3))
  }

  /**
   * This test is currently disabled (by using "ignore") because the method
   * "singletonSet" is not yet implemented and the test would fail.
   *
   * Once you finish your implementation of "singletonSet", exchange the
   * function "ignore" by "test".
   */
  test("singletonSet(1) should contain just 1") {

    /**
     * We create a new instance of the "TestSets" trait, this gives us access
     * to the values "s1" to "s3".
     */
    new TestSets {
      /**
       * The string argument of "assert" is a message that is printed in case
       * the test fails. This helps identifying which assertion failed.
       */
      assert(contains(s1, 1), "s1 should contain 1")
    }
  }

  test("singletonSet(1) should not contain other than 1") {

    new TestSets {
      assert(!contains(s1, 2), "s1 should contain 2")
      assert(!contains(s1, 3), "s1 should contain 3")
      assert(!contains(s2, 1), "s2 should not contain 1")
    }
  }

  test("singletonSet(1) should be invokable and return true for 1") {

    new TestSets {
      assert(s1(1))
      assert(!s1(2))
    }
  }

  test("union should contain all elements of each set") {

    new TestSets {
      val s: Set = union(s1, s2)

      assert(contains(s, 1), "Union 1")
      assert(contains(s, 2), "Union 2")
      assert(!contains(s, 3), "Union 3")
    }
  }

  test("intersect should contain the elements on both sets") {

    new TestSets {
      val s: Set = intersect(s1, s2)
      assert(!contains(s, 1), "Intersect 1")
      assert(!contains(s, 2), "Intersect 2")
      assert(!contains(s, 3), "Intersect 3")
    }
  }

  test("intersection should contains only elements that are common for each set") {

    new TestSets {

      val set1: Set = union(s1, s2)
      val set2: Set = union(s2, s3)
      val s: Set = intersect(set1, set2)

      assert(contains(s, 2), "Intersection includes only 2")
      assert(!contains(s, 1), "Intersection should not include 1")
      assert(!contains(s, 3), "Intersection should not include 3")
    }
  }

  test("diff should contains only elements that are present in left set, but not in the right set") {

    new TestSets {

      val left: Set = union(s1, s2)
      val result: Set = diff(left, s2)

      assert(contains(result, 1), "Intersection includes only 1")
      assert(!contains(result, 2), "Intersection includes only 1")
      assert(!contains(result, 3), "Intersection includes only 1")
    }
  }

  test("filter should contains only elements that are equal 2") {

    new TestSets {

      val result: Set = filter(all, x => x == 2)

      assert(contains(result, 2), "filter includes only 1")
      assert(!contains(result, 1), "filter includes only 1")
      assert(!contains(result, 3), "filter includes only 1")
    }
  }

  test("filter should contains only elements > 1") {

    new TestSets {

      val result: Set = filter(all, x => x > 1)

      assert(!contains(result, 1), "filter includes only 2 and 3")
      assert(contains(result, 2), "filter includes only 2 and 3")
      assert(contains(result, 3), "filter includes only 2 and 3")
    }
  }

  test("forall should check if there are only positive elements") {

    new TestSets {

      val result: Boolean = forall(all, x => x > 0)
      assert(result, "All should be positive")
    }
  }

  test("exists should check if there are only 2 and <= 3") {

    new TestSets {

      var result: Boolean = exists(all, x => x == 2)
      assert(result, "Should be an element = 2")

      result = exists(all, x => x > 3)
      assert(!result, "Should not be an element > 3")
    }
  }

  test("map multiplying by 3") {

    new TestSets {
      assert(contains(map(s3, x => x * 3), 9), "map multiplying by 3 set s3")
    }
  }

  test("map subtracting 2 to set with several elements") {
    new TestSets {
      assert(contains(map(union(s1, union(s2,s3)), x => x - 2),-1), "map subtracting 2 to s1")
      assert(contains(map(union(s1, union(s2,s3)), x => x - 2),0), "map subtracting 2 to s2")
      assert(contains(map(union(s1, union(s2,s3)), x => x - 2),1), "map subtracting 2 to s3")
    }
  }

  test("map should return set multiplied by 2") {

    new TestSets {

      var result: Set = map(all, x => x * 2)

      assert(contains(result, 2), "Should be an element = 2")
      assert(contains(result, 4), "Should be an element = 4")
      assert(contains(result, 6), "Should be an element = 6")
    }
  }
}
