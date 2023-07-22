package esa.mylibrary.code

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.util.concurrent.atomic.AtomicInteger

class SqliteCodeHelper private constructor(
    context: Context,
    name: String,
    cursorFactory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context, name, cursorFactory, version) {

    private constructor (context: Context, name: String = dbName, version: Int = 1) : this(
        context,
        name,
        null,
        version
    )

    companion object {
        const val dbName = "code.db"

        private var instance: SqliteCodeHelper? = null
        fun getInstance(context: Context): SqliteCodeHelper {
            if (instance == null) {
                synchronized(SqliteCodeHelper::class.java) {
                    if (instance == null) {
                        instance = SqliteCodeHelper(context.applicationContext)
                    }
                }
            }

            return instance!!
        }
    }

    private val concurrentNumber: AtomicInteger = AtomicInteger()

    private var db: SQLiteDatabase? = null

    fun getDB(): SQLiteDatabase {
        if (concurrentNumber.incrementAndGet() == 1) {
            db = writableDatabase
        }
        return db!!
    }

    @Synchronized
    fun closeDB() {
        if (concurrentNumber.decrementAndGet() == 0)
            db?.close()
    }

    override fun onCreate(sQLiteDatabase: SQLiteDatabase) {
        sQLiteDatabase.execSQL(
            "create table code(" +
                    "id  integer primary key autoincrement not null," +
                    "groupid text," +
                    "codevalue text," +
                    "codetext text" +
                    ")"
        )

    }

    override fun onUpgrade(sQLiteDatabase: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}
}
