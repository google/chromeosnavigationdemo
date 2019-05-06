/*
 * Copyright 2019 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.emilieroberts.chromeosnavigationdemo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AdaptableDemoViewModel : ViewModel() {
    var inPhotoViewer: Boolean = false
    private val photoIdToShow: MutableLiveData<Int> = MutableLiveData()
    private val fileSourceSelected: MutableLiveData<FileSource> = MutableLiveData()

    fun setCurrentPhotoId(imageId: Int) {
        photoIdToShow.value = imageId
    }

    fun getCurrentPhotoId() = photoIdToShow

    fun setFileSource (fileSource: FileSource) {
        fileSourceSelected.value = fileSource
    }

    fun getFileSource(): MutableLiveData<FileSource> {
        if (fileSourceSelected.value == null) fileSourceSelected.value = FileSource.LOCAL

        return fileSourceSelected
    }

}