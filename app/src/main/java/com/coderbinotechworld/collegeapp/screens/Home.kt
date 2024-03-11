package com.coderbinotechworld.collegeapp.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.coderbinotechworld.collegeapp.itemview.NoticeItemView
import com.coderbinotechworld.collegeapp.viewmodel.BannerViewModel
import com.coderbinotechworld.collegeapp.viewmodel.CollegeInfoViewModel
import com.coderbinotechworld.collegeapp.viewmodel.NoticeViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.delay
import kotlinx.coroutines.yield

@OptIn(ExperimentalFoundationApi::class, ExperimentalPagerApi::class)
@Composable
fun Home() {

    // banner view model
    val bannerViewModel: BannerViewModel = viewModel()
    bannerViewModel.getBanner()
    val bannerList by bannerViewModel.bannerList.observeAsState(null)

    // College Info view model
    val collegeInfoViewModel: CollegeInfoViewModel = viewModel()
    collegeInfoViewModel.getCollegeInfo()
    val collegeInfo by collegeInfoViewModel.collegeInfo.observeAsState(null)

    // notice view model
    val noticeViewModel: NoticeViewModel = viewModel()
    noticeViewModel.getNotice()
    val noticeList by noticeViewModel.noticeList.observeAsState(null)


    val pagerState = rememberPagerState(0)
    val imageSlider = ArrayList<AsyncImagePainter>()

    if (bannerList != null) {
        bannerList!!.forEach {
            imageSlider.add(rememberAsyncImagePainter(model = it.url))
        }
    }

    LaunchedEffect(Unit) {
        try {
            while (true) {
                yield()
                delay(2600)
                pagerState.animateScrollToPage(page = (pagerState.currentPage + 1) % pagerState.pageCount)
            }
            } catch (_: Exception) {
        }
    }


    LazyColumn(
        modifier = Modifier.padding(8.dp)
    ) {
        item {
            HorizontalPager(
                count = imageSlider.size,
                 state = pagerState
            ) {pager ->
                Card(
                    modifier = Modifier.height(210.dp)
                ) {
                    Image(
                        painter = imageSlider[pager],
                        contentDescription = "banner",
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }

        item {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                HorizontalPagerIndicator(
                    pagerState = pagerState,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }

        item {
            if (collegeInfo != null) {
                Text(
                    text = collegeInfo!!.name!!,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = collegeInfo!!.desc!!,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = collegeInfo!!.address!!,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = collegeInfo!!.websiteLink!!,
                    fontWeight = FontWeight.Normal,
                    color = Color.Blue,
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Notices",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

            }
        }

        items(noticeList?: emptyList()) {
            NoticeItemView(
                noticeModel = it,
                delete = {docId ->
                    noticeViewModel.deleteNotice(docId)
                }
            )
        }

    }

}