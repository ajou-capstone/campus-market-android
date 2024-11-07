package kr.linkerbell.campusmarket.android.presentation.ui.main.common.gallery

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.holix.android.bottomsheetdialog.compose.BottomSheetBehaviorProperties
import com.holix.android.bottomsheetdialog.compose.BottomSheetDialogProperties
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.plus
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.MutableEventFlow
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.eventObserve
import kr.linkerbell.campusmarket.android.presentation.R
import kr.linkerbell.campusmarket.android.presentation.common.theme.Black
import kr.linkerbell.campusmarket.android.presentation.common.theme.Blue400
import kr.linkerbell.campusmarket.android.presentation.common.theme.Body0
import kr.linkerbell.campusmarket.android.presentation.common.theme.Body2
import kr.linkerbell.campusmarket.android.presentation.common.theme.Gray100
import kr.linkerbell.campusmarket.android.presentation.common.theme.Gray200
import kr.linkerbell.campusmarket.android.presentation.common.theme.Gray400
import kr.linkerbell.campusmarket.android.presentation.common.theme.Gray900
import kr.linkerbell.campusmarket.android.presentation.common.theme.Headline3
import kr.linkerbell.campusmarket.android.presentation.common.theme.Red400
import kr.linkerbell.campusmarket.android.presentation.common.theme.Space16
import kr.linkerbell.campusmarket.android.presentation.common.theme.Space20
import kr.linkerbell.campusmarket.android.presentation.common.theme.Space24
import kr.linkerbell.campusmarket.android.presentation.common.theme.Space4
import kr.linkerbell.campusmarket.android.presentation.common.theme.Space56
import kr.linkerbell.campusmarket.android.presentation.common.theme.Space8
import kr.linkerbell.campusmarket.android.presentation.common.theme.White
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.LaunchedEffectWithLifecycle
import kr.linkerbell.campusmarket.android.presentation.common.view.BottomSheetScreen
import kr.linkerbell.campusmarket.android.presentation.common.view.RippleBox
import kr.linkerbell.campusmarket.android.presentation.common.view.dropdown.TextDropdownMenu
import kr.linkerbell.campusmarket.android.presentation.model.gallery.GalleryFolder
import kr.linkerbell.campusmarket.android.presentation.model.gallery.GalleryImage

@Composable
fun GalleryScreen(
    navController: NavController,
    onDismissRequest: () -> Unit,
    selectedImageList: List<GalleryImage> = listOf(),
    minSelectCount: Int = 1,
    maxSelectCount: Int = 1,
    onResult: (List<GalleryImage>) -> Unit,
    viewModel: GalleryViewModel = hiltViewModel()
) {
    val argument: GalleryArgument = Unit.let {
        val state by viewModel.state.collectAsStateWithLifecycle()

        GalleryArgument(
            state = state,
            event = viewModel.event,
            intent = viewModel::onIntent,
            logEvent = viewModel::logEvent,
            coroutineContext = viewModel.coroutineContext
        )
    }

    val data: GalleryData = Unit.let {
        val folderList by viewModel.folderList.collectAsStateWithLifecycle()
        val galleryImageList = viewModel.galleryImageList.collectAsLazyPagingItems()

        GalleryData(
            folderList = folderList,
            galleryImageList = galleryImageList,
            selectedImageList = selectedImageList,
            minSelectCount = minSelectCount,
            maxSelectCount = maxSelectCount
        )
    }

    GalleryScreen(
        navController = navController,
        argument = argument,
        data = data,
        onDismissRequest = onDismissRequest,
        onResult = onResult
    )
}

@Composable
private fun GalleryScreen(
    navController: NavController,
    argument: GalleryArgument,
    data: GalleryData,
    onDismissRequest: () -> Unit,
    onResult: (List<GalleryImage>) -> Unit,
) {
    val (state, event, intent, logEvent, coroutineContext) = argument
    val scope = rememberCoroutineScope() + coroutineContext
    val localConfiguration = LocalConfiguration.current

    val perMissionAlbumLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            intent(GalleryIntent.OnGrantPermission)
        } else {
            onDismissRequest()
        }
    }
    val selectedList: MutableList<GalleryImage> = remember {
        mutableStateListOf<GalleryImage>(*data.selectedImageList.toTypedArray())
    }
    var isDropDownMenuExpanded: Boolean by remember { mutableStateOf(false) }
    var currentFolder: GalleryFolder by remember { mutableStateOf(GalleryFolder.recent) }

    BottomSheetScreen(
        onDismissRequest = onDismissRequest,
        properties = BottomSheetDialogProperties(
            behaviorProperties = BottomSheetBehaviorProperties(
                state = BottomSheetBehaviorProperties.State.Expanded,
                skipCollapsed = true
            )
        )
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .background(White)
        ) {
            val (topBar, contents) = createRefs()

            LazyVerticalGrid(
                modifier = Modifier
                    .fillMaxSize()
                    .constrainAs(contents) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                        width = Dimension.fillToConstraints
                        height = Dimension.fillToConstraints
                    },
                columns = GridCells.Adaptive(minSize = 128.dp),
                contentPadding = PaddingValues(top = Space56),
                verticalArrangement = Arrangement.spacedBy(1.dp),
                horizontalArrangement = Arrangement.spacedBy(1.dp)
            ) {
                // TODO : 아무것도 없을 경우 처리
                items(data.galleryImageList.itemCount) { index ->
                    data.galleryImageList[index]?.let { gallery ->
                        GalleryScreenItem(
                            galleryImage = gallery,
                            selectedList = selectedList,
                            onSelectImage = { image ->
                                if (selectedList.size < data.maxSelectCount) {
                                    selectedList.add(image)
                                }
                            },
                            onDeleteImage = { image ->
                                selectedList.removeIf { it.id == image.id }
                            }
                        )
                    }
                }
            }
            Row(
                modifier = Modifier
                    .height(Space56)
                    .background(White.copy(alpha = 0.9f))
                    .constrainAs(topBar) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        width = Dimension.fillToConstraints
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.width(Space20))
                RippleBox(
                    onClick = {
                        onDismissRequest()
                    }
                ) {
                    Icon(
                        modifier = Modifier.size(Space24),
                        painter = painterResource(id = R.drawable.ic_close),
                        contentDescription = null,
                        tint = Gray900
                    )
                }
                Spacer(modifier = Modifier.width(Space20))
                RippleBox(
                    onClick = {
                        isDropDownMenuExpanded = true
                    }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = currentFolder.name,
                            style = Headline3.merge(Gray900)
                        )
                        Icon(
                            modifier = Modifier.size(Space16),
                            painter = painterResource(R.drawable.ic_chevron_down),
                            contentDescription = null,
                            tint = Gray900
                        )
                    }
                    TextDropdownMenu(
                        items = data.folderList,
                        label = { it.name },
                        isExpanded = isDropDownMenuExpanded,
                        onDismissRequest = { isDropDownMenuExpanded = false },
                        onClick = { folder ->
                            currentFolder = folder
                            intent(GalleryIntent.OnChangeFolder(folder))
                        }
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                if (selectedList.size >= data.minSelectCount) {
                    Text(
                        text = selectedList.size.toString(),
                        style = Body0.merge(Blue400)
                    )
                    Spacer(modifier = Modifier.width(Space8))
                    RippleBox(
                        onClick = {
                            onDismissRequest()
                            onResult(selectedList)
                        }
                    ) {
                        Text(
                            text = "확인",
                            style = Body0.merge(Gray900)
                        )
                    }
                } else {
                    Text(
                        text = "확인",
                        style = Body0.merge(Gray400)
                    )
                }
                Spacer(modifier = Modifier.width(Space20))
            }
        }
    }

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            perMissionAlbumLauncher.launch(
                Manifest.permission.READ_MEDIA_IMAGES
            )
        } else {
            perMissionAlbumLauncher.launch(
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        }
    }

    LaunchedEffectWithLifecycle(event, coroutineContext) {
        event.eventObserve { event ->

        }
    }
}

@Composable
fun GalleryScreenItem(
    galleryImage: GalleryImage,
    selectedList: List<GalleryImage>,
    onSelectImage: (GalleryImage) -> Unit,
    onDeleteImage: (GalleryImage) -> Unit
) {
    val context = LocalContext.current

    val index = selectedList.indexOfFirst { it.id == galleryImage.id }

    Box(
        modifier = Modifier
            .background(Gray200)
            .clickable {
                if (index > -1) {
                    onDeleteImage(galleryImage)
                } else {
                    onSelectImage(galleryImage)
                }
            }
            .run {
                if (index > -1) {
                    border(BorderStroke(2.dp, Blue400))
                } else {
                    this
                }
            }
    ) {
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(context)
                .data(galleryImage.filePath)
                .crossfade(true)
                .build(),
            contentDescription = null,
            modifier = Modifier
                .aspectRatio(1f)
                .align(Alignment.Center),
            contentScale = ContentScale.Crop,
            loading = {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(Space24)
                            .align(Alignment.Center),
                        color = Gray100,
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
                        tint = Red400
                    )
                }
            }
        )

        if (index > -1) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .align(Alignment.Center)
                    .background(Black.copy(alpha = 0.4f))
            )
        }

        if (index > -1) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(Space8)
            ) {
                Box(
                    modifier = Modifier
                        .size(Space24)
                        .clip(CircleShape)
                        .background(Blue400, CircleShape)
                ) {
                    Text(
                        text = (index + 1).toString(),
                        modifier = Modifier.align(Alignment.Center),
                        style = Body0.merge(White)
                    )
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(Space8)
            ) {
                Box(
                    modifier = Modifier
                        .size(Space24)
                        .align(Alignment.TopEnd)
                        .clip(CircleShape)
                        .background(White.copy(alpha = 0.6f), CircleShape)
                        .border(1.dp, Gray400, CircleShape)
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(Black.copy(alpha = 0.6f))
        ) {
            Box(
                modifier = Modifier.padding(Space4)
            ) {
                Text(
                    text = galleryImage.name,
                    minLines = 2,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = Body2.merge(White)
                )
            }
        }
    }
}

@Preview(apiLevel = 34)
@Composable
private fun GalleryScreenPreview1() {
    GalleryScreen(
        navController = rememberNavController(),
        argument = GalleryArgument(
            state = GalleryState.Init,
            event = MutableEventFlow(),
            intent = {},
            logEvent = { _, _ -> },
            coroutineContext = CoroutineExceptionHandler { _, _ -> }
        ),
        data = GalleryData(
            folderList = listOf(),
            galleryImageList = MutableStateFlow<PagingData<GalleryImage>>(PagingData.empty()).collectAsLazyPagingItems(),
            selectedImageList = listOf(),
            minSelectCount = 1,
            maxSelectCount = 1
        ),
        onDismissRequest = {},
        onResult = {}
    )
}

@Preview(apiLevel = 34)
@Composable
private fun GalleryScreenPreview2() {
    GalleryScreen(
        navController = rememberNavController(),
        argument = GalleryArgument(
            state = GalleryState.Init,
            event = MutableEventFlow(),
            intent = {},
            logEvent = { _, _ -> },
            coroutineContext = CoroutineExceptionHandler { _, _ -> }
        ),
        data = GalleryData(
            folderList = listOf(),
            galleryImageList = MutableStateFlow<PagingData<GalleryImage>>(
                PagingData.from(
                    listOf(
                        GalleryImage(
                            id = 1,
                            filePath = "https://via.placeholder.com/150",
                            name = "image1",
                            date = "2021-09-01"
                        ),
                        GalleryImage(
                            id = 2,
                            filePath = "https://via.placeholder.com/150",
                            name = "image1",
                            date = "2021-09-01"
                        )
                    )
                )
            ).collectAsLazyPagingItems(),
            selectedImageList = listOf(
                GalleryImage(
                    id = 1,
                    filePath = "https://via.placeholder.com/150",
                    name = "image1",
                    date = "2021-09-01"
                )
            ),
            minSelectCount = 1,
            maxSelectCount = 1
        ),
        onDismissRequest = {},
        onResult = {}
    )
}
