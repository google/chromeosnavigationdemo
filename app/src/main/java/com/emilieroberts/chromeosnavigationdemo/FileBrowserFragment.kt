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

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import java.lang.IllegalArgumentException

class FileBrowserFragment : Fragment(), FilesAdapter.RecyclerViewFileActionsListener {

    private lateinit var fileBehaviorViewModel: AdaptableDemoViewModel
    lateinit var fileActionsListener: FileActionsListener
    private var imagePreview: ImageView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val mainView: View = inflater.inflate(R.layout.file_browser, container, false)
        imagePreview = mainView.findViewById(R.id.image_preview)
        val recycler: RecyclerView = mainView.findViewById(R.id.recyclerview_main) as RecyclerView
        val fileListAdapter = FilesAdapter(this)
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = fileListAdapter
        fileBehaviorViewModel = ViewModelProviders.of(requireActivity()).get(AdaptableDemoViewModel::class.java)
        fileBehaviorViewModel.getFileSource().observe(this, Observer { fileSource ->
            fileListAdapter.submitList(FileStore.getFilesForSource(fileSource))
        })
        fileBehaviorViewModel.getCurrentPhotoId().observe(this, Observer { fileId ->
            imagePreview?.setImageResource(fileId)
        })
        return mainView
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is FileActionsListener) {
            fileActionsListener = context
        } else {
            throw IllegalArgumentException("Activity needs to implement the FileActionsListener interface")
        }
    }

    override fun onListItemClick(filePosition: Int, fileId: Int) {
        fileActionsListener.onFileClick(filePosition, fileId)
    }

    override fun onListItemFocus(imageId: Int) {
        fileBehaviorViewModel.setCurrentPhotoId(imageId)
    }

    interface FileActionsListener {
        fun onFileClick (filePosition: Int, fileId: Int)
    }
}