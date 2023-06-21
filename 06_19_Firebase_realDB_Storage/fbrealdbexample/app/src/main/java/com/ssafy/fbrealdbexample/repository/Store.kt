package com.ssafy.fbrealdbexample.repository

import android.net.Uri
import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class Store {
    // Firebase Storage의 레퍼런스 가져오기
    val storageReference = FirebaseStorage.getInstance().reference

    suspend fun uploadImageToFirebaseStorage(imageUri: Uri): String = suspendCoroutine {
        val imagesRef = storageReference.child("images/${imageUri.lastPathSegment}")
        val uploadTask = imagesRef.putFile(imageUri)

        uploadTask.addOnSuccessListener { _ ->
            // 이미지 업로드 성공 시 처리할 로직 작성
            // 예를 들어, 다운로드 URL을 가져와서 활용할 수 있습니다.
            imagesRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                Log.d("싸피", "Storage 이미지 업로드 성공 : $downloadUrl")
                it.resume(downloadUrl.toString())
            }
        }.addOnFailureListener { exception ->
            Log.d("싸피", "Storage 이미지 업로드 실패 : $exception")
            it.resumeWithException(exception)
        }
    }
}