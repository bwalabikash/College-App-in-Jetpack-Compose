package com.coderbinotechworld.collegeapp.itemview

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.rememberAsyncImagePainter
import com.coderbinotechworld.collegeapp.R
import com.coderbinotechworld.collegeapp.models.BannerModel

@Composable
fun BannerItemView(bannerModel: BannerModel, delete: (bannerModel: BannerModel) -> Unit) {

    OutlinedCard(
        modifier = Modifier.padding(4.dp)
    ) {
        ConstraintLayout {
            val (image, delete) = createRefs()

            Image(
                painter = rememberAsyncImagePainter(model = bannerModel.url),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp),
                contentScale = ContentScale.FillBounds
            )

            Card(
                modifier = Modifier
                    .constrainAs(delete) {
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                    }
                    .padding(4.dp)
                    .clickable {
                        delete(bannerModel)
                    }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.delete),
                    contentDescription = null,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }

}

@Preview(showSystemUi = true)
@Composable
fun BannerView() {
//    BannerItemView(bannerModel = BannerModel())
}