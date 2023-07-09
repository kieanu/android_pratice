package com.ssafy.livebroadcast.repository.remote

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.snapshots
import com.google.firebase.firestore.ktx.toObjects
import com.ssafy.livebroadcast.dto.Room
import kotlinx.coroutines.flow.map

class Remote {
    // Firestore 저장소
    val fireStore = FirebaseFirestore.getInstance()


    // 데이터 추가 - "users" , "room" 컬렉션(최상위) - document(객체) - field(프로퍼티)
    private val usersCollection = fireStore.collection("users")
    private val roomCollection = fireStore.collection("room")

    // return Flow<List<Room>>
    fun getRoom() = roomCollection.snapshots().map {
        Log.d("싸피 get", "getRoom: ${it.toObjects<Room>().toList()}")
        it.toObjects<Room>()
    }


    fun addUser(nickName: String, avatar: String) {
        val user = hashMapOf(
            "nickName" to nickName,
            "avatar" to avatar
        )

        usersCollection.add(user)
            .addOnSuccessListener {
                    documentReference ->
                Log.d("FireStore 통신 성공", "addUser: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                // 로깅
                Log.d("FireStore 통신 에러", "addUser: $e")
            }
    }

    fun addRoom(roomInfo: Room) {
        val room = hashMapOf(
            "title" to roomInfo.title,
            "remainTime" to roomInfo.remainTime
        )

        roomCollection.add(room)
            .addOnSuccessListener {
                    documentReference ->
                Log.d("FireStore 통신 성공", "addRoom: ${documentReference.id}")
                val documentId = documentReference.id
                // 발급된 문서 ID를 필드에 저장
                val updatedData = hashMapOf("documentId" to documentId)
                documentReference.update(updatedData as Map<String, Any>)
                    .addOnSuccessListener {
                        Log.d("FireStore 통신 성공", "Document ID saved successfully!")
                    }
                    .addOnFailureListener { exception ->
                        Log.e("FireStore 통신 에러", "Error saving document ID", exception)
                    }
            }
            .addOnFailureListener { e ->
                // 로깅
                Log.d("FireStore 통신 에러", "addRoom: $e")
            }
    }

    fun delRoom(roomInfo: Room) {
        val documentReference = roomCollection.document(roomInfo.documentId)

        documentReference.delete()
            .addOnSuccessListener {
                Log.d("FireStore 삭제 성공", "Document successfully deleted!")
            }
            .addOnFailureListener { exception ->
                Log.e("FireStore 삭제 실패", "Error deleting document", exception)
            }
    }
}