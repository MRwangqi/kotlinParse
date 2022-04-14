package com.codelang.annotation

annotation class Compo(    val name: String,
                           val version: String,
                           val dependency: String,
                           val verifiedContainer: Array<String> = arrayOf()
)
