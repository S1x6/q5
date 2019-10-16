package ru.jdev.q5.repository

import ru.jdev.q5.storage.Item

interface Repository<T> where T : Item {

    fun list(): List<T>

    fun with(item: T)

    fun delete(id: Int)

    fun persist()
}