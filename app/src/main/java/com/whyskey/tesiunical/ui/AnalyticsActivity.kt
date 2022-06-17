package com.whyskey.tesiunical.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.whyskey.tesiunical.R
import com.whyskey.tesiunical.data.Thesis
import com.whyskey.tesiunical.data.Type
import com.whyskey.tesiunical.model.ThesisViewModel
import com.whyskey.tesiunical.ui.components.AnalyticsRow
import com.whyskey.tesiunical.ui.components.ThesisRow


@Composable
fun Analytics(
    viewModel: ThesisViewModel
) {
    val list = listOf(Type.DEPARTMENTAL, Type.CORPORATE, Type.ERASMUS)

    AnalyticsBody(

        items = list,
        colors = { it.color },
        amounts = { it.amount },
        max = {it.max},
        amountsTotal = list.sumOf { it.amount },
        maxTotal = list.sumOf { it.max },
        circleLabel = stringResource(id = R.string.total),
        rows = {
            AnalyticsRow(color =  it.color, title = it.name, amount = it.amount, max = it.max)
        }
    )
}

@Composable
fun <T> AnalyticsBody(
    modifier: Modifier = Modifier,
    items: List<T>,
    colors: (T) -> Color,
    amounts: (T) -> Int,
    max: (T) ->Int,
    amountsTotal: Int,
    maxTotal: Int,
    circleLabel: String,
    rows: @Composable (T) -> Unit
){
    Column(modifier = modifier.verticalScroll(rememberScrollState())) {
        Box(Modifier.padding(16.dp)) {
            val accountsProportion = items.extractProportions { amounts(it) }
            val circleColors = items.map { colors(it) }
            AnimatedCircle(
                accountsProportion,
                circleColors,
                Modifier
                    .height(300.dp)
                    .align(Alignment.Center)
                    .fillMaxWidth()
            )
            Column(modifier = Modifier.align(Alignment.Center)) {
                Text(
                    text = circleLabel,
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Text(
                    text = "$amountsTotal/$maxTotal",
                    style = MaterialTheme.typography.h2,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
        Spacer(Modifier.height(10.dp))
        Card {
            Column(modifier = Modifier.padding(12.dp)) {
                items.forEach { item ->
                    rows(item)
                }
            }
        }
    }
}

fun <E> List<E>.extractProportions(selector: (E) -> Int): List<Float> {
    val total = this.sumOf { selector(it).toDouble() }
    return this.map { (selector(it) / total).toFloat() }
}

//Animated
private const val DividerLengthInDegrees = 1.8f

/**
 * A donut chart that animates when loaded.
 */
@Composable
fun AnimatedCircle(
    proportions: List<Float>,
    colors: List<Color>,
    modifier: Modifier = Modifier
) {
    val currentState = remember {
        MutableTransitionState(AnimatedCircleProgress.START)
            .apply { targetState = AnimatedCircleProgress.END }
    }
    val stroke = with(LocalDensity.current) { Stroke(5.dp.toPx()) }
    val transition = updateTransition(currentState, label = "")
    val angleOffset by transition.animateFloat(
        transitionSpec = {
            tween(
                delayMillis = 500,
                durationMillis = 900,
                easing = LinearOutSlowInEasing
            )
        }, label = ""
    ) { progress ->
        if (progress == AnimatedCircleProgress.START) {
            0f
        } else {
            360f
        }
    }
    val shift by transition.animateFloat(
        transitionSpec = {
            tween(
                delayMillis = 500,
                durationMillis = 900,
                easing = CubicBezierEasing(0f, 0.75f, 0.35f, 0.85f)
            )
        }, label = ""
    ) { progress ->
        if (progress == AnimatedCircleProgress.START) {
            0f
        } else {
            30f
        }
    }

    Canvas(modifier) {
        val innerRadius = (size.minDimension - stroke.width) / 2
        val halfSize = size / 2.0f
        val topLeft = Offset(
            halfSize.width - innerRadius,
            halfSize.height - innerRadius
        )
        val size = Size(innerRadius * 2, innerRadius * 2)
        var startAngle = shift - 90f
        proportions.forEachIndexed { index, proportion ->
            val sweep = proportion * angleOffset
            drawArc(
                color = colors[index],
                startAngle = startAngle + DividerLengthInDegrees / 2,
                sweepAngle = sweep - DividerLengthInDegrees,
                topLeft = topLeft,
                size = size,
                useCenter = false,
                style = stroke
            )
            startAngle += sweep
        }
    }
}
private enum class AnimatedCircleProgress { START, END }
