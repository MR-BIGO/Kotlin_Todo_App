package com.example.finalsproject.models

import java.sql.Time

data class Todo(
    var id: String? = null,
    var title: String? = null,
    var description: String? = null,
    var isDone: Boolean? = null,
    var isFailed: Boolean? = null,
    //   var deadline: Time? = null
)
