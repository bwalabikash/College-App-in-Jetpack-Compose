package com.coderbinotechworld.collegeapp.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.coderbinotechworld.collegeapp.models.FacultyModel
import com.coderbinotechworld.collegeapp.utills.Constant.FACULTY
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import java.util.UUID

class FacultyViewModel: ViewModel() {

    private val facultyRef = Firebase.firestore.collection(FACULTY)
    private val storageRef = Firebase.storage.reference

    private val _isPostedTeacher = MutableLiveData<Boolean>()
    val isPostedTeacher: LiveData<Boolean> = _isPostedTeacher

    private val _isPostedCategory = MutableLiveData<Boolean>()
    val isPostedCategory: LiveData<Boolean> = _isPostedCategory

    private val _isDeletedTeacher = MutableLiveData<Boolean>()
    val isDeletedTeacher: LiveData<Boolean> = _isDeletedTeacher

    private val _isDeletedCategory = MutableLiveData<Boolean>()
    val isDeletedCategory: LiveData<Boolean> = _isDeletedCategory

    private val _facultyList= MutableLiveData<List<FacultyModel>>()
    val facultyList: LiveData<List<FacultyModel>> = _facultyList

    private val _categoryList= MutableLiveData<List<String>>()
    val categoryList: LiveData<List<String>> = _categoryList

    fun saveFaculty(uri: Uri, name: String, email: String, position: String, catName: String) {
        _isPostedTeacher.postValue(false)

        val randomUid = UUID.randomUUID().toString()
        val imageRef = storageRef.child("$FACULTY/${randomUid}.jpg")
        val uploadTask = imageRef.putFile(uri)

        uploadTask.addOnSuccessListener {
            imageRef.downloadUrl.addOnSuccessListener {
                saveFacultyUrl(it.toString(), randomUid, name, email, position, catName)
            }
        }

    }

    fun saveFacultyCategory(category: String) {

        val map = mutableMapOf<String, String>()
        map["catName"] = category

        facultyRef.document(category).set(map)
            .addOnSuccessListener {
                _isPostedCategory.postValue(true)
            }
            .addOnFailureListener {
                _isPostedCategory.postValue(false)
            }

    }

    private fun saveFacultyUrl(
        imageUrl: String,
        docId: String,
        name: String,
        email: String,
        position: String,
        catName: String
    ) {

        val map = mutableMapOf<String, String>()
        map["imageUrl"] = imageUrl
        map["docId"] = docId
        map["name"] = name
        map["email"] = email
        map["position"] = position
        map["catName"] = catName

        facultyRef.document(catName).collection("teacher").document(docId).set(map)
            .addOnSuccessListener {
                _isPostedTeacher.postValue(true)
            }
            .addOnFailureListener {
                _isPostedTeacher.postValue(false)
            }

    }

    fun getFacultyCategory() {
        facultyRef.get()
            .addOnSuccessListener {
                val list = mutableListOf< String >()

                for (doc in it) {
                    list.add(doc.get("catName").toString())
                }

                _categoryList.postValue(list)
            }
    }

    fun getFaculty(catName: String) {
        facultyRef.document(catName).collection("teacher").get()
            .addOnSuccessListener {
                val list = mutableListOf<FacultyModel>()

                for (doc in it) {
                    list.add(doc.toObject(FacultyModel::class.java))
                }

                _facultyList.postValue(list)
            }
    }

    fun deleteFacultyCategory(category: String) {
        facultyRef.document(category).delete()
            .addOnSuccessListener {
                _isDeletedCategory.postValue(true)
            }
            .addOnFailureListener {
                _isDeletedCategory.postValue(false)
            }
    }

    fun deleteFaculty(facultyModel: FacultyModel) {
        facultyRef.document(facultyModel.catName!!).collection("teacher").document(facultyModel.docId!!).delete()
            .addOnSuccessListener {
                Firebase.storage.getReferenceFromUrl(facultyModel.imageUrl!!).delete()
                _isDeletedTeacher.postValue(true)
            }
            .addOnFailureListener {
                _isDeletedTeacher.postValue(false)
            }
    }

}