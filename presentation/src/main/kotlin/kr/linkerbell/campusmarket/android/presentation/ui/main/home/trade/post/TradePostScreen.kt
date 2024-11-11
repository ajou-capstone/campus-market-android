package kr.linkerbell.campusmarket.android.presentation.ui.main.home.trade.post

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import kr.linkerbell.campusmarket.android.presentation.common.theme.Headline2
import kr.linkerbell.campusmarket.android.presentation.common.theme.Headline3
import kr.linkerbell.campusmarket.android.presentation.common.theme.Indigo100
import kr.linkerbell.campusmarket.android.presentation.common.theme.Indigo50
import kr.linkerbell.campusmarket.android.presentation.common.theme.Red400
import kr.linkerbell.campusmarket.android.presentation.common.theme.White
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.safeNavigateUp
import kr.linkerbell.campusmarket.android.presentation.common.view.BottomSheetScreen
import kr.linkerbell.campusmarket.android.presentation.common.view.DialogScreen
import kr.linkerbell.campusmarket.android.presentation.common.view.confirm.ConfirmButton
import kr.linkerbell.campusmarket.android.presentation.common.view.confirm.ConfirmButtonProperties
import kr.linkerbell.campusmarket.android.presentation.common.view.confirm.ConfirmButtonSize
import kr.linkerbell.campusmarket.android.presentation.common.view.confirm.ConfirmButtonType
import kr.linkerbell.campusmarket.android.presentation.common.view.image.PostImage
import kr.linkerbell.campusmarket.android.presentation.common.view.textfield.TypingTextField
import kr.linkerbell.campusmarket.android.presentation.model.gallery.GalleryImage
import kr.linkerbell.campusmarket.android.presentation.ui.main.common.gallery.GalleryScreen

@Composable
fun TradePostScreen(
    navController: NavController,
    argument: TradePostArgument,
    data: TradePostData
) {
    val (state, event, intent, logEvent, coroutineContext) = argument
    val scope = rememberCoroutineScope() + coroutineContext

    var title by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("0") }
    var category by remember { mutableStateOf("OTHER") }
    var description by remember { mutableStateOf("") }
    var imageList: List<GalleryImage> by remember { mutableStateOf(emptyList()) }

    var isGalleryShowing by remember { mutableStateOf(false) }
    var isBottomSheetVisible by remember { mutableStateOf(false) }
    var isConfirmButtonVisible by remember { mutableStateOf(false) }

    var bottomSheetContent by remember { mutableStateOf("Bottom Sheet Content") }

    val validateContentAndPost = {
        var isValid = true
        when {
            title.isBlank() -> {
                bottomSheetContent = "제목을 입력해주세요"
                isBottomSheetVisible = true
                isValid = false
            }

            description.isBlank() -> {
                bottomSheetContent = "상품 상세 정보를 입력해주세요"
                isBottomSheetVisible = true
                isValid = false
            }
        }
        if (isValid) {
            isConfirmButtonVisible = true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        TradePostScreenTopBar(navigateUp = { navController.safeNavigateUp() })

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "사진",
                    style = Headline3,
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
                                image = image,
                                isThumbnail = (index == 0),
                                onDeleteImageClicked = { imageToBeDeleted ->
                                    imageList = imageList.filter { imageToBeDeleted.id != it.id }
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
                    categoryList = listOf("ALL") + data.categoryList,
                    changeTitle = { changedValue ->
                        title = changedValue
                    },
                    changeDescription = { changedValue ->
                        description = changedValue
                    },
                    changeCategory = { changedValue ->
                        category = changedValue
                    },
                    changePrice = { changedValue ->
                        price = changedValue
                    }
                )
            }
            Spacer(Modifier.padding(vertical = 32.dp))
            TradePostScreenPostButton(onPostButtonClicked = validateContentAndPost)
        }
    }

    if (isGalleryShowing) {
        GalleryScreen(
            navController = navController,
            onDismissRequest = { isGalleryShowing = false },
            onResult = { addedImage ->
                imageList += addedImage
            },
            minSelectCount = 1,
            maxSelectCount = 5 - imageList.size
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

    if (isConfirmButtonVisible) {
        DialogScreen(
            title = "등록되었습니다!",
            isCancelable = false,
            onConfirm = {
                argument.intent(
                    TradePostIntent.PostNewTrade(
                        title = title,
                        description = description,
                        price = price.toIntOrNull() ?: 0,
                        category = category,
                        imageList = imageList
                    )
                )
                //navController.navigate(to TradeInfoPAge)
            },
            onDismissRequest = {
                isConfirmButtonVisible = false
            }
        )
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
    image: GalleryImage,
    isThumbnail: Boolean,
    onDeleteImageClicked: (GalleryImage) -> Unit,
) {
    Box(Modifier.padding(end = 16.dp)) {
        PostImage(
            data = image.filePath,
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
                    onDeleteImageClicked(image)
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
            painter = painterResource(R.drawable.ic_chevron_left),
            contentDescription = "Navigate Up Button",
            modifier = Modifier
                .size(40.dp)
                .clickable {
                    navigateUp()
                }
                .padding(horizontal = 8.dp)
        )
        Text(
            text = "상품 등록",
            style = Headline2
        )
    }
}

@Composable
private fun TradePostScreenTradeInfo(
    title: String,
    category: String,
    price: String,
    description: String,
    categoryList: List<String>,
    changeTitle: (String) -> Unit,
    changeDescription: (String) -> Unit,
    changePrice: (String) -> Unit,
    changeCategory: (String) -> Unit
) {
    Column {
        Column(modifier = Modifier.padding(vertical = 16.dp)) {
            Text(
                text = "제목",
                style = Headline3,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            TypingTextField(
                text = title,
                onValueChange = { changeTitle(it) },
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
                                changeTitle("")
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
                    style = Headline3,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                TypingTextField(
                    text = price,
                    onValueChange = { newValue ->
                        if (newValue.all { it.isDigit() })
                            changePrice(newValue)
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
                                    changePrice(price)
                                }
                        )
                    },
                    modifier = Modifier
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "분류",
                    style = Headline3,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                TradePostScreenCategorySelector(
                    categoryList, category,
                    changeCategory = changeCategory
                )
            }
        }
        Column {
            Text(
                text = "상품 상세 정보",
                style = Headline3,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            TypingTextField(
                text = description,
                onValueChange = { changeDescription(it) },
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
                                changeDescription("")
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
private fun TradePostScreenCategorySelector(
    categoryList: List<String>,
    category: String,
    changeCategory: (String) -> Unit
) {
    var isDropDownExpanded by remember { mutableStateOf(false) }
    var itemIndex by remember {
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
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    isDropDownExpanded = !isDropDownExpanded
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = translateToKor(categoryList[itemIndex]),
                maxLines = 1,
                style = Body2,
                modifier = Modifier.padding(8.dp)
            )
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Search Button",
                modifier = Modifier
                    .size(24.dp)
            )
        }
        DropdownMenu(
            expanded = isDropDownExpanded,
            onDismissRequest = { isDropDownExpanded = false },
        ) {
            categoryList.forEachIndexed { index, category ->
                DropdownMenuItem(
                    text = { Text(text = translateToKor(category)) },
                    onClick = {
                        isDropDownExpanded = false
                        itemIndex = index
                        changeCategory(category)
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
            text = "등록하기",
            modifier = Modifier.padding(16.dp),
            style = Headline3,
            color = Color.White
        )
    }
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
            categoryList = CategoryList.empty.categoryList,
        )
    )
}
