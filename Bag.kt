class Bag<T> {
    private val values = HashMap<T, Int>()
    var size = 0
        private set

    fun add(value: T) {
        if (values[value] == null)
            ++size
        values[value] = values[value]?.plus(1) ?: 1
    }

    fun hasCardinality(cardinality: Int) = values.containsValue(cardinality)

    fun contains(value: T) = values.contains(value)

    operator fun get(value: T) = values[value]
}
