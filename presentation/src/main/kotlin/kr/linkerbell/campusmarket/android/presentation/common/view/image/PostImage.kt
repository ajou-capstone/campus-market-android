package kr.linkerbell.campusmarket.android.presentation.common.view.image

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import kr.linkerbell.campusmarket.android.presentation.R
import kr.linkerbell.campusmarket.android.presentation.common.theme.Black
import kr.linkerbell.campusmarket.android.presentation.common.theme.Red700
import kr.linkerbell.campusmarket.android.presentation.common.theme.Space24
import kr.linkerbell.campusmarket.android.presentation.common.theme.White

@Composable
fun PostImage(
    data: Any?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop
) {
    val context = LocalContext.current

    Box(
        modifier = modifier.background(Black)
    ) {
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(context)
                .data(data)
                .crossfade(true)
                .build(),
            contentScale = contentScale,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            loading = {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(Space24)
                            .align(Alignment.Center),
                        color = White,
                        strokeWidth = 2.dp
                    )
                }
            },
            error = {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Icon(
                        modifier = Modifier
                            .size(Space24)
                            .align(Alignment.Center),
                        painter = painterResource(R.drawable.ic_error),
                        contentDescription = null,
                        tint = Red700
                    )
                }
            }
        )
    }
}

@Preview
@Composable
private fun PostImagePreview1() {
    PostImage(
        data = "https://picsum.photos/200",
        modifier = Modifier.size(60.dp)
    )
}

@Preview
@Composable
private fun PostImagePreview2() {
    PostImage(
        data = "https://picsum.photos/200",
        modifier = Modifier.size(200.dp)
    )
}
