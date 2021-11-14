package com.seacows.app.pill.and.week2app

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import coil.compose.rememberImagePainter
import com.seacows.app.pill.and.week2app.BodyContent
import com.seacows.app.pill.and.week2app.ui.theme.Week2AppTheme
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Week2AppTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(text = "LayoutsCodelab")
                            },
                            actions = {
                                IconButton(onClick = { /* doSomething() */ }) {
                                    Icon(Icons.Filled.Favorite, contentDescription = null)
                                }
                            }
                        )
                    }
                ) { innerPadding ->
                    BodyContent(Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun StaggeredGrid(
    modifier: Modifier = Modifier,
    rows: Int = 3,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->

        // 각각의 아이템에 대한 w, h 배열
        val rowWidths = IntArray(rows) { 0 }
        val rowHeights = IntArray(rows) { 0 }

        val placeables = measurables.mapIndexed { index, measurable ->
            val placeable = measurable.measure(constraints)
            val row = index % rows
            rowWidths[row] += placeable.width
            rowHeights[row] = Math.max(rowHeights[row], placeable.height)

            placeable
        }
        // 전체 그리드 layout의 width는 행 중에 가장 긴 행의 넓이
        val width = rowWidths.maxOrNull()
            ?.coerceIn(constraints.minWidth.rangeTo(constraints.maxWidth)) ?: constraints.minWidth

        // 전체 그리드 layout의 hiehgt은 item 중에 가장 키 큰 친구들의 sum 하고나서
        // min과 max 사이의 값으로 결정해줌
        val height = rowHeights.sumOf { it }
            .coerceIn(constraints.minHeight.rangeTo(constraints.maxHeight))

        // 각각의 low의 position Y는 이전까지의 row height들의 합임
        val rowY = IntArray(rows) { 0 }
        for (i in 1 until rows) {
            rowY[i] = rowY[i-1] + rowHeights[i-1]
        }

        layout(width, height) {
            val rowX = IntArray(rows) { 0 }
            placeables.forEachIndexed { index, placeable ->
                val row = index % rows
                placeable.placeRelative(
                    x = rowX[row],
                    y = rowY[row]
                )
                rowX[row] += placeable.width
            }
        }
    }
}

val topics = listOf(
    "Arts & Crafts", "Beauty", "Books", "Business", "Comics", "Culinary",
    "Design", "Fashion", "Film", "History", "Maths", "Music", "People", "Philosophy",
    "Religion", "Social sciences", "Technology", "TV", "Writing"
)

@Composable
fun BodyContent(modifier: Modifier = Modifier) {
    Row (modifier = modifier
        .background(color = Color.LightGray)
        .padding(16.dp)
        .size(200.dp)
        .horizontalScroll(rememberScrollState())) {
        StaggeredGrid(modifier = modifier) {
            for (topic in topics) {
                Chip(modifier = Modifier.padding(4.dp), text = topic)
            }
        }
    }
}

@Composable
fun Chip(modifier: Modifier = Modifier, text: String) {
    Card(
        modifier = modifier,
        border = BorderStroke(color = Color.Black, width = Dp.Hairline),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(start = 8.dp, top = 1.dp, end = 8.dp, bottom = 1.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(16.dp, 16.dp)
                    .background(color = MaterialTheme.colors.primary)
            )
            Spacer(Modifier.width(4.dp))
            Text(text = text)
        }
    }
}

// PREVIEW
@Preview
@Composable
fun LayoutsCodelabPreview() {
    Week2AppTheme {
        BodyContent()
    }
}

@Preview
@Composable
fun ChipPreview() {
    Week2AppTheme {
        Chip(text = "Hi there")
    }
}
