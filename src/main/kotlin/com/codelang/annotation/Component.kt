package com.codelang.annotation

annotation class Component(
    val name: String,
    val version: String,
    val dependency: String,
    val verifiedContainer: Array<String> = arrayOf(),
    val verifiedProtocol: Array<String> = arrayOf()
)
