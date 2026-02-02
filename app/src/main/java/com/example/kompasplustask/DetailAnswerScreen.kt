package com.example.kompasplustask

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.kompasplustask.ui.theme.KompasPlusTaskTheme

@Composable
fun DetailAnswerScreen(modifier: Modifier = Modifier){
    Text(
        text = "HetailAnswerScreen",
        modifier = modifier
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DetailAnswerScreenPreview() {
    KompasPlusTaskTheme {
        DetailAnswerScreen()
    }
}