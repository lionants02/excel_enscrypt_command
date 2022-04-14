/*
 * MIT License
 *
 * Copyright (c) 2022 Max Thanachai Thongkum
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package me.maxza.lib

import org.apache.poi.poifs.crypt.Decryptor
import org.apache.poi.poifs.crypt.EncryptionInfo
import org.apache.poi.poifs.filesystem.POIFSFileSystem
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File
import java.io.InputStream
import java.util.*


internal class EncryptXlsxTest {
    private val src = File("src/test/resources/test.xlsx")
    private val testFile = File("src/test/resources/test2.xlsx")

    @BeforeEach
    fun setUp() {
        src.copyTo(testFile, true)
    }

    @AfterEach
    fun tearDown() {
        testFile.delete()
    }

    @Test
    fun encrypt() {
        val testObj = EncryptXlsx()
        val password = UUID.randomUUID().toString()
        println("Password test is $password")
        testObj.encrypt(testFile, password)
        readExcel(testFile, password)

    }

    private fun readExcel(excelFile: File, password: String) {
        val fileSystem = POIFSFileSystem(excelFile, true)
        val info = EncryptionInfo(fileSystem)
        val decrypt = Decryptor.getInstance(info)
        if (!decrypt.verifyPassword(password)) {
            throw RuntimeException("Unable to process: document is encrypted.")
        }
        val dataStream: InputStream = decrypt.getDataStream(fileSystem)
        val workbook: Workbook = XSSFWorkbook(dataStream)
        val sheet = workbook.getSheetAt(0)
        val rowIterator: Iterator<Row> = sheet.iterator()
        while (rowIterator.hasNext()) {
            val row: Row = rowIterator.next()
            val cellIterator: Iterator<Cell> = row.cellIterator()
            while (cellIterator.hasNext()) {
                val cell = cellIterator.next()
                print(cell.stringCellValue)
            }
            println("")
        }
        workbook.close()
        dataStream.close()
        fileSystem.close()
    }
}