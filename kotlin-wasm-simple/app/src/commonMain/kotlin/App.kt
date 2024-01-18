
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

@Composable
fun App() {
  MaterialTheme {
    val greetingText by remember { mutableStateOf("Hello World!") }

    Text(greetingText)
  }
}
