package com.example
import kotlinx.serialization.Serializable


data class Task (
    val id      : Int,
    val title   : String,
    val done    : Boolean
)

