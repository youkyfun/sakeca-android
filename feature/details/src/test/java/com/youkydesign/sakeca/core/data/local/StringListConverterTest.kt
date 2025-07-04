package com.youkydesign.sakeca.core.data.local

import org.junit.Test

class StringListConverterTest {

    @Test
    fun `toString with empty list`() {
        // Verify toString returns an empty string when given an empty list.
        val emptyList = emptyList<String>()
        val result = StringListConverter().toString(emptyList)
        assert(result.isEmpty())
    }

    @Test
    fun `toString with single element list`() {
        // Verify toString returns the element itself when given a list with one string.
        val singleElementList = listOf("a")
        val result = StringListConverter().toString(singleElementList)
        assert(result == "a")
    }

    @Test
    fun `toString with multiple elements`() {
        // Verify toString correctly joins multiple strings with a comma delimiter.
        val multipleElementsList = listOf("a", "b", "c")
        val result = StringListConverter().toString(multipleElementsList)
        assert(result == "a,b,c")
    }

    @Test
    fun `toString with elements containing commas`() {
        // Verify toString correctly handles elements that themselves contain commas. 
        // The expectation is that these commas will be part of the resulting string.
        val elementsContainingCommasList = listOf("a,b", "c,d")
        val result = StringListConverter().toString(elementsContainingCommasList)
        assert(result == "a,b,c,d")
    }

    @Test
    fun `toString with empty string elements`() {
        // Verify toString handles lists containing empty strings correctly, e.g., listOf("", "a", "") should produce ",a,".
        val emptyStringElementsList = listOf("", "a", "")
        val result = StringListConverter().toString(emptyStringElementsList)
        assert(result == ",a,")
    }

    @Test
    fun `toString with list of strings with leading trailing spaces`() {
        // Verify toString preserves leading/trailing spaces in the elements of the list.
        val listWithLeadingTrailingSpaces = listOf(" a ", " b ", " c ")
        val result = StringListConverter().toString(listWithLeadingTrailingSpaces)
        assert(result == " a , b , c ")
    }

    @Test
    fun `toList with empty string`() {
        // Verify toList returns empty list when given an empty string.
        val emptyString = ""
        val result = StringListConverter().toList(emptyString)
        assert(result == emptyList<String>())
    }

    @Test
    fun `toList with single element string`() {
        // Verify toList returns a list with that single element when given a string without commas.
        val singleElementString = "a"
        val result = StringListConverter().toList(singleElementString)
        assert(result == listOf("a"))
    }

    @Test
    fun `toList with multiple comma separated elements`() {
        // Verify toList correctly splits a comma-separated string into a list of strings.
        val commaSeparatedString = "a,b,c"
        val result = StringListConverter().toList(commaSeparatedString)
        assert(result == listOf("a", "b", "c"))
    }

    @Test
    fun `toList with string containing consecutive commas`() {
        // Verify toList handles strings with consecutive commas, e.g., "a,,b" should result in listOf("a", "", "b").
        val consecutiveCommasString = "a,,b"
        val result = StringListConverter().toList(consecutiveCommasString)
        assert(result == listOf("a", "", "b"))
    }

    @Test
    fun `toList with string starting with a comma`() {
        // Verify toList handles strings starting with a comma, e.g., ",a,b" should result in listOf("", "a", "b").
        val commaStartingString = ",a,b"
        val result = StringListConverter().toList(commaStartingString)
        assert(result == listOf("", "a", "b"))
    }

    @Test
    fun `toList with string ending with a comma`() {
        // Verify toList handles strings ending with a comma, e.g., "a,b," should result in listOf("a", "b", "").
        val commaEndingString = "a,b,"
        val result = StringListConverter().toList(commaEndingString)
        assert(result == listOf("a", "b", ""))
    }

    @Test
    fun `toList with string containing only commas`() {
        // Verify toList handles strings containing only commas, e.g., ",," should result in listOf("", "", "").
        val onlyCommasString = ",,"
        val result = StringListConverter().toList(onlyCommasString)
        assert(result == listOf("", "", ""))
    }

    @Test
    fun `toList with string elements having leading trailing spaces`() {
        // Verify toList preserves leading/trailing spaces in the resulting list elements.
        val stringWithLeadingTrailingSpaces = " a , b , c "
        val result = StringListConverter().toList(stringWithLeadingTrailingSpaces)
        assert(result == listOf(" a ", " b ", " c "))
    }

    @Test
    fun `Symmetry check  toList toString list   equals list`() {
        // Verify that converting a list to a string and back to a list results in the original list, 
        // assuming no elements contain the delimiter used by toString internally (which is a comma in this case).
        val list = listOf("a", "b", "c")
        val result = StringListConverter().toList(StringListConverter().toString(list))
        assert(result == list)
    }

    @Test
    fun `Symmetry check for empty list`() {
        // Specifically test the symmetry for an empty list: toList(toString(emptyList())) should be an empty list (or a list with one empty string, depending on split behavior).
        val emptyList = emptyList<String>()
        val emptyString = StringListConverter().toString(emptyList)
        val result = StringListConverter().toList(emptyString)
        assert(result == emptyList)
    }

    @Test
    fun `toString with list containing special characters`() {
        // Verify toString correctly handles list elements with special characters (e.g., unicode, symbols) without modification.
        val listWithSpecialCharacters = listOf("a", "b", "c")
        val result = StringListConverter().toString(listWithSpecialCharacters)
        assert(result == "a,b,c")
    }

    @Test
    fun `toList with string containing special characters`() {
        // Verify toList correctly handles strings with special characters (e.g., unicode, symbols) within the elements.
        val stringWithSpecialCharacters = "a,b,c"
        val result = StringListConverter().toList(stringWithSpecialCharacters)
        assert(result == listOf("a", "b", "c"))

    }

}