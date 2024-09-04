package kr.linkerbell.boardlink.android.presentation.ui.main.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/* Top bar 초안
* 추후 다른 composable로 대체 예정
* */

@Composable
fun TemporaryTopBar(modifier: Modifier = Modifier){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp) //Android standard
            //.background(MaterialTheme.colorScheme.background)
            .background(Color(0xFF00BCD4)) // Temp
            .padding(16.dp),
        contentAlignment = Alignment.CenterStart
    ){
        Text(
            text = "BoardLink Logo",
            modifier = Modifier
                .border(1.dp, Color(0xFF000000))
                .padding(2.dp)
        )
    }

}

@Preview
@Composable
private fun TemporaryTopBarPreview(){
    TemporaryTopBar()
}
