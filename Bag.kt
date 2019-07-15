import java.lang.Integer.min

class Bag<T> {
    
    private val valuesMap = HashMap<T, Int>()

    val cardinalities
        get() = valuesMap.values
    val items // items or keys in the bag (without their cardinalities)
        get() = valuesMap.keys
    val keyCount
        get() = valuesMap.size
    val totalSize
        get() = valuesMap.values.sum()
    val most
        get() = valuesMap.entries.sortedByDescending { it.value }
                                    .map { Pair(it.key, it.value) }
                                    .first()

    fun add(item: T, pieceCount: Int = 1) {
        valuesMap[item] =
            valuesMap[item]?.
                plus(pieceCount)
            ?:
                pieceCount
    }

    // safe remove
    // if the item is in the bag, we remove pieceCount pieces from that item.
    // if there's not that much pieces from that item, we remove them all.
    fun remove(item: T, pieceCount: Int = 1) {
        valuesMap[item]?.let { it.rem(min(pieceCount, it)) }
            .also { if (it == 0) valuesMap.remove(item) }
    }

    fun contains(value: T) = valuesMap.containsKey(value)

    operator fun get(value: T) = valuesMap[value]
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