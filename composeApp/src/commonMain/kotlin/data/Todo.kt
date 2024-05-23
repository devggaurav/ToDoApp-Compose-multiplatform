package data

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import kotlinx.serialization.Serializable
import org.mongodb.kbson.ObjectId


//
// Created by Code For Android on 23/05/24.
// Copyright (c) 2024 CFA. All rights reserved.
//


class Todo : RealmObject {
    var title: String = ""
    var description: String? = ""
    var isDone: Boolean = false

    @PrimaryKey
    var _id: ObjectId = ObjectId()
}



