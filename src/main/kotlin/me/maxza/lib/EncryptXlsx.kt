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

import org.apache.poi.openxml4j.opc.OPCPackage
import org.apache.poi.openxml4j.opc.PackageAccess
import org.apache.poi.poifs.crypt.*
import org.apache.poi.poifs.filesystem.POIFSFileSystem
import java.io.File
import java.io.FileOutputStream


class EncryptXlsx : EncryptExcel {
    override fun encrypt(file: File, password: String) {
        POIFSFileSystem().use { fs ->
            val aes256 = CipherAlgorithm.aes256
            val info = EncryptionInfo(
                EncryptionMode.agile,
                aes256,
                HashAlgorithm.sha256,
                aes256.defaultKeySize,
                aes256.blockSize,
                ChainingMode.cbc
            )
            val encryptor = info.encryptor
            encryptor.confirmPassword(password)
            OPCPackage.open(file, PackageAccess.READ_WRITE).use { opc ->
                encryptor.getDataStream(fs).use { os -> opc.save(os) }
            }
            FileOutputStream(file).use { fos -> fs.writeFilesystem(fos) }
        }
    }
}