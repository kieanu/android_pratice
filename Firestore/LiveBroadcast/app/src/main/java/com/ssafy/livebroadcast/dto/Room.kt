package com.ssafy.livebroadcast.dto

data class Room  (
    val id: Int = 0,
    val thumbnail: String = "",
    val title: String = "",
    val topic: String = "",
    val remainTime: Long = 0L,
    var documentId: String = ""
) {
    constructor(thumbnail : String, title : String, topic : String, remainTime : Long) : this(0,thumbnail,title,topic,remainTime,"")
}