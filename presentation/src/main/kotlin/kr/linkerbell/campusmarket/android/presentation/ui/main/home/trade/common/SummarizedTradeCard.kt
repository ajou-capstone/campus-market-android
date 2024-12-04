package kr.linkerbell.campusmarket.android.presentation.ui.main.home.trade.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.text.DecimalFormat
import kr.linkerbell.campusmarket.android.domain.model.feature.trade.SummarizedTrade
import kr.linkerbell.campusmarket.android.presentation.common.theme.Black
import kr.linkerbell.campusmarket.android.presentation.common.theme.Blue400
import kr.linkerbell.campusmarket.android.presentation.common.theme.Body1
import kr.linkerbell.campusmarket.android.presentation.common.theme.Caption2
import kr.linkerbell.campusmarket.android.presentation.common.theme.Gray200
import kr.linkerbell.campusmarket.android.presentation.common.theme.Headline3
import kr.linkerbell.campusmarket.android.presentation.common.view.image.PostImage

@Composable
internal fun SummarizedTradeCard(
    item: SummarizedTrade,
    onItemCardClicked: (Long) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .clickable {
                onItemCardClicked(item.itemId)
            },
    ) {
        PostImage(
            data = item.thumbnail,
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(8.dp))
        )
        Spacer(modifier = Modifier.padding(4.dp))
        Column(
            modifier = Modifier.height(100.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = item.title,
                style = Headline3,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )
            Column {
                Text(
                    text = item.nickname,
                    style = Caption2,
                    color = Black
                )
                Text(
                    text = "${item.chatCount} 명이 대화중",
                    style = Caption2,
                    color = Black
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(min = 100.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        TradeItemStatus(isSold = item.itemStatus == "SOLDOUT")
                        Spacer(modifier = Modifier.padding(4.dp))
                        Text(
                            text = "${DecimalFormat("#,###").format(item.price)} 원",
                            style = Body1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                    Row(
                        modifier = Modifier.padding(start = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val favIcon =
                            if (item.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder
                        Icon(
                            imageVector = favIcon,
                            tint = Blue400,
                            contentDescription = "isLike",
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.padding(2.dp))
                        Text(item.likeCount.toString())
                    }
                }
            }
        }
    }
    HorizontalDivider(
        thickness = 1.dp,
        color = Gray200,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Preview
@Composable
private fun SummarizedTradeCardPreview() {
    SummarizedTradeCard(
        item = SummarizedTrade(
            itemId = 2L,
            userId = 2L,
            nickname = "장성혁",
            thumbnail = "https://picsum.photos/200",
            title = "콜라 팝니다",
            price = 10000000,
            chatCount = 5,
            likeCount = 2,
            itemStatus = "",
            isLiked = false
        ),
        onItemCardClicked = {}
    )
}