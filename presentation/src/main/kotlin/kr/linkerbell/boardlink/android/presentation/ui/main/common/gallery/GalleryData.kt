package kr.linkerbell.boardlink.android.presentation.ui.main.common.gallery

import androidx.compose.runtime.Immutable
import androidx.paging.compose.LazyPagingItems
import kr.linkerbell.boardlink.android.presentation.model.gallery.GalleryFolder
import kr.linkerbell.boardlink.android.presentation.model.gallery.GalleryImage

@Immutable
data class GalleryData(
    val folderList: List<GalleryFolder>,
    val galleryImageList: LazyPagingItems<GalleryImage>
)
