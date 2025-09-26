package com.example.myapplication.features.profile.domain.model
import kotlin.jvm.JvmInline;

@JvmInline
value class Picture(val value: String) {
    init {
        require(value.isNotEmpty()){
            "Picture must not be empty"
        }
    }

    override fun toString(): String = value
}