package pl.edu.pollub.parser.domain

import java.util.regex.Matcher

interface TagMatcher {

    fun isFound(): Boolean

    fun isNotFound(): Boolean

    fun getTagValue(): String

}

fun Matcher.isNotFount() = !find()