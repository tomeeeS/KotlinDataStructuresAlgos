import java.lang.Integer.min

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
        get() = valuesMap.entries.sortedByDescending { it.value }
                                    .map { Pair(it.key, it.value) }
                                    .first()

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
        valuesMap[key]?.let { it.rem(min(pieceCount, it)) }
            .also { if (it == 0) valuesMap.remove(key) }
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