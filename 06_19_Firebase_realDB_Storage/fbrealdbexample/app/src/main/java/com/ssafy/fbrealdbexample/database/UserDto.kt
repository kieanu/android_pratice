package com.ssafy.fbrealdbexample.database

//Firebase Realtime Database는 데이터를 역직렬화할 때 기본 생성자 (no-argument constructor)를 사용합니다.
// 따라서 com.ssafy.fbrealdbexample.database.UserDto 클래스에는 기본 생성자가 정의되어야 합니다.
// 해당 오류 메시지가 발생하는 이유는 UserDto 클래스에 기본 생성자가 없기 때문입니다.
data class UserDto(
    val name: String? = null, // 이름
    val age: Int? = null, // 나이
    val introduce: String? = null, // 자기소개
    val avatar: String? = null//  image http
) {
    var id: String = "default"

    constructor(id:String, name:String?, age: Int?, introduce: String?, avatar: String?): this(name, age, introduce, avatar){
        this.id = id
    }

    override fun toString(): String {
        return "$name $age $introduce"
    }
}
