package com.example.myapplication.features.profile.domain.model

import com.example.myapplication.features.login.domain.model.Email

@JvmInline
value class Email (val value: String){
    companion object{
        fun create(raw: String): com.example.myapplication.features.profile.domain.model.Email {
            require(raw.isNotEmpty()){
                "Email must not be empty"
            }

            val normalized = raw.trim().lowercase()

            require(normalized.contains("@")){
                "Email must contain @"
            }
            return com.example.myapplication.features.profile.domain.model.Email(normalized)
        }
    }
    override fun toString(): String = value
}