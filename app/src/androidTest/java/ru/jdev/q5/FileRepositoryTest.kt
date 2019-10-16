package ru.jdev.q5

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import ru.jdev.q5.repository.impl.CategoryFileRepository
import ru.jdev.q5.repository.impl.TransactionFileRepository
import java.io.File

@RunWith(AndroidJUnit4::class)
class FileRepositoryTest {

    @Test
    fun testReadWriteCategoryRepository() {
        val context = InstrumentationRegistry.getTargetContext()
        val file = File(context.getExternalFilesDir(null), "test.txt")
        file.delete()
        val repo = CategoryFileRepository(file)
        val item1 = Category(null, "First")
        val item2 = Category(null, "Second")
        repo.with(item1)
        repo.with(item2)
        repo.persist()
        val newRepo = CategoryFileRepository(file)
        val readItem1 = newRepo.list()[0]
        val readItem2 = newRepo.list()[1]
        Assert.assertEquals(item1.name, readItem1.name)
        Assert.assertEquals(item2.name, readItem2.name)
        file.delete()
    }

    @Test
    fun testInsertUpdateCategoryRepository() {
        val context = InstrumentationRegistry.getTargetContext()
        val file = File(context.getExternalFilesDir(null), "test.txt")
        file.delete()
        val repo = CategoryFileRepository(file)
        val item1 = Category(null, "First")
        val item2 = Category(null, "Second")
        repo.with(item1)
        repo.with(item2)
        repo.persist()
        val editedItem = Category(0, "Third")
        repo.with(editedItem)
        repo.delete(1)
        repo.persist()
        val newRepo = CategoryFileRepository(file)
        val readList = newRepo.list()
        Assert.assertEquals(readList.size, 1)
        Assert.assertEquals(readList[0].name, "Third")
        file.delete()
    }

    @Test
    fun testReadWriteTransactionRepository() {
        val context = InstrumentationRegistry.getTargetContext()
        val file = File(context.getExternalFilesDir(null), "test.txt")
        file.delete()
        val repo = TransactionFileRepository(file)
        val item1 = Transaction(null, "50", "Продукты", "В тц", "source", logPart = null)
        val item2 = Transaction(null, "100", "Здоровье", "В аптеке", "sms", logPart = null)
        repo.with(item1)
        repo.with(item2)
        repo.persist()
        val newRepo = TransactionFileRepository(file)
        val readItem1 = newRepo.list()[0]
        val readItem2 = newRepo.list()[1]
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
        file.delete()
    }

    @Test
    fun testInsertUpdateTransactionRepository() {
        val context = InstrumentationRegistry.getTargetContext()
        val file = File(context.getExternalFilesDir(null), "test.txt")
        file.delete()
        val repo = TransactionFileRepository(file)
        val item1 = Transaction(null, "50", "Продукты", "В тц", "source", logPart = null)
        val item2 = Transaction(null, "100", "Здоровье", "В аптеке", "sms", logPart = null)
        repo.with(item1)
        repo.with(item2)
        repo.persist()
        val item12 = Transaction(0, "1100", "Здоровье1", "В аптеке1", "sms1", item1.date ,null)
        repo.delete(1)
        repo.with(item12)
        repo.persist()
        val newRepo = TransactionFileRepository(file)
        val readItem1 = newRepo.list()[0]
        Assert.assertEquals(newRepo.list().size, 1)
        Assert.assertEquals(item12.sum, readItem1.sum)
        Assert.assertEquals(item12.category, readItem1.category)
        Assert.assertEquals(item12.comment, readItem1.comment)
        Assert.assertEquals(item12.source, readItem1.source)
        Assert.assertEquals(item12.date.date(), readItem1.date.date())
        file.delete()
    }
}