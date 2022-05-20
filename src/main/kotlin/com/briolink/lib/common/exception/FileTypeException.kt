package com.briolink.lib.common.exception

import com.briolink.lib.common.exception.base.BaseFileTypeException

class FileTypeException(override val message: String = "Invalid file type: allowed JPEG or PNG images") :
    BaseFileTypeException() {
    override val code: String = "exception.file.type.invalid"
}
