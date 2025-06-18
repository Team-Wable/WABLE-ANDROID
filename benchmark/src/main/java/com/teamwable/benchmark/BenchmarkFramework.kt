package com.teamwable.benchmark

import android.content.Intent
import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.ExperimentalMetricApi
import androidx.benchmark.macro.FrameTimingMetric
import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.benchmark.macro.MemoryUsageMetric
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Until

/**
 * 벤치마크 테스트 대상을 정의하는 인터페이스입니다.
 * 성능을 측정할 특정 Activity나 화면을 나타냅니다.
 * ### 구현 예시
 * ```kotlin
 * object Targets {
 *     val composeViewItScreen = object : BenchmarkTarget {
 *         override val name = "ViewIt Compose"
 *         override val packageName = "com.teamwable.wable"
 *         override val activityClassName = "com.teamwable.viewit.ComposeViewItActivity"
 *         override val waitConditions = listOf(
 *             WaitCondition(WaitType.SCROLLABLE, "", 3000)
 *         )
 *     }
 * }
 * ```
 */
interface BenchmarkTarget {
    val name: String
    val packageName: String
    val activityClassName: String
    val waitConditions: List<WaitCondition>

    fun createIntent(): Intent = Intent().apply {
        setClassName(packageName, activityClassName)
    }
}

/**
 * @param type 대기할 요소의 유형
 * @param selector 요소를 찾기 위한 선택자 (리소스 ID, 텍스트 등)
 * @param timeout 대기 시간 제한 (밀리초)
 */
data class WaitCondition(
    val type: WaitType,
    val selector: String,
    val timeout: Long = 5000L,
)

enum class WaitType {
    RESOURCE_ID,
    TEXT,
    SCROLLABLE,
    CLICKABLE,
}

interface BenchmarkScenario {
    val name: String

    fun execute(scope: MacrobenchmarkScope)
}

object CommonScenarios {
    /**
     * 기본적인 터치 상호작용을 시뮬레이션하는 시나리오
     *
     * ### 사용 예시
     * ```kotlin
     * @Test
     * fun basicInteractionTest() {
     *     runPerformanceTest(
     *         target = MyTargets.composeScreen,
     *         scenario = CommonScenarios.BasicInteraction() // 기본 파라미터 사용
     *     )
     * }
     *
     * @Test
     * fun customInteractionTest() {
     *     runPerformanceTest(
     *         target = MyTargets.composeScreen,
     *         scenario = CommonScenarios.BasicInteraction(
     *             clickCount = 10,  // 더 많은 터치
     *             delayMs = 200     // 더 빠른 간격
     *         )
     *     )
     * }
     * ```
     *
     * @param clickCount 터치 횟수 (기본값: 5)
     * @param delayMs 터치 간격 밀리초 (기본값: 300)
     */
    class BasicInteraction(
        private val clickCount: Int = 5,
        private val delayMs: Long = 300,
    ) : BenchmarkScenario {
        override val name = "BasicInteraction"

        override fun execute(scope: MacrobenchmarkScope) {
            with(scope) {
                device.waitForIdle()
                repeat(clickCount) {
                    device.click(device.displayWidth / 2, device.displayHeight / 2)
                    device.waitForIdle()
                    Thread.sleep(delayMs)
                }
            }
        }
    }

    /**
     * 스크롤 성능을 테스트하는 시나리오
     *
     * ## 테스트 패턴
     * 1. 일반 스크롤: 일정한 속도로 위아래 스크롤하여 프레임 성능 측정
     * 2. Fling 스크롤: 빠른 스와이프로 관성 스크롤하여 집중적인 렌더링 성능 측정
     *
     * ### 사용 예시
     * ```kotlin
     * @Test
     * fun scrollPerformanceComparison() {
     *     runComparison(
     *         targets = listOf(xmlScreen, composeScreen),
     *         scenario = CommonScenarios.ScrollTest() // 기본 파라미터로 스크롤 테스트
     *     )
     * }
     *
     * @Test
     * fun intensiveScrollTest() {
     *     runPerformanceTest(
     *         target = composeScreen,
     *         scenario = CommonScenarios.ScrollTest(
     *             scrollCount = 20,    // 더 많은 스크롤
     *             scrollRatio = 0.8f,  // 더 큰 스크롤 거리
     *             flingCount = 5       // 더 많은 fling
     *         )
     *     )
     * }
     * ```
     *
     * @param scrollCount 일반 스크롤 횟수 (기본값: 10)
     * @param scrollRatio 스크롤 이동 비율 0.0~1.0 (기본값: 0.6)
     * @param flingCount Fling 스크롤 횟수 (기본값: 3)
     */
    class ScrollTest(
        private val scrollCount: Int = 10,
        private val scrollRatio: Float = 0.6f,
        private val flingCount: Int = 3,
    ) : BenchmarkScenario {
        override val name = "ScrollTest"

        override fun execute(scope: MacrobenchmarkScope) {
            with(scope) {
                device.waitForIdle()
                Thread.sleep(1000)

                val displayHeight = device.displayHeight
                val displayWidth = device.displayWidth
                val centerX = displayWidth / 2

                repeat(10) {
                    device.swipe(
                        centerX, displayHeight * 2 / 3,
                        centerX, displayHeight / 3,
                        50,
                    )
                    device.waitForIdle()
                    Thread.sleep(300)

                    device.swipe(
                        centerX, displayHeight / 3,
                        centerX, displayHeight * 2 / 3,
                        50,
                    )
                    device.waitForIdle()
                    Thread.sleep(300)
                }
            }
        }
    }
}

@OptIn(ExperimentalMetricApi::class)
enum class PerformanceMetrics(val metrics: List<androidx.benchmark.macro.Metric>) {
    /**
     * - FrameTimingMetric: CPU 프레임 처리 시간, 렌더링 지연 시간, jank 등
     * - MemoryUsageMetric: 메모리 사용량
     */
    CORE_PERFORMANCE(
        listOf(
            FrameTimingMetric(),
            MemoryUsageMetric(MemoryUsageMetric.Mode.Max),
        ),
    ),

    FRAME_ONLY(listOf(FrameTimingMetric())),
    MEMORY_ONLY(listOf(MemoryUsageMetric(MemoryUsageMetric.Mode.Max))),
}

/**
 * @param iterations 벤치마크 반복 실행 횟수 (더 많을수록 정확하지만 시간 소요, 권장: 15-20)
 * @param startupMode 앱 시작 모드 (COLD: 완전 재시작, WARM: 프로세스 재사용)
 * @param compilationMode 앱 컴파일 모드 (성능에 영향을 주는 JIT 컴파일 설정)
 * @param metrics 측정할 성능 지표 조합
 * @param setupDelayMs 각 테스트 시작 전 환경 안정화 대기 시간
 * @param cleanupDelayMs 각 테스트 종료 후 정리 대기 시간
 */
data class BenchmarkConfig(
    val iterations: Int = 15,
    val startupMode: StartupMode = StartupMode.WARM,
    val compilationMode: CompilationMode = CompilationMode.DEFAULT,
    val metrics: PerformanceMetrics = PerformanceMetrics.CORE_PERFORMANCE,
    val setupDelayMs: Long = 5000,
    val cleanupDelayMs: Long = 3000,
)

abstract class PerformanceBenchmark {
    abstract val benchmarkRule: MacrobenchmarkRule
    abstract val config: BenchmarkConfig

    /**
     * 화면의 성능을 측정합니다.
     *
     * ### 사용 예시
     * ```kotlin
     * @Test
     * fun xmlVsComposeScrollComparison() {
     *     runComparison(
     *         targets = listOf(
     *             MyTargets.xmlScreen,
     *             MyTargets.composeScreen
     *         ),
     *         scenario = CommonScenarios.ScrollTest()
     *     )
     * }
     *
     * ```
     *
     * @param targets 대상들의 리스트 (단일 혹은 복수 비교 가능)
     * @param scenario 모든 대상에 적용할 동일한 시나리오
     * @param customConfig 이 테스트에만 적용할 커스텀 설정
     */
    protected fun runPerformanceTest(
        targets: List<BenchmarkTarget>,
        scenario: BenchmarkScenario,
        customConfig: BenchmarkConfig? = null,
    ) {
        require(targets.isNotEmpty()) { "At least one target is required" }

        val actualConfig = customConfig ?: config
        targets.forEach { target ->
            benchmarkRule.measureRepeated(
                packageName = target.packageName,
                metrics = actualConfig.metrics.metrics,
                iterations = actualConfig.iterations,
                startupMode = actualConfig.startupMode,
                compilationMode = actualConfig.compilationMode,
                setupBlock = {
                    device.executeShellCommand("pkill perfetto")
                    device.executeShellCommand("am force-stop ${target.packageName}")
                    pressHome()
                    device.waitForIdle()
                    Thread.sleep(actualConfig.setupDelayMs)
                },
            ) {
                startTargetActivity(target)
                waitForTargetToLoad(target)
                scenario.execute(this)

                pressHome()
                device.waitForIdle()
                Thread.sleep(actualConfig.cleanupDelayMs)
            }
        }
    }

    private fun MacrobenchmarkScope.startTargetActivity(target: BenchmarkTarget) {
        startActivityAndWait(target.createIntent())
        device.waitForIdle()
    }

    private fun MacrobenchmarkScope.waitForTargetToLoad(target: BenchmarkTarget) {
        target.waitConditions.forEach { condition ->
            when (condition.type) {
                WaitType.RESOURCE_ID -> {
                    device.wait(Until.hasObject(By.res(target.packageName, condition.selector)), condition.timeout)
                }

                WaitType.TEXT -> {
                    device.wait(Until.hasObject(By.text(condition.selector)), condition.timeout)
                }

                WaitType.SCROLLABLE -> {
                    device.wait(Until.hasObject(By.scrollable(true)), condition.timeout)
                }

                WaitType.CLICKABLE -> {
                    device.wait(Until.hasObject(By.clickable(true)), condition.timeout)
                }
            }
        }
        device.waitForIdle()
        Thread.sleep(1000)
    }
}
