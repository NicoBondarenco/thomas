package com.thomas.storage

import com.thomas.storage.data.FileResource
import com.thomas.storage.data.FileResult

interface Storage {

    fun saveFile(resource: FileResource): FileResult

}
