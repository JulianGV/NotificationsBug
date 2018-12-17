package com.globant.notificarionbug

fun String.capitalizeAllWords(): String {
    if (this.isEmpty()) return ""
    val sb = StringBuilder()
    var newWord: Boolean = true
    for (charInString in this) {
        if (!charInString.isLetter() && !charInString.isWhitespace())
            sb.append(charInString)
        else if (newWord)
            sb.append(charInString.toUpperCase())
        else
            sb.append(charInString.toLowerCase())
        newWord = charInString.isWhitespace()
    }
    return sb.toString()
}