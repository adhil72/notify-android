package org.notify.helpers

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import org.json.JSONObject
import org.notify.models.UserData


class Database(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "Notify.db"
        private const val TABLE_NAME = "user_data"

        // Define column names
        private const val COLUMN_USERNAME = "username"
        private const val COLUMN_EMAIL = "email"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_MESSAGES_SEND_TODAY = "messagesSendToday"
        private const val COLUMN_MESSAGES_SEND_MONTH = "messagesSendMonth"
        private const val COLUMN_LAST_ACCESS = "lastAccess"
        private const val COLUMN_CLIENTS = "clients"
    }


    override fun onCreate(db: SQLiteDatabase?) {
        val createTable =
            ("CREATE TABLE $TABLE_NAME ($COLUMN_USERNAME TEXT PRIMARY KEY, $COLUMN_EMAIL TEXT, $COLUMN_NAME TEXT, $COLUMN_MESSAGES_SEND_TODAY TEXT, $COLUMN_MESSAGES_SEND_MONTH TEXT, $COLUMN_LAST_ACCESS TEXT, $COLUMN_CLIENTS TEXT)")
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertUserData(userData: UserData) {
        val db = this.writableDatabase

        // Delete the previous entry (if any)
        db.delete(TABLE_NAME, null, null)

        val values = ContentValues()
        values.put(COLUMN_USERNAME, userData.username)
        values.put(COLUMN_EMAIL, userData.email)
        values.put(COLUMN_NAME, userData.name)
        values.put(COLUMN_MESSAGES_SEND_TODAY, userData.messagesSendToday.toString())
        values.put(COLUMN_MESSAGES_SEND_MONTH, userData.messagesSendMonth.toString())
        values.put(COLUMN_LAST_ACCESS, userData.lastAccess)
        values.put(COLUMN_CLIENTS, userData.clients)

        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    @SuppressLint("Range")
    fun getUserData(): UserData? {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_NAME, null, null, null, null, null, null
        )

        var userData: UserData? = null
        if (cursor.moveToFirst()) {
            val usernameIndex = cursor.getColumnIndex(COLUMN_USERNAME)
            if (usernameIndex != -1) {
                userData = UserData(
                    cursor.getString(usernameIndex),
                    cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_NAME)),
                    JSONObject(cursor.getString(cursor.getColumnIndex(COLUMN_MESSAGES_SEND_TODAY))),
                    JSONObject(cursor.getString(cursor.getColumnIndex(COLUMN_MESSAGES_SEND_MONTH))),
                    cursor.getString(cursor.getColumnIndex(COLUMN_LAST_ACCESS)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_CLIENTS))
                )

            }
        }
        cursor.close()
        return userData
    }

}