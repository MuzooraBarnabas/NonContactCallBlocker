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

package xyz.mcomella.noncontactcallblocker.blocklist

import android.arch.lifecycle.Observer
import android.content.res.Resources
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.telephony.PhoneNumberUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout.VERTICAL
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_call_block_list.view.*
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.withContext
import xyz.mcomella.noncontactcallblocker.R
import xyz.mcomella.noncontactcallblocker.db.AppDB
import xyz.mcomella.noncontactcallblocker.db.dbDispatcher
import java.text.DateFormat
import kotlin.properties.Delegates

/** The screen that lists blocked calls. */
class CallBlockListFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layout = inflater.inflate(R.layout.fragment_call_block_list, container, false)

        layout.callBlockList.apply {
            layoutManager = LinearLayoutManager(context, VERTICAL, false)

            val blockListAdapter = CallBlockListAdapter(context.resources)
            adapter = blockListAdapter
            launch {
                val blockedCallLiveData = withContext(dbDispatcher) { AppDB.db.blockedCallDao().loadBlockedCalls() }
                blockedCallLiveData.observe(this@CallBlockListFragment, Observer<List<BlockedCallEntity>> { newList ->
                    blockListAdapter.blockList = newList!!
                })
            }
        }

        return layout
    }

    companion object {
        fun newInstance() = CallBlockListFragment()
    }
}

private class CallBlockListAdapter(res: Resources) : RecyclerView.Adapter<CallBlockListViewHolder>() {
    private val unknownNumberString = res.getString(R.string.block_list_unknown_number)
    private val dateFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT)

    var blockList by Delegates.observable(emptyList<BlockedCallEntity>()) { _, _, _ -> notifyDataSetChanged() }

    override fun getItemCount() = blockList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CallBlockListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return CallBlockListViewHolder(inflater.inflate(R.layout.block_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: CallBlockListViewHolder, position: Int) = with (holder) {
        val blockedNumberEntity = blockList[position]
        numberView.text = if (blockedNumberEntity.number != null) {
//            PhoneNumberUtils.formatNumber(blockedNumberEntity.number, "US") // TODO: Country okay?
            PhoneNumberUtils.formatNumber(blockedNumberEntity.number) // TODO: can't get to work.
        } else {
            unknownNumberString
        }

        dateView.text = dateFormat.format(blockedNumberEntity.date)
    }
}

private class CallBlockListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val numberView: TextView = itemView.findViewById(R.id.number)
    val dateView: TextView = itemView.findViewById(R.id.date)
}
