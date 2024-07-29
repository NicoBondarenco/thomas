package com.thomas.hash.bouncycastle.properties

internal val SALT_CHAR: List<String> = listOf(
    "0123456789",
    "ABCDEFGHIJKLMNOPQRSTUVWXYZ",
    "abcdefghijklmnopqrstuvwxyz",
    "\"'!¹@²#³$£%¢¨¬&*()_+-=§",
    "`´[]{}ªº^~,.<>;:/?|\\",
    "aàáâãäạảăắặằẳẵấậầẩẫåāą",
    "cçčćĉċdđðďeèéêëẹẻẽếệềểễēĕėęě",
    "gĝğġģhĥħiìíîïịỉĩīĭį̇ıjĵkķĸ",
    "lĺļľŀłnñńņňŉŋoòóôõöọỏốộồổỗơớợờởỡøōŏő",
    "ŕŗřsšśŝştţťŧuùúûüụủũưứựừửữŭūůűų",
    "wŵyýÿỵỳỷỹŷzžźż",
    "AÀÁÂÃÄẠẢĂẮẶẰẲẴẤẬẦẨẪÅĀĄ",
    "CÇČĆĈĊDĐÐĎEÈÉÊËẸẺẼẾỆỀỂỄĒĔĖĘĚ",
    "GĜĞĠĢHĤĦIÌÍÎÏỊỈĨĪĬĮ̇IJĴKĶĸ",
    "LĹĻĽĿŁNÑŃŅŇNŊOÒÓÔÕÖỌỎỐỘỒỔỖƠỚỢỜỞỠØŌŎŐ",
    "ŔŖŘSŠŚŜŞTŢŤŦUÙÚÛÜỤỦŨƯỨỰỪỬỮŬŪŮŰŲ",
    "WŴYÝŸỴỲỶỸŶZŽŹŻ",
).joinToString("").map { it.toString() }