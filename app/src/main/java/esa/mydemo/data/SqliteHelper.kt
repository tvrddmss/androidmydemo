package esa.mydemo.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.util.concurrent.atomic.AtomicInteger

class SqliteHelper private constructor(
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
        const val dbName = "data.db"

        private var instance: SqliteHelper? = null
        fun getInstance(context: Context): SqliteHelper {
            if (instance == null) {
                synchronized(SqliteHelper::class.java) {
                    if (instance == null) {
                        instance = SqliteHelper(context.applicationContext)
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
        sQLiteDatabase.execSQL("create table teacher(id  integer primary key autoincrement not null,date text,timestamp integer,name text, sex text)")
        sQLiteDatabase.execSQL("create table student(id  integer primary key autoincrement not null,date text,timestamp integer,name text)")
    }

    override fun onUpgrade(sQLiteDatabase: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}
}
