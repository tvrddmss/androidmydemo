package esa.mydemo.data

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase

class StudentDao private constructor(context: Context) {
    //    private val dateFormat = SimpleDateFormat("yyyy-MM-dd  HH:mm:ss", Locale.getDefault())
    private val sqliteHelper: SqliteHelper = SqliteHelper.getInstance(context)
    private val tblName = "student"

    companion object {
        private lateinit var studentDao: StudentDao

        @Synchronized
        fun getInstance(context: Context): StudentDao {
            try {
                if (studentDao == null) {
//                synchronized(StudentDao::class.java) {
//                    if (studentDao == null) {
                    studentDao = StudentDao(context.applicationContext)
//                    }
//                }
                }
            } catch (ex: Exception) {
                studentDao = StudentDao(context.applicationContext)
            }

            return studentDao
        }
    }

    @Synchronized
    fun insert(studentBean: StudentBean) {
//        val timeStamp: Long = studentBean.timestamp
//        val date = dateFormat.format(Date(timeStamp))

        val values = ContentValues()
        values.put("date", studentBean.date)
        values.put("timestamp", studentBean.timestamp)
        values.put("name", studentBean.name)

        sqliteHelper.getDB().insert(tblName, null, values)
        sqliteHelper.closeDB()
    }

    val count: Int
        get() {
            var count = 0
            var cursor: Cursor? = null

            try {
                cursor = sqliteHelper.getDB()
                    .rawQuery("select count(*) from $tblName", null)
                if (cursor.moveToFirst()) {
                    count = cursor.getInt(0)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                cursor?.close()
                sqliteHelper.closeDB()
            }
            return count
        }

    @SuppressLint("Range")
    fun select(offset: Int, count: Int): List<StudentBean> {
        val results: ArrayList<StudentBean> = ArrayList()
        var cursor: Cursor? = null

        try {
            val database: SQLiteDatabase = sqliteHelper.getDB()
            val params = arrayOfNulls<String>(2)
            params[0] = count.toString()
            params[1] = offset.toString()
            cursor =
                database.rawQuery(
                    "select * from $tblName order by id asc limit  ?  offset ?",
                    params
                )

            while (cursor.moveToNext()) {
                val bean = StudentBean.Builder()
                    .setId(cursor.getInt(cursor.getColumnIndex("id")))
                    .setName(cursor.getString(cursor.getColumnIndex("name")))
                    .setDate(cursor.getString(cursor.getColumnIndex("date")))
                    .setTimeStamp(cursor.getLong(cursor.getColumnIndex("timestamp")))
                    .build()

                results.add(bean)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
            sqliteHelper.closeDB()
        }

        return results
    }

    @SuppressLint("Range")
    fun selectAll(): List<StudentBean> {
        val results: ArrayList<StudentBean> = ArrayList()
        var cursor: Cursor? = null

        try {
            val database: SQLiteDatabase = sqliteHelper.getDB()
            var params = arrayOfNulls<String>(2)
            cursor =
                database.rawQuery(
                    "select * from $tblName order by id asc ",
                    params
                )

            while (cursor.moveToNext()) {
                val bean = StudentBean.Builder()
                    .setName(cursor.getString(cursor.getColumnIndex("name")))
                    .setDate(cursor.getString(cursor.getColumnIndex("date")))
                    .setTimeStamp(cursor.getLong(cursor.getColumnIndex("timestamp")))
                    .build()

                results.add(bean)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
            sqliteHelper.closeDB()
        }

        return results
    }

    @Synchronized
    fun delete(id: Int) {
        try {
            val database: SQLiteDatabase = sqliteHelper.getDB()
            val sql = StringBuilder()
                .append("delete from $tblName where id = $id ")
                .toString()
            database.execSQL(sql)
            sqliteHelper.closeDB()

            return
        } catch (e: Exception) {
            sqliteHelper.closeDB()
        }
    }

    @Synchronized
    fun clear() {
        try {
            val database: SQLiteDatabase = sqliteHelper.getDB()
            val sql = StringBuilder()
                .append("delete from $tblName  ")
                .toString()
            database.execSQL(sql)
            sqliteHelper.closeDB()

            return
        } catch (e: Exception) {
            sqliteHelper.closeDB()
        }
    }
}