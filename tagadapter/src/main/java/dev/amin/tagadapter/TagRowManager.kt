package dev.amin.tagadapter

class TagRowManager {

    /***
     * List of all the rows inside the row manager.
     */
    private val rowList = ArrayList<TagRow>()
        .apply {

            // Manually add the first empty row.
            add(TagRow())
        }

    /***
     * Loop through the rowList to fit the required span
     */
    fun add(spanRequired: Float, tag: Tag) {

        for (i in 0..rowList.size) {

            val tagRow = rowList[i]

            // If the title was added and was fit to the list in dreamTitleRow
            if (tagRow.addTag(spanRequired, tag))
                break

            // If the model did not fit in any of current cells
            if (i == rowList.lastIndex)
                rowList.add(TagRow())
        }
    }

    fun getSortedSpans() =
        ArrayList<Int>().apply {
            rowList.forEach {
                addAll(it.spanList)
            }
        }

    fun getSortedTags() =
        ArrayList<Tag>().apply {
            rowList.forEach {
                addAll(it.tagList)
            }
        }
}