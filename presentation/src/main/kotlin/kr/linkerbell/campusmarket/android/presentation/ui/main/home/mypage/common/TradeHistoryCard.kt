package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.common

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.text.DecimalFormat
import kr.linkerbell.campusmarket.android.domain.model.feature.mypage.RecentTrade
import kr.linkerbell.campusmarket.android.presentation.common.theme.Body1
import kr.linkerbell.campusmarket.android.presentation.common.theme.Gray200
import kr.linkerbell.campusmarket.android.presentation.common.theme.Headline3
import kr.linkerbell.campusmarket.android.presentation.common.theme.White
import kr.linkerbell.campusmarket.android.presentation.common.view.image.PostImage
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.trade.common.TradeItemStatus

@Composable
internal fun TradeHistoryCard(
    recentTrade: RecentTrade,
    onClicked: () -> Unit
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .fillMaxWidth()
            .background(White)
            .clickable {
                onClicked()
            }
    ) {
        PostImage(
            data = recentTrade.thumbnail,
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(8.dp))
        )
        Spacer(modifier = Modifier.padding(4.dp))
        Column(
            modifier = Modifier.height(80.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = recentTrade.title,
                style = Headline3,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )
            Column{
                Text(
                    text = "${DecimalFormat("#,###").format(recentTrade.price)} 원",
                    style = Body1,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(modifier = Modifier.padding(4.dp))
                TradeItemStatus(isSold = recentTrade.isSold)
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
private fun TradeHistoryCardPreview() {
    TradeHistoryCard(
        RecentTrade(
            id = 123L,
            title = "Example Title",
            price = 5000,
            thumbnail = "",
            isSold = false
        ),
        onClicked = {}
    )
}
