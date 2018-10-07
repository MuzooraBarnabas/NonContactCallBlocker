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

package xyz.mcomella.noncontactcallblocker.ui.blocklist

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.res.Resources
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.telephony.PhoneNumberUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout.VERTICAL
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_call_block_list.*
import xyz.mcomella.noncontactcallblocker.R
import xyz.mcomella.noncontactcallblocker.db.BlockedCallEntity
import java.text.DateFormat

/** The screen that lists blocked calls. */
class CallBlockListFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_call_block_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val context = context!! // Activity is created.

        val blockListAdapter = CallBlockListAdapter(context.resources)
        callBlockList.apply {
            layoutManager = LinearLayoutManager(context, VERTICAL, false)
            adapter = blockListAdapter
        }

        val viewModelFactory = CallBlockListViewModel.Factory()
        val callBlockListViewModel =
                ViewModelProviders.of(this, viewModelFactory)[CallBlockListViewModel::class.java]
        callBlockListViewModel.blockedCalls.observe(this, Observer { blockedCalls ->
            blockListAdapter.submitList(blockedCalls)
        })
    }

    companion object {
        fun newInstance() = CallBlockListFragment()
    }
}

private class CallBlockListAdapter(
        res: Resources
) : ListAdapter<BlockedCallEntity, CallBlockListViewHolder>(DiffCallback()) {

    private val unknownNumberString = res.getString(R.string.block_list_unknown_number)
    private val dateFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CallBlockListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return CallBlockListViewHolder(inflater.inflate(R.layout.block_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: CallBlockListViewHolder, position: Int) = with (holder) {
        val blockedNumberEntity = getItem(position)
        numberView.text = if (blockedNumberEntity.number != null) {
//            PhoneNumberUtils.formatNumber(blockedNumberEntity.number, "US") // TODO: Country okay?
            PhoneNumberUtils.formatNumber(blockedNumberEntity.number) // TODO: can't get to work.
        } else {
            unknownNumberString
        }

        dateView.text = dateFormat.format(blockedNumberEntity.date)
    }

    private class DiffCallback : DiffUtil.ItemCallback<BlockedCallEntity>() {
        override fun areItemsTheSame(oldItem: BlockedCallEntity, newItem: BlockedCallEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: BlockedCallEntity, newItem: BlockedCallEntity): Boolean {
            return oldItem == newItem
        }
    }
}

private class CallBlockListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val numberView: TextView = itemView.findViewById(R.id.number)
    val dateView: TextView = itemView.findViewById(R.id.date)
}
