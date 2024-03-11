package com.coderbinotechworld.collegeapp.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.coderbinotechworld.collegeapp.models.BannerModel
import com.coderbinotechworld.collegeapp.models.CollegeInfoModel
import com.coderbinotechworld.collegeapp.utills.Constant.COLLEGE_INFO
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.storage
import java.util.UUID

class CollegeInfoViewModel: ViewModel() {

    private val collegeInfoRef = Firebase.firestore.collection(COLLEGE_INFO)
    private val storageRef = Firebase.storage.reference

    private val _isPosted = MutableLiveData<Boolean>()
    val isPosted: LiveData<Boolean> = _isPosted

    private val _collegeInfo= MutableLiveData<CollegeInfoModel>()
    val collegeInfo: LiveData<CollegeInfoModel> = _collegeInfo

    fun saveImage(uri: Uri, name: String, address: String, desc: String, websiteLink: String) {
        _isPosted.postValue(false)

        val randomUid = UUID.randomUUID().toString()
        val imageRef = storageRef.child("$COLLEGE_INFO/${randomUid}.jpg")
        val uploadTask = imageRef.putFile(uri)

        uploadTask.addOnSuccessListener {
            imageRef.downloadUrl.addOnSuccessListener {
                saveImageUrl(it.toString(), name, address, desc, websiteLink)
            }
        }

    }

    fun saveImageUrl(
        imageUrl: String,
        name: String,
        address: String,
        desc: String,
        websiteLink: String
    ) {

        val map = mutableMapOf<String, Any>()
        map["imageUrl"] = imageUrl
        map["name"] = name
        map["address"] = address
        map["desc"] = desc
        map["websiteLink"] = websiteLink

        collegeInfoRef.document("college_info").update(map)
            .addOnSuccessListener {
                _isPosted.postValue(true)
            }
            .addOnFailureListener {
                _isPosted.postValue(false)
            }

    }

    fun getCollegeInfo() {
        collegeInfoRef.document("college_info").get()
            .addOnSuccessListener {
                _collegeInfo.postValue(
                    CollegeInfoModel(
                        imageUrl = it.data!!["imageUrl"].toString(),
                        name = it.data!!["name"].toString(),
                        address = it.data!!["address"].toString(),
                        desc = it.data!!["desc"].toString(),
                        websiteLink = it.data!!["websiteLink"].toString(),
                    )
                )

            }
    }

}