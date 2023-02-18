package com.feveral.composeexample.models

data class Meme (
    var id: String,
    var description: String,
    var tags: List<String>,
    var image: MemeImage,
    var user: MemeUser
)