
import java.lang.Long.max
import java.lang.Long.min

/*
    With putting items in the matrix with setAll, we can expand it arbitrarily, we don't have to set any size
    in advance at all. See main function below for example.

    Licence: Apache-2.0 (do with it whatever you please)
 */
open class IndefiniteSizeMatrix<T>(val defaultValue: T, var columnCount: Long = 0, var rowCount: Long = 0 ) {

    protected val items: MutableMap<Pair<Long,Long>, T> = HashMap()
    protected var isIniting = true
    protected var hasOverlapped = false

    init {
        columnCount = max(columnCount,  0L)
        rowCount = max(rowCount,  0L)
        setAll(0, 0, columnCount - 1, rowCount - 1, defaultValue) { defaultValue }
        isIniting = false
    }

    // expand the matrix if necessary, set the elements in the specified submatrix to value, the rest to the defaultValue,
    // and if there's an overlap (there was already an element there (that wasn't default), execute onOverlap lambda
    // and put its result in place.
    // the params are indices. they're inclusive.
    // returns: has any overlap occurred with previous values (that weren't default)?
    open fun setAll(fromColumn: Long
               , fromRow: Long
               , toColumn: Long
               , toRow: Long
               , valueForNewCell: T
               , onOverlap: (T) -> T
    ) : Boolean {
        val fromColumn = max(fromColumn,  0L)
        val fromRow = max(fromRow,  0L)

        fun setAndExpand() {
            for (i in 0 until rowCount)
                for (j in columnCount..toColumn)
                    setItemInternal(i, j, fromColumn, toColumn, fromRow, toRow, valueForNewCell, onOverlap)
            columnCount = max(toColumn + 1, columnCount)
            for (i in rowCount..toRow)
                for (j in 0 until columnCount)
                    setItemInternal(i, j, fromColumn, toColumn, fromRow, toRow, valueForNewCell, onOverlap)
            rowCount = max(toRow + 1, rowCount)
        }

        fun setWithoutExpand() {
            for (i in fromRow..min((rowCount - 1), toRow))
                for (j in fromColumn..min((columnCount - 1), toColumn))
                    setItemInternal(i, j, fromColumn, toColumn, fromRow, toRow, valueForNewCell, onOverlap)
        }

        fun expandIfZeroSized() {
            if (rowCount == 0L || columnCount == 0L && (toRow > 0 && toColumn > 0)) {
                items[Pair(0L, 0L)] = valueForNewCell
                rowCount = 1
                columnCount = 1
            }
        }

        fun considerIniting() {
            if (isIniting) {
                columnCount = 0
                rowCount = 0
            }
        }

        hasOverlapped = false
        considerIniting()
        setWithoutExpand()
        expandIfZeroSized()
        setAndExpand()
        return hasOverlapped
    }

    protected open fun setItemInternal(
        i: Long
        , j: Long
        , fromColumn: Long
        , toColumn: Long
        , fromRow: Long
        , toRow: Long
        , valueForNewCell: T
        , onOverlap: (T) -> T
    ) {
        items[Pair(i, j)] =
            if (j in fromColumn..toColumn && i in fromRow..toRow)
                items[Pair(i, j)]
                    ?.let (
                        if (items[Pair(i, j)] != defaultValue)
                            onOverlap.also { hasOverlapped = true }
                        else
                            { _ -> valueForNewCell })
                    ?: valueForNewCell
            else
                defaultValue
    }

    open fun setItem(
        rowIndex: Long
        , columnIndex: Long
        , valueForNewCell: T
        , onOverlap: (T) -> T
    ) = setAll(columnIndex, rowIndex, columnIndex, rowIndex, valueForNewCell, onOverlap)

    operator fun get(i: Long, j: Long) =  items[Pair(i, j)]!!

    // have to expand first with setAll
    operator fun set(i: Long, j: Long, t: T) {
        items[Pair(i, j)] = t
    }

    open fun countValue(value: T) = items.values.count {  it == value }

    open fun onEach(f: (T) -> Unit) {
        items.values.onEach { f(it) }
    }

    open fun onEachIndexed(f: (T, Long, Long) -> Unit) {
        items.onEach { f(it.value, it.key.component1(), it.key.component2() ) }
    }

    override fun toString(): String {
        val sb = StringBuilder()
        sb.append("[ ")
        for (i in 0 until rowCount) {
            if(i > 0L)
                sb.append("  ")
            for (j in 0 until columnCount) {
                if(j > 0L)
                    sb.append(", ")
                sb.append(items[Pair(i, j)])
            }
            if(i < rowCount - 1)
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
    matrix.setItem(4, 5, 1) { 2 }
    println(matrix)
}