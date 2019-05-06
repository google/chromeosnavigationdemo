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

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class FilesAdapter (private val actionsListener: RecyclerViewFileActionsListener) :
    ListAdapter<PhotoFile, FilesAdapter.PhotoFileViewHolder> (DIFF_CALLBACK)  {

    override fun getItemViewType(position: Int): Int {
        return if (0 == position) {
            LIST_HEADER
        } else {
            LIST_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoFileViewHolder {
        val layoutResource = when(viewType) {
            LIST_HEADER -> R.layout.recycler_item_list_header
            else -> R.layout.recycler_item_list
        }

        val view = LayoutInflater.from(parent.context).inflate(layoutResource, parent, false) as LinearLayout
        if (viewType == LIST_HEADER) view.setFocusable(false)
        return PhotoFileViewHolder(view, actionsListener)
    }

    override fun onBindViewHolder(holder: PhotoFileViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onViewDetachedFromWindow(holder: PhotoFileViewHolder) {
        if (holder.itemViewType != LIST_HEADER) holder.clearAllViews()
    }

    class PhotoFileViewHolder (private val view: View, private val actionsListener: RecyclerViewFileActionsListener) :
        RecyclerView.ViewHolder (view), View.OnClickListener, View.OnFocusChangeListener {

        private val fileTitle: TextView = view.findViewById(R.id.file_title)
        private val fileSize: TextView = view.findViewById(R.id.file_filesize)
        private val fileDate: TextView? = view.findViewById(R.id.file_date)

        lateinit var file: PhotoFile

        fun bind(fileToShow: PhotoFile) {
            view.onFocusChangeListener = this
            view.setOnClickListener(this)
            file = fileToShow
            if (file.imageId != 0) {
                fileTitle.text = file.title
                fileSize.text = file.filesize
                fileDate?.text = dateFormat(file.date)
            }
        }

        fun clearAllViews() {
            fileTitle.text = EMPTY_STRING
            fileDate?.text = EMPTY_STRING
            fileSize.text = EMPTY_STRING
        }

        override fun onClick(v: View?) {
            actionsListener.onListItemClick(this.adapterPosition, file.imageId)
        }

        override fun onFocusChange(v: View?, hasFocus: Boolean) {
            if (hasFocus) actionsListener.onListItemFocus(file.imageId)
        }

        private fun dateFormat(date: Date) : String {
            return DATE_FORMATTER.format(date)
        }
    }


    companion object {
        private const val LIST_HEADER = 0
        private const val LIST_ITEM = 1
        private const val EMPTY_STRING = ""
        private val DATE_FORMATTER = SimpleDateFormat("dd MMM YYYY")

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<PhotoFile> () {
            override fun areItemsTheSame(oldItem: PhotoFile, newItem: PhotoFile): Boolean {
                return oldItem.imageId == newItem.imageId
            }

            override fun areContentsTheSame(oldItem: PhotoFile, newItem: PhotoFile): Boolean {
                return false
            }


        }
    }

    interface RecyclerViewFileActionsListener {
        fun onListItemClick(position: Int, fileId: Int)
        fun onListItemFocus(imageId: Int)
    }
}
