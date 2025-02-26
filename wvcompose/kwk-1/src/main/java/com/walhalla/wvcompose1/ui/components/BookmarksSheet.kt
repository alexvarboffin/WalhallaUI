package com.walhalla.wvcompose1.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.walhalla.wvcompose1.config.MenuItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookmarksSheet(
    bookmarks: List<MenuItem>,
    onBookmarkClick: (MenuItem) -> Unit,
    onBookmarkDelete: (MenuItem) -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Закладки",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            if (bookmarks.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Нет сохранённых закладок",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn {
                    items(bookmarks) { bookmark ->
                        BookmarkItem(
                            bookmark = bookmark,
                            onClick = { onBookmarkClick(bookmark) },
                            onDelete = { onBookmarkDelete(bookmark) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun BookmarkItem(
    bookmark: MenuItem,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    ListItem(
        headlineContent = { Text(bookmark.title) },
        supportingContent = { Text(bookmark.url) },
        trailingContent = {
            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Удалить закладку"
                )
            }
        },
        modifier = Modifier.clickable(onClick = onClick)
    )
} 