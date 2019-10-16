package ru.jdev.q5

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import ru.jdev.q5.repository.impl.CategorySqlRepository
import ru.jdev.q5.repository.impl.TransactionSqlRepository

@RunWith(AndroidJUnit4::class)
class SqlRepositoryTest {

    @Test
    fun testReadWriteCategoryRepository() {
        val context = InstrumentationRegistry.getTargetContext()
        context.deleteDatabase("q5DB")
        val repo = CategorySqlRepository(context)
        val item1 = Category(null, "First")
        val item2 = Category(null, "Second")
        repo.with(item1)
        repo.with(item2)
        val size = repo.list().size
        val newRepo = CategorySqlRepository(context)
        val readItem1 = newRepo.list()[size - 2]
        val readItem2 = newRepo.list()[size - 1] // смотрим последние записи, потому что в бд могли симпортироваться данные из файла
        Assert.assertEquals(item1.name, readItem1.name)
        Assert.assertEquals(item2.name, readItem2.name)
        context.deleteDatabase("q5DB")
    }

    @Test
    fun testInsertUpdateCategoryRepository() {
        val context = InstrumentationRegistry.getTargetContext()
        context.deleteDatabase("q5DB")
        val repo = CategorySqlRepository(context)
        val item1 = Category(null, "First")
        val item2 = Category(null, "Second")
        repo.with(item1)
        repo.with(item2)
        val size = repo.list().size
        val editedItem = Category(size, "Third")
        repo.with(editedItem)
        repo.delete(size - 1)
        val newRepo = CategorySqlRepository(context)
        val readList = newRepo.list()
        Assert.assertEquals(readList.size, 1)
        Assert.assertEquals(readList[size - 2].name, "Third") // смотрим последние записи, потому что в бд могли симпортироваться данные из файла
        context.deleteDatabase("q5DB")
    }

    @Test
    fun testReadWriteTransactionRepository() {
        val context = InstrumentationRegistry.getTargetContext()
        context.deleteDatabase("q5DB")
        val repo = TransactionSqlRepository(context)
        val item1 = Transaction(null, "50", "Продукты", "В тц", "source", logPart = null)
        val item2 = Transaction(null, "100", "Здоровье", "В аптеке", "sms", logPart = null)
        repo.with(item1)
        repo.with(item2)
        val size = repo.list().size
        val newRepo = TransactionSqlRepository(context)
        val readItem1 = newRepo.list()[size - 2]
        val readItem2 = newRepo.list()[size - 1] // смотрим последние записи, потому что в бд могли симпортироваться данные из файла
        Assert.assertEquals(item1.sum, readItem1.sum)
        Assert.assertEquals(item1.category, readItem1.category)
        Assert.assertEquals(item1.comment, readItem1.comment)
        Assert.assertEquals(item1.source, readItem1.source)
        Assert.assertEquals(item1.date.date(), readItem1.date.date())
        Assert.assertEquals(item2.sum, readItem2.sum)
        Assert.assertEquals(item2.category, readItem2.category)
        Assert.assertEquals(item2.comment, readItem2.comment)
        Assert.assertEquals(item2.source, readItem2.source)
        Assert.assertEquals(item2.date.date(), readItem2.date.date())
        context.deleteDatabase("q5DB")
    }

    @Test
    fun testInsertUpdateTransactionRepository() {
        val context = InstrumentationRegistry.getTargetContext()
        context.deleteDatabase("q5DB")
        val repo = TransactionSqlRepository(context)
        val item1 = Transaction(null, "50", "Продукты", "В тц", "source", logPart = null)
        val item2 = Transaction(null, "100", "Здоровье", "В аптеке", "sms", logPart = null)
        repo.with(item1)
        repo.with(item2)
        val size = repo.list().size
        val item12 = Transaction(size - 1, "1100", "Здоровье1", "В аптеке1", "sms1", item1.date, null)
        repo.delete(size)
        repo.with(item12)
        val newRepo = TransactionSqlRepository(context)
        val readItem1 = newRepo.list()[size - 2]
        Assert.assertEquals(newRepo.list().size, size - 1) // смотрим последние записи, потому что в бд могли симпортироваться данные из файла
        Assert.assertEquals(item12.sum, readItem1.sum)
        Assert.assertEquals(item12.category, readItem1.category)
        Assert.assertEquals(item12.comment, readItem1.comment)
        Assert.assertEquals(item12.source, readItem1.source)
        Assert.assertEquals(item12.date.date(), readItem1.date.date())
        context.deleteDatabase("q5DB")
    }
}