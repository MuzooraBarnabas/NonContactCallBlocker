/* Copyright (C) 2018 Michael Comella
 *
 * This file is part of NonContactCallBlocker.
 *
 *  NonContactCallBlocker is free software: you can redistribute it
 *  and/or modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation, either version 3 of
 *  the License, or (at your option) any later version.
 *
 *  NonContactCallBlocker is distributed in the hope that it will be
 *  useful, but WITHOUT ANY WARRANTY; without even the implied
 *  warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *  See the GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with NonContactCallBlocker.  If not, see
 *  <https://www.gnu.org/licenses/>. */

package xyz.mcomella.noncontactcallblocker

import android.content.Intent
import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.experimental.CancellationException
import kotlinx.coroutines.experimental.Job
import xyz.mcomella.noncontactcallblocker.ui.blocklist.CallBlockListFragment
import xyz.mcomella.noncontactcallblocker.config.Config
import xyz.mcomella.noncontactcallblocker.config.ConfigurationFragment
import xyz.mcomella.noncontactcallblocker.ext.toApp
import xyz.mcomella.noncontactcallblocker.rights.SoftwareRightsActivity

const val LOGTAG = "NonContactCallBlocker" // max len 23.

class MainActivity : AppCompatActivity() {

    private val uiLifecycleCancelJob = Job()

    private var permissionsRequestContext: Permissions.PermissionsContext? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        initTabsView()
    }

    private fun initTabsView() {
        pagerContainer.adapter = MainPagerAdapter(supportFragmentManager)
        pagerContainer.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(pagerContainer))
    }

    override fun onStart() {
        super.onStart()
        maybeRequestPermissions()
    }

    override fun onDestroy() {
        super.onDestroy()
        uiLifecycleCancelJob.cancel(CancellationException("Activity lifecycle has ended"))
    }

    private fun maybeRequestPermissions() {
        permissionsRequestContext = Permissions.maybePromptForRequired(permissionsRequestContext,
                this, uiLifecycleCancelJob, ::onPermissionsGranted)
    }

    private fun onPermissionsGranted() {
        permissionsRequestContext = null

        val config = this.toApp().config
        if (!config.isInitialPermissionsRequestComplete) {
            config.isInitialPermissionsRequestComplete = true
            config.isBlockingEnabled = true // Will animate to visualize to users that blocking is now enabled.
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Permissions.onRequestPermissionsResult(permissionsRequestContext, requestCode, permissions,
                grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Permissions.onActivityResult(permissionsRequestContext, requestCode, resultCode)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_rights -> startActivity(Intent(this, SoftwareRightsActivity::class.java))

            else -> return super.onOptionsItemSelected(item)
        }

        return true
    }
}

private class MainPagerAdapter(fm: androidx.fragment.app.FragmentManager) : androidx.fragment.app.FragmentPagerAdapter(fm) {
    override fun getItem(position: Int) = when (position) {
        0 -> ConfigurationFragment.newInstance()
        1 -> CallBlockListFragment.newInstance()
        else -> throw IllegalArgumentException("Unexpected index $position")
    }

    override fun getCount() = 2
}
