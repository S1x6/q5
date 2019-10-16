package ru.jdev.q5.repository

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import ru.jdev.q5.repository.impl.CategoryFileRepository
import ru.jdev.q5.repository.impl.TransactionFileRepository
import java.io.File


internal class DBHelper(private val context: Context)
    : SQLiteOpenHelper(context, "q5DB", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        Log.d("DB", "onCreate database")

        db.execSQL("create table Category ("
                + "id integer primary key autoincrement,"
                + "name text);")
        db.execSQL("create table Transactions ("
                + "id integer primary key autoincrement,"
                + "category text,"
                + "date text,"
                + "time text,"
                + "sum text, "
                + "comment text,"
                + "device text,"
                + "source text);")

        importCategoryFromFile(db, context)
        importTransactionFromFile(db, context)
    }

    private fun importCategoryFromFile(db: SQLiteDatabase, context: Context) {
        val repo = CategoryFileRepository(File(context.getExternalFilesDir(null), "categories.txt"))
        repo.list().forEach {
            val values = ContentValues()
            values.put("name", it.name)
            db.insert("Category", null, values)
        }
    }

    private fun importTransactionFromFile(db: SQLiteDatabase, context: Context) {
        val files = if (context.getExternalFilesDir(null)?.exists() != true) {
            listOf()
        } else {
            context.getExternalFilesDir(null)
                    .listFiles { file -> file.name.endsWith(".csv") }
                    .map { it.name }
        }
        files.forEach {fileName ->
            val file = File(context.getExternalFilesDir(null), fileName)
            val trRepo = TransactionFileRepository(file)
            trRepo.list().forEach {
                val values = ContentValues()
                values.put("category", it.category)
                values.put("date", it.date.date())
                values.put("time", it.date.time())
                values.put("sum", it.sum)
                values.put("comment", it.comment)
                values.put("device", it.device)
                values.put("source", it.source)
                db.insert("Transactions", null, values)
            }
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

    }
}