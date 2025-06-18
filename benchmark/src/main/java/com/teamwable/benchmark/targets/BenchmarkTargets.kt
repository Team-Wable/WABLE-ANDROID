package com.teamwable.benchmark.targets

import com.teamwable.benchmark.BenchmarkTarget
import com.teamwable.benchmark.WaitCondition
import com.teamwable.benchmark.WaitType

object BenchmarkTargets {
    val xmlViewItScreen = object : BenchmarkTarget {
        override val name = "ViewIt XML"
        override val packageName = "com.teamwable.wable"
        override val activityClassName = "com.teamwable.viewit.benchmark.BenchmarkXmlEntryActivity"
        override val waitConditions = listOf(
            WaitCondition(WaitType.SCROLLABLE, "", 3000),
        )
    }

    val composeViewItScreen = object : BenchmarkTarget {
        override val name = "ViewIt Compose Screen"
        override val packageName = "com.teamwable.wable"
        override val activityClassName = "com.teamwable.viewit.benchmark.BenchmarkComposeEntryActivity"
        override val waitConditions = listOf(
            WaitCondition(WaitType.SCROLLABLE, "", 3000),
        )
    }
}
