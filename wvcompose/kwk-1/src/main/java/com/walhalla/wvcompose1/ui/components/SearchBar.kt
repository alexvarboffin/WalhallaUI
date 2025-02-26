package com.walhalla.wvcompose1.ui.components

import android.webkit.WebView
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    webView: WebView?,
    onClose: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var lastSearchQuery by remember { mutableStateOf("") }
    var currentMatchIndex by remember { mutableStateOf(0) }
    var totalMatches by remember { mutableStateOf(0) }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        tonalElevation = 3.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = searchQuery,
                onValueChange = { 
                    searchQuery = it
                    if (it.length >= 2) {
                        webView?.findAllAsync(it)
                        lastSearchQuery = it
                    }
                },
                placeholder = { Text("Поиск на странице") },
                leadingIcon = { Icon(Icons.Default.Search, "Поиск") },
                modifier = Modifier.weight(1f),
                singleLine = true
            )
            
            if (searchQuery.isNotEmpty()) {
                Text(
                    text = if (totalMatches > 0) "${currentMatchIndex + 1}/$totalMatches" else "0/0",
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                
                IconButton(onClick = {
                    if (lastSearchQuery.isNotEmpty()) {
                        webView?.findNext(false)
                    }
                }) {
                    Icon(Icons.Default.KeyboardArrowUp, "Предыдущее")
                }
                
                IconButton(onClick = {
                    if (lastSearchQuery.isNotEmpty()) {
                        webView?.findNext(true)
                    }
                }) {
                    Icon(Icons.Default.KeyboardArrowDown, "Следующее")
                }
            }
            
            IconButton(onClick = onClose) {
                Icon(Icons.Default.Close, "Закрыть")
            }
        }
    }
} 