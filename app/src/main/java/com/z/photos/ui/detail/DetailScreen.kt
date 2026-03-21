package com.z.photos.ui.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.z.photos.business.entities.Photo
import com.z.photos.ui.theme.HomeworkTheme

private const val IMAGE_HEIGHT_DP = 320
private const val CONTENT_PADDING_DP = 16
private const val SECTION_SPACING_DP = 12
private const val LABEL_ARTIST = "Artist"
private const val LABEL_ID = "ID"
private const val CONTENT_DESC_BACK = "Back"
private const val CONTENT_DESC_FAVORITE = "Favorite"

@Composable
fun DetailScreen(
    onBack: () -> Unit,
    viewModel: DetailViewModel = hiltViewModel(),
) {
    val photo by viewModel.photoDetailsState.collectAsState()
    photo?.let {
        DetailContent(
            photo = it,
            onBack = onBack,
            onFavoriteClick = { viewModel.toggleFavorite() },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DetailContent(
    photo: Photo,
    onBack: () -> Unit,
    onFavoriteClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(photo.artist) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = CONTENT_DESC_BACK,
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Box {
                AsyncImage(
                    model = photo.photoUrl,
                    contentDescription = photo.artist,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IMAGE_HEIGHT_DP.dp),
                    contentScale = ContentScale.Crop,
                )
                IconButton(
                    onClick = onFavoriteClick,
                    modifier = Modifier.align(Alignment.BottomEnd),
                ) {
                    Icon(
                        imageVector = if (photo.isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = CONTENT_DESC_FAVORITE,
                        tint = if (photo.isFavorite) Color.Red else Color.White,
                    )
                }
            }
            Spacer(modifier = Modifier.height(CONTENT_PADDING_DP.dp))
            Column(modifier = Modifier.padding(horizontal = CONTENT_PADDING_DP.dp)) {
                Text(
                    text = LABEL_ARTIST,
                    style = MaterialTheme.typography.labelSmall,
                )
                Text(
                    text = photo.artist,
                    style = MaterialTheme.typography.bodyLarge,
                )
                Spacer(modifier = Modifier.height(SECTION_SPACING_DP.dp))
                Text(
                    text = LABEL_ID,
                    style = MaterialTheme.typography.labelSmall,
                )
                Text(
                    text = photo.id.toString(),
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DetailScreenPreview() {
    HomeworkTheme {
        DetailContent(
            photo = Photo(
                id = 1234567,
                photoUrl = "",
                photoThumbnailUrl = "",
                artist = "Jane Doe",
                isFavorite = false,
            ),
            onBack = {},
            onFavoriteClick = {},
        )
    }
}
