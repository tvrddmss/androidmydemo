package esa.mylibrary.code

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase

class CodeDao private constructor(context: Context) {
    //    private val dateFormat = SimpleDateFormat("yyyy-MM-dd  HH:mm:ss", Locale.getDefault())
    private val sqliteCodeHelper: SqliteCodeHelper = SqliteCodeHelper.getInstance(context)
    private val tblName = "code"

    companion object {
        private lateinit var codeDao: CodeDao

        @Synchronized
        fun getInstance(context: Context): CodeDao {
            try {
                if (codeDao == null) {
                    codeDao = CodeDao(context.applicationContext)
                }
            } catch (ex: Exception) {
                codeDao = CodeDao(context.applicationContext)
            }

            return codeDao
        }
    }

    @Synchronized
    fun insert(codeBean: CodeBean) {
        val values = ContentValues()
        values.put("groupId", codeBean.groupId)
        values.put("codeValue", codeBean.codeValue)
        values.put("codeText", codeBean.codeText)

        sqliteCodeHelper.getDB().insert(tblName, null, values)
        sqliteCodeHelper.closeDB()
    }

    val count: Int
        get() {
            var count = 0
            var cursor: Cursor? = null

            try {
                cursor = sqliteCodeHelper.getDB().rawQuery("select count(*) from $tblName", null)
                if (cursor.moveToFirst()) {
                    count = cursor.getInt(0)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                cursor?.close()
                sqliteCodeHelper.closeDB()
            }
            return count
        }

    @SuppressLint("Range")
    fun select(offset: Int, count: Int): List<CodeBean> {
        val results: ArrayList<CodeBean> = ArrayList()
        var cursor: Cursor? = null

        try {
            val database: SQLiteDatabase = sqliteCodeHelper.getDB()
            val params = arrayOfNulls<String>(2)
            params[0] = count.toString()
            params[1] = offset.toString()
            cursor =
                database.rawQuery(
                    "select * from $tblName order by groupid asc limit  ?  offset ?",
                    params
                )

            while (cursor.moveToNext()) {
                val bean = CodeBean.Builder()
                    .setId(cursor.getInt(cursor.getColumnIndex("id")))
                    .setGroupId(cursor.getString(cursor.getColumnIndex("groupid")))
                    .setCodeValue(cursor.getString(cursor.getColumnIndex("codevalue")))
                    .setCodeText(cursor.getString(cursor.getColumnIndex("codetext")))
                    .build()

                results.add(bean)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
            sqliteCodeHelper.closeDB()
        }

        return results
    }

    @SuppressLint("Range")
    fun selectAll(): List<CodeBean> {
        val results: ArrayList<CodeBean> = ArrayList()
        var cursor: Cursor? = null

        try {
            val database: SQLiteDatabase = sqliteCodeHelper.getDB()
            val params = arrayOfNulls<String>(0)
            cursor =
                database.rawQuery(
                    "select * from $tblName order by groupId asc",
                    params
                )

            while (cursor.moveToNext()) {
                val bean = CodeBean.Builder()
                    .setId(cursor.getInt(cursor.getColumnIndex("id")))
                    .setGroupId(cursor.getString(cursor.getColumnIndex("groupid")))
                    .setCodeValue(cursor.getString(cursor.getColumnIndex("codevalue")))
                    .setCodeText(cursor.getString(cursor.getColumnIndex("codetext")))
                    .build()

                results.add(bean)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
            sqliteCodeHelper.closeDB()
        }

        return results
    }

    @Synchronized
    fun delete(id: Int) {
        try {
            val database: SQLiteDatabase = sqliteCodeHelper.getDB()
            val sql = StringBuilder()
                .append("delete from $tblName where id = $id ")
                .toString()
            database.execSQL(sql)
            sqliteCodeHelper.closeDB()

            return
        } catch (e: Exception) {
            sqliteCodeHelper.closeDB()
        }
    }

    @Synchronized
    fun clear() {
        try {
            val database: SQLiteDatabase = sqliteCodeHelper.getDB()
            val sql = StringBuilder()
                .append("delete from $tblName  ")
                .toString()
            database.execSQL(sql)
            sqliteCodeHelper.closeDB()

            return
        } catch (e: Exception) {
            sqliteCodeHelper.closeDB()
        }
    }
}