package com.ssafy.fbrealdbexample

import android.app.Application
import android.net.Uri
import android.util.Log
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference
import com.ssafy.fbrealdbexample.repository.Realtime
import com.ssafy.fbrealdbexample.repository.Store

class ApplicationClass : Application() {
    companion object {
        val fbRealDb: Realtime by lazy {
            Realtime()
        }

        val fbStorage: Store by lazy {
            Store()
        }
    }
}