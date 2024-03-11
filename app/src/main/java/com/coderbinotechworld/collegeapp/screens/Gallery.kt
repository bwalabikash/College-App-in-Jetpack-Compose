package com.coderbinotechworld.collegeapp.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.coderbinotechworld.collegeapp.itemview.GalleryItemView
import com.coderbinotechworld.collegeapp.viewmodel.GalleryViewModel

@Composable
fun Gallery() {

    // gallery view model
    val galleryViewModel: GalleryViewModel = viewModel()
    galleryViewModel.getGallery()
    val galleryList by galleryViewModel.galleryList.observeAsState(null)


    LazyColumn {

        items(galleryList?: emptyList()) {
            GalleryItemView(it,
                delete = {category ->
                    galleryViewModel.deleteGallery(category)
                },
                deleteImage = {imageUrl, cat ->
                    galleryViewModel.deleteImage(cat, imageUrl)
                }
            )
        }
    }

}