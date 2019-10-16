package ru.jdev.q5.repository.impl

import android.content.ContentValues
import android.content.Context
import ru.jdev.q5.Transaction
import ru.jdev.q5.TrxDate
import ru.jdev.q5.repository.DBHelper
import ru.jdev.q5.repository.Repository

class TransactionSqlRepository(context: Context): Repository<Transaction> {

    private val dbHelper = DBHelper(context)

    override fun list(): List<Transaction> {
        val c = dbHelper.readableDatabase.rawQuery("SELECT * FROM Transactions", null)
        val list = ArrayList<Transaction>()
        if (c.moveToFirst()) {
            val idColIndex = c.getColumnIndex("id")
            val categoryColIndex = c.getColumnIndex("category")
            val sourceColIndex = c.getColumnIndex("source")
            val dateColIndex = c.getColumnIndex("date")
            val timeColIndex = c.getColumnIndex("time")
            val sumColIndex = c.getColumnIndex("sum")
            val deviceColIndex = c.getColumnIndex("device")
            val commentColIndex = c.getColumnIndex("comment")
            do {
                list.add(Transaction(
                        c.getInt(idColIndex),
                        TrxDate(c.getString(dateColIndex), c.getString(timeColIndex)),
                        c.getString(sumColIndex),
                        c.getString(categoryColIndex),
                        c.getString(commentColIndex),
                        c.getString(deviceColIndex),
                        c.getString(sourceColIndex),
                        null))
            } while (c.moveToNext())
        }
        c.close()
        dbHelper.readableDatabase.close()
        return list
    }

    override fun with(item: Transaction) {
        if (item.id == null) {
            dbHelper.writableDatabase.insert("Transactions", null, ContentValues().apply {
                put("category", item.category)
                put("time", item.date.time())
                put("date", item.date.date())
                put("source", item.source)
                put("device", item.device)
                put("comment", item.comment)
                put("sum", item.sum)
            })
        } else {
            dbHelper.writableDatabase.update(
                    "Transactions",
                    ContentValues().apply {
                        put("category", item.category)
                        put("time", item.date.time())
                        put("date", item.date.date())
                        put("source", item.source)
                        put("device", item.device)
                        put("comment", item.comment)
                        put("sum", item.sum)
                    },
                    "id = ?",
                    arrayOf(item.id.toString())
            )
        }
        dbHelper.writableDatabase.close()
    }

    override fun delete(id: Int) {
        dbHelper.writableDatabase.delete("Transactions", "id = ?", arrayOf(id.toString()))
        dbHelper.writableDatabase.close()
    }

    override fun persist() {}

}