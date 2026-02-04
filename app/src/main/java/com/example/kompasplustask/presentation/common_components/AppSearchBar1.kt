package com.example.kompasplustask.presentation.common_components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kompasplustask.R
import com.example.kompasplustask.ui.theme.KompasPlusTaskTheme

@Composable
fun AppSearchBar1(
    query: String,
    onQueryChange: (String) -> Unit,
    onClearClick: () -> Unit,
    modifier: Modifier = Modifier,
    height: Dp = 40.dp  // параметр высоты
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var isFocused by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .padding(horizontal = 8.dp)
    ) {
        BasicTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(height),
            textStyle = LocalTextStyle.current.copy(
                fontSize = 14.sp,
                lineHeight = 16.sp,
                color = MaterialTheme.colorScheme.onSurface
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = { keyboardController?.hide() }
            ),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(height)
                        .border(
                            width = 1.dp,
                            color = if (isFocused)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .background(
                            color = MaterialTheme.colorScheme.onPrimary,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(start = 36.dp, end = 36.dp, top = 6.dp)
                        .onFocusChanged { isFocused = it.isFocused }
                ) {
                    // Leading icon
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Поиск",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .size(28.dp)
                            .align(Alignment.CenterStart)
                            .offset(x = (-20).dp)
                    )

                    // Text field content
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (query.isEmpty()) {
                            Text(
                                text = stringResource(R.string.search),
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
                                fontSize = 16.sp
                            )
                        }
                        innerTextField()
                    }

                    // Trailing icon
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .offset(x = 20.dp)
                    ) {
                        if (query.isNotBlank()) {
                            IconButton(
                                onClick = onClearClick,
                                modifier = Modifier.size(28.dp),
                              //  contentPadding = PaddingValues(0.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Очистить",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        } else {
                            IconButton(
                                onClick = onClearClick,
                                modifier = Modifier.size(28.dp),
                             //   contentPadding = PaddingValues(0.dp)
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.icons_microphone_48),
                                    contentDescription = "Микрофон",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                        }
                    }
                }
            }
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun SearchBarPreview(){
    KompasPlusTaskTheme {
        AppSearchBar1(
            query = "string",
            onClearClick = {},
            onQueryChange = {}
        )
    }

}