package ru.freestyle.websync.models

data class EveryLogs(
    val database: String,
    val table: String,
    val objects: List<EveryObjects>
)

data class EveryObjects(
    val column_name: String,
    val value: Any
)