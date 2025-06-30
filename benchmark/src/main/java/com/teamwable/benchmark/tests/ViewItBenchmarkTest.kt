package com.teamwable.benchmark.tests

import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.teamwable.benchmark.BenchmarkConfig
import com.teamwable.benchmark.CommonScenarios
import com.teamwable.benchmark.PerformanceBenchmark
import com.teamwable.benchmark.PerformanceMetrics
import com.teamwable.benchmark.targets.BenchmarkTargets
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class ViewItBenchmarkTest : PerformanceBenchmark() {
    @get:Rule
    override val benchmarkRule = MacrobenchmarkRule()

    override val config = BenchmarkConfig(
        iterations = 3,
        metrics = PerformanceMetrics.CORE_PERFORMANCE,
    )

    @Test
    fun viewItXmlTest() {
        runPerformanceTest(
            targets = listOf(
                BenchmarkTargets.xmlViewItScreen,
            ),
            scenario = CommonScenarios.BasicInteraction(),
            customConfig = config,
        )
    }

    @Test
    fun viewItComposeTest() {
        runPerformanceTest(
            targets = listOf(
                BenchmarkTargets.composeViewItScreen,
            ),
            scenario = CommonScenarios.BasicInteraction(),
            customConfig = config,
        )
    }

    @Test
    fun viewItComposeScrollPerformance() {
        runPerformanceTest(
            targets = listOf(
                BenchmarkTargets.composeViewItScreen,
            ),
            scenario = CommonScenarios.ScrollTest(),
            customConfig = config.copy(
                metrics = PerformanceMetrics.FRAME_ONLY,
                iterations = 3,
            ),
        )
    }

    @Test
    fun viewItXmlScrollPerformance() {
        runPerformanceTest(
            targets = listOf(
                BenchmarkTargets.xmlViewItScreen,
            ),
            scenario = CommonScenarios.ScrollTest(),
            customConfig = config.copy(
                metrics = PerformanceMetrics.FRAME_ONLY,
                iterations = 3,
            ),
        )
    }
}
