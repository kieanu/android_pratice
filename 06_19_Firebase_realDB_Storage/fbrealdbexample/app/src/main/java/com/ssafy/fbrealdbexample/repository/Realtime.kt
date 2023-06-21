package com.ssafy.fbrealdbexample.repository

import android.util.Log
import com.google.firebase.database.*
import com.ssafy.fbrealdbexample.ApplicationClass
import com.ssafy.fbrealdbexample.database.UserDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class Realtime {
    val realDbRef = FirebaseDatabase.getInstance().getReference("users")

    fun fetchData(): Flow<List<UserDto>> {
        return dbChanged()
            .map { snapshot -> // trySend에서 보낸 snapshot을 받음
                // snapshot을 UserDto 타입으로 변경
                snapshot.children.mapNotNull { it.getValue(UserDto::class.java) }
            }
            .flowOn(Dispatchers.IO) // IO 코루틴 영역(백그라운드)에서 flow 데이터 처리하도록 context변경
    }
    // callbackFlow 는 Flow<T> 반환 (callback 을 Flow로 변환하는 역할 즉 콜백 함수(Lisnter)를 구현한다)
    private fun dbChanged() = callbackFlow {
        val valueEventListener = object : ValueEventListener {
            // firebase db 변경될 때 마다 호출
            override fun onDataChange(snapshot: DataSnapshot) {
                trySend(snapshot) // flow에 snapshot 전달
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("싸피", "onCancelled: 플로우 변화 감지 에러")
                close(error.toException())
            }
        }

        realDbRef.addValueEventListener(valueEventListener)

        // callbackFlow의 Flow 완료시 수행되는 함수
        awaitClose {
            realDbRef.removeEventListener(valueEventListener)
            Log.d("싸피", "dbChanged: 콜백 플로우 완료 : 리스너 해제")
        }
    }
}