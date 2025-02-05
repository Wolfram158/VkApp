package android.learn.vkapp

import kotlinx.coroutines.delay

class Node(val left: Node?, val right: Node?, val value: Char) {
    var chars = mutableSetOf<Char>()
}

class Solver {
    val set = mutableSetOf<Set<Char>>()

    private fun isExist(root: Node): Boolean {
        if (set.contains(root.chars)) {
            return true
        }
        set.add(root.chars)
        if (root.left != null) {
            if (root.right != null) {
                return isExist(root.left) || isExist(root.right)
            }
            return isExist(root.left)
        } else {
            if (root.right != null) {
                return isExist(root.right)
            }
            return false
        }
    }

    private fun countSets(root: Node) {
        root.left?.let { countSets(it) }
        root.right?.let { countSets(it) }
        val set3 = mutableSetOf(root.value)
        root.left?.chars?.let { set3.addAll(it) }
        root.right?.chars?.let { set3.addAll(it) }
        root.chars = set3
    }

    fun solve(root: Node): Boolean {
        countSets(root)
        return isExist(root)
    }
}

fun main() {
//    val solver = Solver()
//    val tree = Node(
//        Node(
//            Node(null, null, 'H'),
//            Node(null, null, 'G'), 'D'
//        ),
//        Node(
//            Node(null, null, 'D'),
//            Node(null, Node(null, null, 'D'), 'G'), 'H'
//        ), 'F'
//    )
//    println(solver.solve(tree))
    val y: C<Int> = C(1, 2)
    val x: C<Any> = y
    x.y = "xxx"
    //print(y.y)

    val z: D<Any> = D(1, 2);
    z.x = "x"
    println(z as D<Int>)
}

class D<T>(var x: T, val y: T)

class C<out T>(val x: T, var y: @UnsafeVariance T) {
}

open class A

class B : A()

suspend fun g(): Int {
    val y = 3
    delay(500)
    return y * y
}

suspend fun f(): Int {
    val x = 3
    val y = g()
    return x + y
}