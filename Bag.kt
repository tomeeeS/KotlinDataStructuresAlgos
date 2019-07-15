class Bag<T> {
    
    private val valuesMap = HashMap<T, Int>()

    val cardinalities
        get() = valuesMap.values
    val keys
        get() = valuesMap.keys
    val keyCount
        get() = valuesMap.size
    val totalSize
        get() = valuesMap.values.sum()
    val most
        get() = valuesMap.entries.sortedByDescending { it.value }
                                    .map { Pair(it.key, it.value) }
                                    .first()

    fun add(value: T, pieceCount: Int = 1) {
        valuesMap[value] =
            valuesMap[value]?.
                plus(pieceCount)
            ?:
                pieceCount
    }

    fun remove(value: T, pieceCount: Int = 1) {
        valuesMap[value]?.rem(pieceCount)
    }

    fun contains(value: T) = valuesMap.containsKey(value)

    operator fun get(value: T) = valuesMap[value]
}

// prints 2
fun main() = println(
    Bag<Byte>().apply {
        add(3)
        add(4)
        add(1)
        add(1)
        add(2, 4)
        add(1)
    }.most.first
)