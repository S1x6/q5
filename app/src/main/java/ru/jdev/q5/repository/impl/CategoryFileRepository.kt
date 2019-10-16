package ru.jdev.q5.repository.impl

import android.util.Log
import ru.jdev.q5.Category
import ru.jdev.q5.repository.Repository
import java.io.BufferedWriter
import java.io.File
import java.io.FileInputStream
import java.io.FileWriter

class CategoryFileRepository(private val file: File) : Repository<Category> {

    private val parse: (IndexedValue<String>) -> Category = { c -> Category(c.index, c.value) }
    private val serialize = Category::name

    private val elements = if (file.exists()) {
        FileInputStream(file).bufferedReader().lineSequence()
                .withIndex()
                .map { parse(it) }
                .toCollection(ArrayList())
    } else {
        Log.d("CategoryFileRepository", "No data file found, create parent dirs at ${file.parentFile.absolutePath}")
        file.parentFile.mkdirs()
        ArrayList<Category>()
    }

    override fun list() = elements

    override fun with(item: Category) {
        val id = item.id
        if (id == null) {
            elements.add(item)
        } else {
            elements[id] = item
        }
    }

    override fun delete(id: Int) {
        elements.removeAt(id)
    }

    override fun persist() {
        val content = elements.joinToString("\n") { serialize(it) }
        BufferedWriter(FileWriter(file)).use {
            it.write(content)
        }
    }

}