package com.example.myapplication.features.profile.domain.model

import kotlin.jvm.JvmInline;

@JvmInline
value class Username(val value: String) {
    init {
        require(value.isNotEmpty()){
            "Username must not be empty"
        }
        require(!value.contains(" "))
    }

    override fun toString(): String = value
}