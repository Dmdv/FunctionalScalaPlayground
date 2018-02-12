import funsets.FunSets._

val s1 = singletonSet(1)
val s2 = singletonSet(2)
val s3 = singletonSet(3)

def func = (x: Int) => x * 2

val mapped: Set = map(s2, func)

printSet(mapped)

exists(s2, _ => func == 4)

