package ru.jdev.q5.repository.impl

import android.content.ContentValues
import android.content.Context
import ru.jdev.q5.Category
import ru.jdev.q5.repository.DBHelper
import ru.jdev.q5.repository.Repository

class CategorySqlRepository(context: Context) : Repository<Category> {

    private val dbHelper = DBHelper(context)

    override fun list(): List<Category> {
        val c = dbHelper.readableDatabase.rawQuery("SELECT * FROM Category", null)
        val list = ArrayList<Category>()
        if (c.moveToFirst()) {
            val idColIndex = c.getColumnIndex("id")
            val nameColIndex = c.getColumnIndex("name")
            do {
                list.add(Category(c.getInt(idColIndex), c.getString(nameColIndex)))
            } while (c.moveToNext())
        }
        c.close()
        dbHelper.readableDatabase.close()
        return list
    }

    override fun with(item: Category) {
        if (item.id == null) {
            dbHelper.writableDatabase.insert("Category", null, ContentValues().apply { put("name", item.name) })
        } else {
            dbHelper.writableDatabase.update(
                    "Category",
                    ContentValues().apply { put("name", item.name) },
                    "id = ?",
                    arrayOf(item.id.toString())
            )
        }
        dbHelper.writableDatabase.close()
    }

    override fun delete(id: Int) {
        dbHelper.writableDatabase.delete("Category", "id = ?", arrayOf(id.toString()))
        dbHelper.writableDatabase.close()
    }

    override fun persist() {}
}