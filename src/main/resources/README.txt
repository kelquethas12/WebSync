```
        val testLogs = EveryLogs(
            "db", "table", mutableListOf(
                EveryObjects(
                    column_name = "player",
                    value = "username"
                ),
                (
                        EveryObjects(
                            column_name = "item",
                            value = 55
                        )
                        ),
                (
                        EveryObjects(
                            column_name = "location",
                            value = "xzy"
                        )
                        )
            )
        )

        logs.send(testLogs, 172800000)```