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

import java.util.*

enum class FileSource {
    LOCAL, CLOUD, SHARED
}

class PhotoFile (var title: String = "",
                 var imageId: Int = 0,
                 var filesize: String = "0kb",
                 var date: Date = Date(System.currentTimeMillis()))


object FileStore {
    private val localFiles = listOf(
        PhotoFile(),
        PhotoFile("Blueberry soup", R.drawable.food_00, "42.5 kB"),
        PhotoFile("Asparagus", R.drawable.food_01, "51.2 kB"),
        PhotoFile("Tomato soup", R.drawable.food_02, "32.1 kB")
    )
    private val cloudFiles = listOf(
        PhotoFile(),
        PhotoFile("Breakfast spread", R.drawable.food_03, "48.1 kB"),
        PhotoFile("Delicious dessert", R.drawable.food_04, "403 kB"),
        PhotoFile("Pizza", R.drawable.food_05, "20.3 kB"),
        PhotoFile("Yummy Crêpe", R.drawable.food_11, "300.2 kB")
    )
    private val sharedFiles = listOf(
        PhotoFile(),
        PhotoFile("Gelato al pistacchio", R.drawable.food_07, "25.1 kB"),
        PhotoFile("Basilico e limone", R.drawable.food_08, "25.1 kB"),
        PhotoFile("Spaghetti and basil", R.drawable.food_09, "25.1 kB"),
        PhotoFile("Wonderful penne", R.drawable.food_10, "25.1 kB"),
        PhotoFile("Fancy ice-cream treat", R.drawable.food_12, "25.1 kB")
    )

    fun getFilesForSource (source: FileSource) : List<PhotoFile> {
        return when (source) {
            FileSource.LOCAL -> localFiles

            FileSource.CLOUD -> cloudFiles

            FileSource.SHARED -> sharedFiles
        }
    }

    fun intToPhotoSource(index: Int) : FileSource {
        if (index == R.id.navigation_cloud)
            return FileSource.CLOUD
        if (index == R.id.navigation_shared)
            return FileSource.SHARED

        return FileSource.LOCAL
    }
}