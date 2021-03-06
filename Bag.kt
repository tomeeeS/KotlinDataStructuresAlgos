import java.lang.Integer.min

/*
 warning: not concurrent
 
 note: does not extend HashMap<T, Int>, because fun remove(key: T) would clash with HashMap's.
 here it does not mean remove the key-value pair completely, but just remove 1 of that key,
 there might be more of that key left in the container.
 */
open class Bag<T> {
    
    protected val valuesMap = HashMap<T, Int>()

    val cardinalities
        get() = valuesMap.values
    val keys // keys in the bag (without their cardinalities)
        get() = valuesMap.keys
    val keyCount
        get() = valuesMap.size
    val totalSize
        get() = valuesMap.values.sum()
    val most
        get() = valuesMap.entries.maxByOrNull { it.value }!!.toPair()

    fun add(key: T, pieceCount: Int = 1) {
        valuesMap[key] =
            valuesMap[key]?.
                plus(pieceCount)
            ?:
                pieceCount
    }

    // safe remove
    // if the key is in the bag, we remove pieceCount pieces from that key.
    // if there's not that much pieces from that key, we remove them all.
    fun remove(key: T, pieceCount: Int = 1) {
        if (contains(key)) {
            val newValue = valuesMap[key]!! - pieceCount
            valuesMap[key] = newValue
            if (newValue <= 0)
                valuesMap.remove(key) }
    }
 
    fun removeAll(key: T) {
        valuesMap.remove(key)
    }

    fun contains(value: T) = valuesMap.containsKey(value)

    operator fun get(value: T) = valuesMap[value]

    fun clear() { valuesMap.clear() }
}

// prints 1
fun main() = println(
    Bag<Byte>().apply {
        add(3)
        add(4)
        add(1)
        add(1)
        add(2, 4)
        add(1)
        remove(2, 4)
    }.most.first
)
