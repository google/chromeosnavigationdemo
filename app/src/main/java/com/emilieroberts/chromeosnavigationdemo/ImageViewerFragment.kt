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

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager

class ImageViewerFragment : Fragment() {

    lateinit var viewModel : AdaptableDemoViewModel

    //Remember our header is in the array list so we need to skip the first element
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        val mainView: View = inflater.inflate(R.layout.image_viewer, container, false)

        viewModel = ViewModelProviders.of(requireActivity()).get(AdaptableDemoViewModel::class.java)

        val fileSource = viewModel.getFileSource().value
        val listOfFiles = FileStore.getFilesForSource(fileSource!!)

        val selectedFileIndex = listOfFiles.indexOfFirst { file ->
            file.imageId == viewModel.getCurrentPhotoId().value
        }

        val pager = mainView.findViewById<ViewPager>(R.id.pager_imageviewer)
        val pagerAdapter = ImagePagerAdapter(requireFragmentManager(), listOfFiles)

        pager.adapter = pagerAdapter
        pager.setCurrentItem(selectedFileIndex - 1, true)

        return mainView
    }

    private inner class ImagePagerAdapter(fragmentManager: FragmentManager, val listOfFiles: List<PhotoFile>) : FragmentStatePagerAdapter(fragmentManager) {
        override fun getCount(): Int {
            //Note: we ignore the first element (header) so real size is size - 1
            return listOfFiles.size - 1
        }

        override fun getItem(position: Int): Fragment {
            return ImageFragment.newInstance(listOfFiles[position + 1].imageId)
        }
    }
}