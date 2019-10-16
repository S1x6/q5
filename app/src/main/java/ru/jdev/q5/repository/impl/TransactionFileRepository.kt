package ru.jdev.q5.repository.impl

import android.util.Log
import ru.jdev.q5.Transaction
import ru.jdev.q5.repository.Repository
import java.io.BufferedWriter
import java.io.File
import java.io.FileInputStream
import java.io.FileWriter

class TransactionFileRepository(private val file: File) : Repository<Transaction> {

    private val parse: (IndexedValue<String>) -> Transaction = { line -> Transaction.parse(file.name, line) }
    private val serialize: (Transaction) -> String = { it.toCsvLine() }

    private val elements = if (file.exists()) {
        FileInputStream(file).bufferedReader().lineSequence()
                .withIndex()
                .map { parse(it) }
                .toCollection(ArrayList())

    } else {
        Log.d("TransactionFileRepository", "No data file found, create parent dirs at ${file.parentFile.absolutePath}")
        file.parentFile.mkdirs()
        ArrayList<Transaction>()
    }

    override fun list() = elements

    override fun with(item: Transaction) {
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