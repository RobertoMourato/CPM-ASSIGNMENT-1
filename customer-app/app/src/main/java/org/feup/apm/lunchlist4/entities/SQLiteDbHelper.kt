package org.feup.apm.lunchlist4.entities

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import android.provider.BaseColumns
import androidx.annotation.RequiresApi
import com.google.gson.annotations.SerializedName
import org.feup.apm.lunchlist4.crypto.decrypt
import org.feup.apm.lunchlist4.httpRequests.Product
import java.lang.Exception
import java.net.URLDecoder
import java.net.URLEncoder
import java.util.*


// If you change the database schema, you must increment the database version.
const val DATABASE_VERSION = 2
const val DATABASE_NAME = "ACMEShoppingCart.db"

data class Token(
    @SerializedName("token") val token: String,
    val requestDate: String?
)


class SQLiteDbHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    // Table contents are grouped together in an anonymous object.
    private object ProductEntry : BaseColumns {
        const val TABLE_NAME = "product"
        const val COLUMN_NAME_BARCODE = "barcode"
        const val COLUMN_NAME_MODEL = "model"
        const val COLUMN_NAME_MANUFACTURER = "manufacturer"
        const val COLUMN_NAME_PRICE = "price"
        const val COLUMN_NAME_DESCRIPTION = "description"
        const val COLUMN_NAME_QUANTITY = "quantity"

    }

    companion object TokenEntry : BaseColumns {
        const val TABLE_NAME = "token"
        const val COLUMN_NAME_REQUEST_DATE = "requestDate"
        const val COLUMN_NAME_TOKEN = "token_value"
    }


    private val SQL_CREATE_PRODUCT_ENTRIES =
        "CREATE TABLE ${ProductEntry.TABLE_NAME} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                "${ProductEntry.COLUMN_NAME_BARCODE} TEXT," +
                "${ProductEntry.COLUMN_NAME_MODEL} TEXT," +
                "${ProductEntry.COLUMN_NAME_MANUFACTURER} TEXT," +
                "${ProductEntry.COLUMN_NAME_PRICE} REAL," +
                "${ProductEntry.COLUMN_NAME_DESCRIPTION} TEXT," +
                "${ProductEntry.COLUMN_NAME_QUANTITY} INTEGER)"


    private val SQL_CREATE_TOKEN_ENTRIES = "CREATE TABLE $TABLE_NAME (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY, " +
            "$COLUMN_NAME_REQUEST_DATE TEXT NOT NULL, " +
            "$COLUMN_NAME_TOKEN TEXT NOT NULL)"

    private val SQL_DROP_TABLE_PRODUCT = "DROP TABLE IF EXISTS ${ProductEntry.TABLE_NAME}"


    private val SQL_SELECT_ALL =
        "SELECT ${BaseColumns._ID}," +
                " ${ProductEntry.COLUMN_NAME_BARCODE}," +
                " ${ProductEntry.COLUMN_NAME_MODEL}," +
                " ${ProductEntry.COLUMN_NAME_MANUFACTURER}," +
                " ${ProductEntry.COLUMN_NAME_PRICE}," +
                " ${ProductEntry.COLUMN_NAME_DESCRIPTION}," +
                " ${ProductEntry.COLUMN_NAME_QUANTITY} " +
                "FROM ${ProductEntry.TABLE_NAME} ORDER BY ${ProductEntry.COLUMN_NAME_MODEL}"

    private val SQL_SELECT_ID = "SELECT ${BaseColumns._ID}," +
            " ${ProductEntry.COLUMN_NAME_BARCODE}," +
            " ${ProductEntry.COLUMN_NAME_MODEL}," +
            " ${ProductEntry.COLUMN_NAME_MANUFACTURER}," +
            " ${ProductEntry.COLUMN_NAME_PRICE}," +
            " ${ProductEntry.COLUMN_NAME_DESCRIPTION}," +
            " ${ProductEntry.COLUMN_NAME_QUANTITY}" +
            " FROM ${ProductEntry.TABLE_NAME}" +
            " WHERE ${ProductEntry.COLUMN_NAME_BARCODE}=?"

    private val SQL_SELECT_TOKEN = "SELECT $COLUMN_NAME_REQUEST_DATE,$COLUMN_NAME_TOKEN " +
            "FROM $TABLE_NAME " +
            "WHERE $COLUMN_NAME_REQUEST_DATE=?"


    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_PRODUCT_ENTRIES)
        db.execSQL(SQL_CREATE_TOKEN_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DROP_TABLE_PRODUCT)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    fun getAll(): List<Product> {
        val cursor = readableDatabase.rawQuery(SQL_SELECT_ALL, null)
        val list = generateSequence { if (cursor.moveToNext()) cursor else null }
            .mapNotNull {
                cursorToProduct(it)
            }
            .toList()
        cursor.close()
        return list
    }

    fun clearProducts() {
        writableDatabase.delete(ProductEntry.TABLE_NAME, null, null)
    }

    fun clearTokens() {
        writableDatabase.delete(TABLE_NAME, null, null)
    }

    private fun cursorToProduct(cursor: Cursor): Product? {
        return try {
            Product(
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4)
                    .toFloat(),
                cursor.getString(5),
                cursor.getString(6)
                    .toInt(),
            )
        } catch (e: Exception) {
            null
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun cursorToToken(cursor: Cursor): Token? {
        val encrypted : String = cursor.getString(1) ?: ""
        val encoded = URLDecoder.decode(encrypted, "UTF-8")
        val token = decrypt(encoded)
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Token(
                    token,
                    cursor.getString(0)
                )
            } else {
                TODO("VERSION.SDK_INT < O")
            }
        } catch (e: Exception) {
            println(e)
            null
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getToken(date: String): Token? {
//        val cursor = readableDatabase.query(
//            TABLE_NAME,
//            arrayOf(COLUMN_NAME_TOKEN, COLUMN_NAME_REQUEST_DATE), "$COLUMN_NAME_REQUEST_DATE=?",
//            arrayOf(date), null, null, null
//        )
        val cursor = readableDatabase.rawQuery(SQL_SELECT_TOKEN, arrayOf(date))
        return if (cursor.moveToNext()) cursorToToken(cursor) else null
    }


    fun getProductById(barcode: String): Product? {
        val cursor = readableDatabase.rawQuery(SQL_SELECT_ID, arrayOf(barcode))
        if (!cursor.moveToNext()) return null
        return cursorToProduct(cursor)
    }

    fun insert(product: Product): Long {
        val oldProduct = product.id?.let { getProductById(it) }
        if (oldProduct != null) {
            oldProduct.id?.let { editQty(it, oldProduct.quantity + product.quantity) }
            return -1
        }
        val cv = ContentValues()
        cv.put(ProductEntry.COLUMN_NAME_MODEL, product.model)
        cv.put(ProductEntry.COLUMN_NAME_BARCODE, product.id)
        cv.put(ProductEntry.COLUMN_NAME_MANUFACTURER, product.manufacturer)
        cv.put(ProductEntry.COLUMN_NAME_PRICE, product.price)
        cv.put(ProductEntry.COLUMN_NAME_DESCRIPTION, product.description)
        cv.put(ProductEntry.COLUMN_NAME_QUANTITY, product.quantity)
        return writableDatabase.insert(ProductEntry.TABLE_NAME, null, cv)
    }

    fun insert(token: Token): Long {
        val cv = ContentValues()
        cv.put(COLUMN_NAME_TOKEN, URLEncoder.encode(token.token, "UTF-8"))
        cv.put(COLUMN_NAME_REQUEST_DATE, token.requestDate)
        return writableDatabase.insert(TABLE_NAME, null, cv)
    }


    fun editQty(barcode: String, newQty: Int) {
        val cv = ContentValues()
        val args = arrayOf(barcode.toString())
        cv.put(ProductEntry.COLUMN_NAME_QUANTITY, newQty)
        writableDatabase.update(
            ProductEntry.TABLE_NAME,
            cv,
            "${ProductEntry.COLUMN_NAME_BARCODE}=?",
            args
        )
    }

    fun delete(token: Token): Int {
        return writableDatabase.delete(
            TABLE_NAME,
            "$COLUMN_NAME_TOKEN=? AND $COLUMN_NAME_REQUEST_DATE=?",
            arrayOf(
                token.token,
                token.requestDate
            )
        )
    }

}




