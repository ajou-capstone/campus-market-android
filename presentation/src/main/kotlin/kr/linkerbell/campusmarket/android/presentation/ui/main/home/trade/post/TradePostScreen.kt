package kr.linkerbell.campusmarket.android.presentation.ui.main.home.trade.post

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.plus
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.MutableEventFlow
import kr.linkerbell.campusmarket.android.domain.model.feature.category.CategoryList
import kr.linkerbell.campusmarket.android.presentation.R
import kr.linkerbell.campusmarket.android.presentation.common.theme.Blue400
import kr.linkerbell.campusmarket.android.presentation.common.theme.Body0
import kr.linkerbell.campusmarket.android.presentation.common.theme.Body2
import kr.linkerbell.campusmarket.android.presentation.common.theme.Gray200
import kr.linkerbell.campusmarket.android.presentation.common.theme.Headline3
import kr.linkerbell.campusmarket.android.presentation.common.theme.Indigo100
import kr.linkerbell.campusmarket.android.presentation.common.theme.Indigo50
import kr.linkerbell.campusmarket.android.presentation.common.theme.Red400
import kr.linkerbell.campusmarket.android.presentation.common.theme.White
import kr.linkerbell.campusmarket.android.presentation.common.view.BottomSheetScreen
import kr.linkerbell.campusmarket.android.presentation.common.view.confirm.ConfirmButton
import kr.linkerbell.campusmarket.android.presentation.common.view.confirm.ConfirmButtonProperties
import kr.linkerbell.campusmarket.android.presentation.common.view.confirm.ConfirmButtonSize
import kr.linkerbell.campusmarket.android.presentation.common.view.confirm.ConfirmButtonType
import kr.linkerbell.campusmarket.android.presentation.common.view.image.PostImage
import kr.linkerbell.campusmarket.android.presentation.common.view.textfield.TypingTextField
import kr.linkerbell.campusmarket.android.presentation.ui.main.common.gallery.GalleryScreen
import timber.log.Timber

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TradePostScreen(
    navController: NavController,
    argument: TradePostArgument,
    data: TradePostData
) {
    val (state, event, intent, logEvent, coroutineContext) = argument
    val scope = rememberCoroutineScope() + coroutineContext

    val title = remember { mutableStateOf("") }
    val price = remember { mutableStateOf("0") }
    val category = remember { mutableStateOf("OTHER") }
    val description = remember { mutableStateOf("") }
    val thumbnailIndex = remember { mutableIntStateOf(0) }
    var imageList: List<String> by remember { mutableStateOf(emptyList()) }

    var isGalleryShowing by remember { mutableStateOf(false) }
    var isBottomSheetVisible by remember { mutableStateOf(false) }

    var bottomSheetContent = "Bottom Sheet Content"

    val validatePostContentAndPost = validatePostContentAndPost@{
        when {
            title.value.isBlank() -> {
                bottomSheetContent = "제목을 입력해주세요"
                return@validatePostContentAndPost
            }

            description.value.isBlank() -> {
                bottomSheetContent = "상품 상세 정보를 입력해주세요"
                return@validatePostContentAndPost
            }

            imageList.isEmpty() -> {
                bottomSheetContent = "최소 한 장 이상의 사진이 필요해요"
                return@validatePostContentAndPost
            }
        }
        argument.intent(
            TradePostIntent.PostNewTrade(
                title = title.value,
                description = description.value,
                price = price.value.toIntOrNull() ?: 0,
                category = category.value,
                thumbnail = imageList[thumbnailIndex.intValue],
                images = imageList
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        TradePostScreenTopBar(navigateUp = { navController.popBackStack() })

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "사진",
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Row {
                    TradePostScreenAddImageBox(
                        onAddImageClicked = { isGalleryShowing = true }
                    )
                    Row(
                        modifier = Modifier
                            .horizontalScroll(rememberScrollState())
                            .padding(horizontal = 16.dp)
                    ) {
                        imageList.forEachIndexed { index, image ->
                            TradePostScreenImageBox(
                                imageSource = image,
                                isThumbnail = (thumbnailIndex.intValue == index),
                                onDeleteImageClicked = { uri ->
                                    imageList = imageList.filter { it != uri }
                                },
                                onContentClicked = {
                                    thumbnailIndex.intValue = index
                                }
                            )
                        }
                    }

                }
                TradePostScreenTradeInfo(
                    title = title,
                    price = price,
                    category = category,
                    description = description,
                    categoryList = data.categoryList
                )
            }
            TradePostScreenPostButton(onPostButtonClicked = validatePostContentAndPost)
        }
    }

    if (isGalleryShowing) {
        GalleryScreen(
            navController = navController,
            onDismissRequest = { isGalleryShowing = false },
            onResult = {
                if (imageList.size <= 5) {
                    imageList = imageList + it.filePath
                    Timber.tag("siri22").d("ImageList : $imageList")
                } else {
                    //TODO("사진은 5장까지만!")
                }
            }
        )
    }

    if (isBottomSheetVisible) {
        BottomSheetScreen(
            onDismissRequest = { isBottomSheetVisible = false },
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = bottomSheetContent,
                    style = Body0
                )

                Spacer(
                    modifier = Modifier.height(60.dp)
                )

                ConfirmButton(
                    modifier = Modifier.fillMaxWidth(),
                    properties = ConfirmButtonProperties(
                        size = ConfirmButtonSize.Large,
                        type = ConfirmButtonType.Primary
                    ),
                    onClick = {
                        isBottomSheetVisible = false
                    }
                ) { style ->
                    Text(
                        text = "확인",
                        style = style
                    )
                }
            }
        }
    }
}

@Composable
private fun TradePostScreenAddImageBox(onAddImageClicked: () -> Unit) {
    Card(
        modifier = Modifier.size(100.dp),
        colors = CardDefaults.cardColors(White),
        border = BorderStroke(2.dp, Color.Gray)
    ) {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(Indigo100)
            .clickable {
                onAddImageClicked()
            }
        ) {
            Icon(
                modifier = Modifier
                    .size(64.dp)
                    .align(Alignment.Center)
                    .padding(4.dp),
                painter = painterResource(id = R.drawable.ic_plus),
                contentDescription = null,
            )
        }
    }
}

@Composable
private fun TradePostScreenImageBox(
    imageSource: String,
    isThumbnail: Boolean,
    onDeleteImageClicked: (String) -> Unit,
    onContentClicked: () -> Unit
) {
    Box(Modifier.padding(end = 16.dp)) {
        PostImage(
            data = imageSource,
            modifier = Modifier
                .size(100.dp)
                .background(
                    Color.White,
                    shape = RoundedCornerShape(4.dp)
                )
                .border(
                    1.dp,
                    Color.Gray,
                    shape = RoundedCornerShape(12.dp)
                )
                .clickable {
                    onContentClicked()
                }
        )
        Icon(
            modifier = Modifier
                .size(28.dp)
                .align(Alignment.TopEnd)
                .padding(4.dp)
                .border(
                    1.dp,
                    Color.Gray,
                    shape = RoundedCornerShape(32.dp)
                )
                .clickable {
                    onDeleteImageClicked(imageSource)
                },
            painter = painterResource(id = R.drawable.ic_close),
            contentDescription = null,
            tint = Red400,
        )
        if (isThumbnail) {
            Text(
                text = "대표",
                style = Body2,
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.BottomStart)
                    .border(
                        1.dp,
                        Color.LightGray,
                        shape = RoundedCornerShape(4.dp)
                    )
                    .background(
                        Color.LightGray.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(4.dp)
                    )
                    .padding(horizontal = 4.dp)
            )
        }
    }
}

@Composable
private fun TradePostScreenTopBar(navigateUp: () -> Unit) {
    Row(
        modifier = Modifier
            .background(Indigo50)
            .fillMaxWidth()
            .height(56.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Done,
            contentDescription = "Navigate Up Button",
            modifier = Modifier
                .size(24.dp)
                .clickable {
                    navigateUp()
                }
                .padding(horizontal = 8.dp)
        )
        Text(
            text = "상품 등록"
        )
    }
}

@Composable
private fun TradePostScreenTradeInfo(
    title: MutableState<String>,
    category: MutableState<String>,
    price: MutableState<String>,
    description: MutableState<String>,
    categoryList: List<String>
) {
    Column {
        Column(modifier = Modifier.padding(vertical = 8.dp)) {
            Text(
                text = "제목",
                modifier = Modifier.padding(bottom = 8.dp)
            )
            TypingTextField(
                text = title.value,
                onValueChange = { title.value = it },
                hintText = "제목을 입력하세요",
                maxLines = 1,
                maxTextLength = 100,
                trailingIconContent = {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Clear button",
                        modifier = Modifier
                            .size(20.dp)
                            .clickable {
                                title.value = ""
                            }
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
            )
        }

        Row(modifier = Modifier.padding(bottom = 8.dp)) {

            Column(
                modifier = Modifier
                    .weight(2f)
                    .padding(end = 16.dp)
            ) {
                Text(
                    text = "가격",
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                TypingTextField(
                    text = price.value,
                    onValueChange = { newValue ->
                        if (newValue.all { it.isDigit() })
                            price.value = newValue
                    },
                    hintText = "가격을 입력하세요",
                    maxLines = 1,
                    maxTextLength = 100,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    ),
                    trailingIconContent = {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear button",
                            modifier = Modifier
                                .size(20.dp)
                                .clickable {
                                    price.value = "0"
                                }
                        )
                    },
                    modifier = Modifier
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "분류",
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                TradePostCategorySelector(categoryList, category.value)
            }
        }

        Column {
            Text(
                text = "상품 상세 정보",
                modifier = Modifier.padding(bottom = 8.dp)
            )
            TypingTextField(
                text = description.value,
                onValueChange = { description.value = it },
                hintText = "상품 정보를 입력해주세요 (최대 1,000자)",
                maxLines = 100,
                maxTextLength = 1000,
                trailingIconContent = {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Clear button",
                        modifier = Modifier
                            .size(20.dp)
                            .clickable {
                                description.value = ""
                            }
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            )
        }
    }
}

@Composable
private fun TradePostCategorySelector(
    categoryList: List<String>,
    category: String
) {

    val isDropDownExpanded = remember { mutableStateOf(false) }
    val itemIndex = remember {
        mutableIntStateOf(
            categoryList.indexOf(category).takeIf { it >= 0 } ?: 0
        )
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(Gray200)
            .border(1.dp, Color.Gray, shape = RoundedCornerShape(4.dp))
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = translateToKor(categoryList[itemIndex.intValue]),
                maxLines = 1,
                style = Body2,
                modifier = Modifier.padding(8.dp)
            )
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Search Button",
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
                        isDropDownExpanded.value = !isDropDownExpanded.value
                    }
            )
        }

        DropdownMenu(
            expanded = isDropDownExpanded.value,
            onDismissRequest = { isDropDownExpanded.value = false },
        ) {
            categoryList.forEachIndexed { index, category ->
                DropdownMenuItem(
                    text = { Text(text = translateToKor(category)) },
                    onClick = {
                        isDropDownExpanded.value = false
                        itemIndex.intValue = index
                    }
                )
            }
        }
    }
}

private fun translateToKor(engCategory: String): String {
    return when (engCategory) {
        "ELECTRONICS_IT" -> "전자기기/IT"
        "HOME_APPLIANCES" -> "가전제품"
        "FASHION_ACCESSORIES" -> "패션/액세서리"
        "ACCESSORIES" -> "액세서리"
        "BOOKS_EDUCATIONAL_MATERIALS" -> "서적/교육 자료"
        "STATIONERY_OFFICE_SUPPLIES" -> "문구/사무용품"
        "HOUSEHOLD_ITEMS" -> "생활용품"
        "KITCHEN_SUPPLIES" -> "주방용품"
        "FURNITURE_INTERIOR" -> "가구/인테리어"
        "SPORTS_LEISURE" -> "스포츠/레저"
        "ENTERTAINMENT_HOBBIES" -> "엔터테인먼트/취미"
        "OTHER" -> "기타"
        else -> "기타"
    }
}

@Composable
private fun TradePostScreenPostButton(onPostButtonClicked: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Blue400)
            .clickable {
                onPostButtonClicked()
            },
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = "올리기",
            modifier = Modifier.padding(16.dp),
            style = Headline3,
            color = Color.White
        )
    }
}

@Preview
@Composable
private fun TradePostScreenTradeInfoPreview() {
    val title = remember { mutableStateOf("제목제목") }
    val price = remember { mutableStateOf("1000") }
    val category = remember { mutableStateOf("OTHER") }
    val description =
        remember {
            mutableStateOf(
                "상품에 대한 자세한 정보입니다. 좀 길게 작성될 수도 있구요. " +
                        "그럴 때 어떻게 되는지 제가 직접 한번 해보겠습니다."
            )
        }
    TradePostScreenTradeInfo(
        title = title,
        price = price,
        category = category,
        description = description,
        categoryList = CategoryList.empty.categoryList
    )

}

@Preview
@Composable
private fun TradePostScreenPreview() {
    TradePostScreen(
        navController = rememberNavController(),
        argument = TradePostArgument(
            state = TradePostState.Init,
            event = MutableEventFlow(),
            intent = {},
            logEvent = { _, _ -> },
            coroutineContext = Dispatchers.IO
        ),
        data = TradePostData(
            categoryList = CategoryList.empty.categoryList
        )
    )
}

