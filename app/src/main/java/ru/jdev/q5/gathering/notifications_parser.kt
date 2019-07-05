package ru.jdev.q5.gathering

const val split = "%@~"

val patterns = arrayOf(
        CheckPattern("(.*)$split([\\d, ]*) .*с карты.*".toRegex(), 2, 1), // Google Pay
        CheckPattern("Кукуруза$split-([\\d. ]*) RUR\\s(.*.) Остаток.*".toRegex(), 1, 2),  // Kukuruza
        CheckPattern("Уведомление$split.*Summa: ([\\d, ]*) .* RUR (.*) \\d{2}\\..*".toRegex(), 1, 2), // Alfa
        CheckPattern("(Перевод|Покупка) (.*)%@~([\\d, ]*) .*".toRegex(), 3, 2) // Sber
)

fun parseNotification(title: String, text: String): Check? {
    val ft = "$title$split$text"
    return patterns.map { it to it.pattern.matchEntire(ft) }
            .firstOrNull() { it.second != null }
            ?.let { (cp, mr) -> mr?.let { Check(it.groups[cp.sumGroupIdx]!!.value, it.groups[cp.placeGroupIdx]!!.value, "$title\n$text") } }
}

data class CheckPattern(val pattern: Regex, val sumGroupIdx: Int, val placeGroupIdx: Int)