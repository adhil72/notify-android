package org.notify.models

import org.json.JSONObject

class UserData(
    val username: String,
    val email: String,
    val name: String,
    val messagesSendToday: JSONObject,
    val messagesSendMonth: JSONObject,
    val lastAccess: String,
    val clients: String
)