class Bag<T : Comparable<T>> {
    private val valuesMap = HashMap<T, Int>()

    val cardinalities = valuesMap.values
    val keys = valuesMap.keys
    val keyCount = valuesMap.size
    val totalSize = valuesMap.values.sum()
    val most = valuesMap.toSortedMap().map { Pair(it.key, it.value) }.first()

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
