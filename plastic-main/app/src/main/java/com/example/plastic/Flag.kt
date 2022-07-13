package com.example.plastic

object Flag {
    private var f = 0
    fun change(){
        f = (f+1)%2
    }
    fun getInstance():Boolean{
        return f == 1
    }



}