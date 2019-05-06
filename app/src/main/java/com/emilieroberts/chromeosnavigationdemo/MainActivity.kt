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
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(),
    FragmentManager.OnBackStackChangedListener,
    FileBrowserFragment.FileActionsListener {

    private lateinit var fileBehaviorViewModel: AdaptableDemoViewModel

    private val onNavigationItemSelection: (MenuItem) -> Boolean = { item ->
        //If we're in the image viewer, pop back out to the file browser
        supportFragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)

        fileBehaviorViewModel.inPhotoViewer = false

        changeFileBrowserSource(item.itemId)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fileBehaviorViewModel = ViewModelProviders.of(this).get(AdaptableDemoViewModel::class.java)

        val bottomNavigationView: BottomNavigationView? = findViewById(R.id.bottomnav_main)
        bottomNavigationView?.setOnNavigationItemSelectedListener(onNavigationItemSelection)

        val navigationView: NavigationView? = findViewById(R.id.nav_view)
        navigationView?.setNavigationItemSelectedListener(onNavigationItemSelection)

        fileBehaviorViewModel.getFileSource().observe(this, Observer { updateTitle(it) })


        supportFragmentManager.addOnBackStackChangedListener(this)

        val fileBrowserFragment = FileBrowserFragment()
        fileBrowserFragment.fileActionsListener = this
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.file_listing_fragment, fileBrowserFragment)
            commit()
        }

        shouldDisplayHomeUp()
    }

    override fun onResume() {
        restoreState()
        super.onResume()
    }

    private fun restoreState() {
        if (fileBehaviorViewModel.inPhotoViewer) {
            displayImage()
        }
    }

    private fun changeFileBrowserSource(itemId: Int): Boolean {
        return when (itemId) {
            R.id.navigation_local -> {
                fileBehaviorViewModel.setFileSource(FileStore.intToPhotoSource(R.id.navigation_local))
                true
            }
            R.id.navigation_cloud -> {
                title = getString(R.string.label_cloud)
                fileBehaviorViewModel.setFileSource(FileStore.intToPhotoSource(R.id.navigation_cloud))
                true
            }
            R.id.navigation_shared -> {
                title = getString(R.string.label_shared)
                fileBehaviorViewModel.setFileSource(FileStore.intToPhotoSource(R.id.navigation_shared))
                true
            }
            else -> false
        }
    }

    override fun onFileClick(filePosition: Int, fileId: Int) {
        fileBehaviorViewModel.setCurrentPhotoId(fileId)
        fileBehaviorViewModel.inPhotoViewer = true
        displayImage()
    }

    private fun displayImage() {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.file_listing_fragment, ImageViewerFragment())
            addToBackStack(null)
            commit()
        }
    }

    private fun updateTitle(fileSource: FileSource) {
        title = when (fileSource) {
            FileSource.LOCAL -> getString(R.string.label_local)
            FileSource.CLOUD -> getString(R.string.label_cloud)
            FileSource.SHARED -> getString(R.string.label_shared)
        }
    }


    override fun onBackPressed() {
        //If we press back, we will never be in the photo viewer
        fileBehaviorViewModel.inPhotoViewer = false
        super.onBackPressed()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.options_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_item_about -> {
                showAboutDialog(this)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackStackChanged() {
        shouldDisplayHomeUp()
    }

    private fun shouldDisplayHomeUp() {
        //Enable Up if there are entries in the back stack
        supportActionBar?.setDisplayHomeAsUpEnabled(supportFragmentManager.backStackEntryCount > 0)
    }

    override fun onSupportNavigateUp(): Boolean {
        supportFragmentManager.popBackStack()
        fileBehaviorViewModel.inPhotoViewer = false
        return true
    }
}
