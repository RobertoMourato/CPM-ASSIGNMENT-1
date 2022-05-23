package org.feup.apm.lunchlist4.entities

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

data class Token(
    val token: String,
    val requestDate : String
)

class TokenDbHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
        private object TokenEntry: BaseColumns{
            const val  TABLE_NAME = "token"
            const val COLUMN_NAME_REQUEST_DATE = "requestDate"
            const val COLUMN_NAME_TOKEN = "token"
        }

    private val SQL_CREATE_TABLE = "CREATE TABLE ${TokenEntry.TABLE_NAME} (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY, " +
            "${TokenEntry.COLUMN_NAME_REQUEST_DATE} TEXT NOT NULL, " +
            "${TokenEntry.COLUMN_NAME_TOKEN} TOKEN NOT NULL)"

    private val SQL_DROP_TABLE = "DROP TABLE IF EXISTS ${TokenEntry.TABLE_NAME}"

    private val SQL_SELECT_ALL = "SELECT * FROM ${TokenEntry.TABLE_NAME}"

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_CREATE_TABLE)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    fun clearTable() {
        writableDatabase.delete(TokenEntry.TABLE_NAME, null, null)
    }

    fun cursorToProduct(cursor: Cursor): Token?{
        return try {
            Token(
                cursor.getString(1),
                cursor.getString(2)
            )
        } catch (e: Exception){
            println(e)
            null
        }
    }

    fun insert(token: Token): Long {
        val cv = ContentValues()
        cv.put(TokenEntry.COLUMN_NAME_TOKEN, token.token)
        cv.put(TokenEntry.COLUMN_NAME_REQUEST_DATE, token.requestDate)
        return writableDatabase.insert(TokenEntry.TABLE_NAME, null, cv)
    }

    fun delete(token: Token): Int {
        return writableDatabase.delete(
            TokenEntry.TABLE_NAME,
            "${TokenEntry.COLUMN_NAME_TOKEN}=? AND ${TokenEntry.COLUMN_NAME_REQUEST_DATE}=?",
            arrayOf(
                token.token,
                token.requestDate
            )
        )
    }
}