package com.ssafy.swipeexample.ui.main

import androidx.lifecycle.ViewModel
import com.ssafy.swipeexample.CardItem

class MainViewModel : ViewModel() {
    var cur:Int = 0
    var size:Int = 0
    private var _cardList:MutableList<CardItem> = mutableListOf()
    val cardList:List<CardItem>
        get() = _cardList

    init{
        _cardList.add(CardItem(0,"1번"))
        _cardList.add(CardItem(1,"2번"))
        _cardList.add(CardItem(2,"3번"))
        _cardList.add(CardItem(3,"4번"))
        _cardList.add(CardItem(4,"5번"))
        _cardList.add(CardItem(5,"6번"))
        _cardList.add(CardItem(6,"7번"))
        _cardList.add(CardItem(7,"8번"))
        _cardList.add(CardItem(8,"9번"))
        _cardList.add(CardItem(9,"10번"))
        cur = 0;
        size = _cardList.size
    }
}