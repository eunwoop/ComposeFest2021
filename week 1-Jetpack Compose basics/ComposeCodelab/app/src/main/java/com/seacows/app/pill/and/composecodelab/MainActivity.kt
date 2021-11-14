package com.seacows.app.pill.and.composecodelab

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.seacows.app.pill.and.composecodelab.ui.theme.ComposeCodelabTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // project name 따라감.
            ComposeCodelabTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    MyApp()
                }
            }
        }
    }
}

@Composable
private fun MyApp() {
    var shouldShowOnBoarding by rememberSaveable { mutableStateOf(true) }
    if (shouldShowOnBoarding) {
        OnBoardingScreen(
            onContinueClicked = { shouldShowOnBoarding = false })
    } else {
        GreetingList()
    }
}

@Composable
fun OnBoardingScreen(onContinueClicked: () -> Unit) {
    Surface {
        Column (
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text("Welcome to the Basic Codelab!")
            Button (
                modifier = Modifier.padding(vertical = 24.dp),
                onClick = onContinueClicked
            ) {
                Text("Continue")
            }
        }
    }
}

@Composable
private fun GreetingList(names: List<String> = List(1000) { "$it" }) {
    LazyColumn(modifier = Modifier.padding(vertical = 4.dp)) {
        items(items = names) { name ->
            Greeting(name)
        }
    }
}

@Composable
private fun Greeting(name: String) {
    Card (
        backgroundColor = MaterialTheme.colors.primary,
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        CardContent(name)
    }
}

@Composable
private fun CardContent(name: String) {
    val expanded = remember { mutableStateOf(false) }
    val extraPadding by animateDpAsState(
        if (expanded.value) 48.dp else 0.dp,

        )

    Row(
        modifier = Modifier
            .padding(12.dp)
            .animateContentSize (
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow,
                )
            )
    ){
        Column(modifier = Modifier
            .weight(1f)
            .padding(bottom = extraPadding.coerceAtLeast(0.dp))
        ) {
            Text(text = "Hello, ")
            Text(text = name, style = MaterialTheme.typography.h4.copy(
                fontWeight = FontWeight.ExtraBold
            ))
            if (expanded.value) {
                Text(
                    text = ("Composem ipsum color sit lazy, " +
                            "padding theme elit, sed do bouncy. ").repeat(4),
                )
            }
        }
        IconButton(
            onClick = { expanded.value = !expanded.value },
        ) {
            Icon (imageVector = if (expanded.value) Filled.ExpandLess else Filled.ExpandMore,
                contentDescription = if (expanded.value) {
                    stringResource(R.string.show_less)
                } else {
                    stringResource(R.string.show_more)
                })
        }
    }
}

// PREVIEW
@Preview(showBackground = true,
    widthDp = 320,
    heightDp = 320,
    uiMode = UI_MODE_NIGHT_YES,
    name = "DefaultPreviewDark")
@Preview(showBackground = true,
    widthDp = 320)
@Composable
fun OnBoardingPreview() {
    ComposeCodelabTheme {
        OnBoardingScreen(onContinueClicked = {/* Do nothing */} )
    }
}

@Preview(showBackground = true,
    widthDp = 320,
    heightDp = 320)
@Composable
fun DefaultPreview() {
    ComposeCodelabTheme {
        GreetingList()
    }
}