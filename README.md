# excel_enscrypt_command

excel (xlsx) with password and encrypt data by AES256

Example windows

```shell
"H:\Program Files\jdk-11\bin\java.exe" -jar ./excel_enc.jar "excel_jar/test.xlsx" "test1234"
```

Example linux

```shell
/usr/lib/jvm/java-11-openjdk-amd64/bin/java -jar /var/lib/jenkins/workspace/xbc/excel_enc.jar '/var/lib/jenkins/workspace/xbc/worksheet.xls' 'passwordAbCCA'
```

Example python

```python
import os

excel_jar_command = '/usr/lib/jvm/java-11-openjdk-amd64/bin/java -jar /var/lib/jenkins/workspace/xbc/excel_enc.jar'
excel_file = '/var/lib/jenkins/workspace/xbc/worksheet.xls'
password = 'passwordAbCCA'

os.system(excel_jar_command + " " + excel_file + " " + password)

```

Encrypt by Agile
Encryption https://docs.microsoft.com/en-us/openspecs/office_file_formats/ms-offcrypto/87020a34-e73f-4139-99bc-bbdf6cf6fa55?redirectedfrom=MSDN

- CipherAlgorithm AES256
- hashAlgorithm sha512
- KeyBits 256 is default
- blockSize 16 is default
- CipherChaining ChainingModeCBC
