package com.example.kompasplustask

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.kompasplustask.ui.theme.KompasPlusTaskTheme

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    onNavigateToDetail: () -> Unit = {}
){
Column(modifier = modifier
    .fillMaxSize()
    .padding(16.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center) {

    Text(
        text = "Hello Serega",
        modifier = modifier
    )

    Button(
        onClick = onNavigateToDetail,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Go to Detail Screen")
    }
}


}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainScreenPreview() {
    KompasPlusTaskTheme {
        MainScreen()
    }
}

