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
import android.widget.ImageView
import androidx.fragment.app.Fragment

class ImageFragment : Fragment() {
    var image: ImageView? = null

    companion object {
        fun newInstance(imageid: Int) : ImageFragment {
            val fragment = ImageFragment()
            val args = Bundle()
            args.putInt("imageid", imageid)
            fragment.arguments = args

            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val mainView: View = inflater.inflate(R.layout.image, container, false)
        image = mainView.findViewById(R.id.image_viewer)
        val imageId = arguments?.getInt("imageid")

        image?.setImageResource(imageId!!)
        image?.requestFocus()

        return mainView
    }

    override fun onResume() {
        super.onResume()
        image?.requestFocus()
    }
}