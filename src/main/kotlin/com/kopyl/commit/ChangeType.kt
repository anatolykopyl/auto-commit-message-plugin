package com.kopyl.commit

import java.util.*

enum class ChangeType {
    FEAT,
    FIX,
    DOCS,
    STYLE,
    REFACTOR,
    PERF,
    TEST,
    BUILD,
    CI,
    CHORE,
    REVERT;

    fun label(): String {
        return name.lowercase(Locale.getDefault())
    }
}
