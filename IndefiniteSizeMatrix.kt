
import java.lang.Long.max
import java.lang.Long.min

/*
    With putting items in the matrix with setAll, we can expand it arbitrarily, we don't have to set any size
    in advance at all. See main function below for example.

    Licence: Apache-2.0 (do with it whatever you please)
 */
class IndefiniteSizeMatrix<T>(private val defaultValue: T, var width: Long = 0, var height: Long = 0 ) {

    private val items: MutableMap<Pair<Long,Long>, T> = HashMap()
    private var isIniting = true
    private var hasOverlapped = false

    init {
        width = max(width,  0L)
        height = max(height,  0L)
        setAll(0, 0, width - 1, height - 1, defaultValue) { defaultValue }
        isIniting = false
    }

    // expand the matrix if necessary, set the elements in the specified submatrix to value, the rest to the defaultValue,
    // and if there's an overlap (there was already an element there (that wasn't default), execute onOverlap lambda
    // and put its result in place.
    // the params are indices. they're inclusive.
    // returns: has any overlap occurred with previous values (that weren't default)?
    fun setAll(fromWidth: Long
               , fromHeight: Long
               , toWidth: Long
               , toHeight: Long
               , valueForNewCell: T
               , onOverlap: (T) -> T
    ) : Boolean {
        hasOverlapped = false
        considerIniting()
        val fromWidth = max(fromWidth,  0L)
        val fromHeight = max(fromHeight,  0L)
        setWithoutExpand(fromHeight, toHeight, fromWidth, toWidth, onOverlap, valueForNewCell)
        expandIfZeroSized(toHeight, toWidth, valueForNewCell)
        setAndExpand(toWidth, fromWidth, fromHeight, toHeight, onOverlap, valueForNewCell)
        return hasOverlapped
    }

    private fun setAndExpand(
        toWidth: Long,
        fromWidth: Long,
        fromHeight: Long,
        toHeight: Long,
        onOverlap: (T) -> T,
        valueForNewCell: T
    ) {
        for (h in 0 until height)
            for (w in width..toWidth)
                setItem(h, w, fromWidth, toWidth, fromHeight, toHeight, onOverlap, valueForNewCell)
        width = max(toWidth + 1, width)
        for (h in height..toHeight)
            for (w in 0 until width)
                setItem(h, w, fromWidth, toWidth, fromHeight, toHeight, onOverlap, valueForNewCell)
        height = max(toHeight + 1, height)
    }

    private fun setWithoutExpand(
        fromHeight: Long,
        toHeight: Long,
        fromWidth: Long,
        toWidth: Long,
        onOverlap: (T) -> T,
        valueForNewCell: T
    ) {
        for (h in fromHeight..min((height - 1), toHeight))
            for (w in fromWidth..min((width - 1), toWidth))
                setItem(h, w, fromWidth, toWidth, fromHeight, toHeight, onOverlap, valueForNewCell)
    }

    private fun expandIfZeroSized(toHeight: Long, toWidth: Long, valueForNewCell: T) {
        if (height == 0L || width == 0L && (toHeight > 0 && toWidth > 0)) {
            items[Pair(0L, 0L)] = valueForNewCell
            height = 1
            width = 1
        }
    }

    private fun considerIniting() {
        if (isIniting) {
            width = 0
            height = 0
        }
    }

    private fun setItem(
        h: Long,
        w: Long,
        fromWidth: Long,
        toWidth: Long,
        fromHeight: Long,
        toHeight: Long,
        onOverlap: (T) -> T,
        valueForNewCell: T
    ) {
        items[Pair(h, w)] =
            if (w in fromWidth..toWidth && h in fromHeight..toHeight)
                items[Pair(h, w)]
                    ?.let (
                        if (items[Pair(h, w)] != defaultValue)
                            onOverlap.also { hasOverlapped = true }
                        else
                            { _ -> valueForNewCell })
                    ?: valueForNewCell
            else
                defaultValue
    }

    operator fun get(h: Long, w: Long) =  items[Pair(h, w)]!!

    // have to expand first with setAll
    operator fun set(h: Long, w: Long, t: T) {
        items[Pair(h, w)] = t
    }

    fun countValue(value: T) = items.values.count {  it == value }

    fun onEach(f: (T) -> Unit) {
        items.values.onEach { f(it) }
    }

    fun onEachIndexed(f: (T, Long, Long) -> Unit) {
        items.onEach { f(it.value, it.key.component1(), it.key.component2() ) }
    }

    override fun toString(): String {
        val sb = StringBuilder()
        sb.append("[ ")
        for (h in 0 until height) {
            if(h > 0L)
                sb.append("  ")
            for (w in 0 until width) {
                if(w > 0L)
                    sb.append(", ")
                sb.append(items[Pair(h, w)])
            }
            if(h < height - 1)
                sb.append("\n")
        }
        sb.append(" ]")
        return sb.toString()
    }
}

fun main() {
    val matrix = IndefiniteSizeMatrix(0, 2, 2)
    matrix.setAll(1, 1, 2, 2, 1) { 2 }
    matrix.setAll(2, 2, 3, 3, 1) { 2 }
    println(matrix)
}