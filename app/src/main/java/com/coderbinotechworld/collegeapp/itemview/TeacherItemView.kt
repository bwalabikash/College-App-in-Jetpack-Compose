package com.coderbinotechworld.collegeapp.itemview

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.rememberAsyncImagePainter
import com.coderbinotechworld.collegeapp.R
import com.coderbinotechworld.collegeapp.models.BannerModel
import com.coderbinotechworld.collegeapp.models.FacultyModel
import com.coderbinotechworld.collegeapp.models.NoticeModel
import com.coderbinotechworld.collegeapp.utills.Constant
import com.coderbinotechworld.collegeapp.utills.Constant.IS_ADMIN

@Composable
fun TeacherItemView(
    facultyModel: FacultyModel,
    delete: (facultyModel: FacultyModel) -> Unit
) {

    OutlinedCard(
        modifier = Modifier.padding(4.dp)
    ) {
        ConstraintLayout {
            val (image, delete) = createRefs()

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(modifier = Modifier.height(6.dp))

                Image(rememberAsyncImagePainter(model = facultyModel.imageUrl),
                    contentDescription = Constant.FACULTY,
                    modifier = Modifier
                        .height(120.dp)
                        .width(120.dp)
                        .clip(CircleShape),
                    contentScale = if (facultyModel.imageUrl == null) ContentScale.Fit else ContentScale.FillBounds
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = facultyModel.name!!,
                    modifier = Modifier
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )

                Text(
                    text = facultyModel.email!!,
                    modifier = Modifier
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )

                Text(
                    text = facultyModel.position!!,
                    modifier = Modifier
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )

            }

            if (IS_ADMIN) {
                Card(
                    modifier = Modifier
                        .constrainAs(delete) {
                            top.linkTo(parent.top)
                            end.linkTo(parent.end)
                        }
                        .padding(4.dp)
                        .clickable {
                            delete(facultyModel)
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

}
