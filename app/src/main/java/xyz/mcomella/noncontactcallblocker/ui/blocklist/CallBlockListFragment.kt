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

import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import kotlinx.android.synthetic.main.fragment_call_block_list.view.*
import xyz.mcomella.noncontactcallblocker.R
import xyz.mcomella.noncontactcallblocker.ext.viewModelFactory

/** The screen that lists blocked calls. */
class CallBlockListFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layout = inflater.inflate(R.layout.fragment_call_block_list, container, false)

        val context = inflater.context
        val blockListAdapter = CallBlockListAdapter(context.resources)
        layout.callBlockList.apply {
            layoutManager = LinearLayoutManager(context, VERTICAL, false)
            adapter = blockListAdapter
        }

        val callBlockListViewModel = viewModelFactory[CallBlockListViewModel::class.java]
        callBlockListViewModel.blockedCalls.observe(viewLifecycleOwner, Observer { blockedCalls ->
            blockListAdapter.submitList(blockedCalls)
        })

        return layout
    }

    companion object {
        fun newInstance() = CallBlockListFragment()
    }
}

private class CallBlockListAdapter(
    res: Resources
) : ListAdapter<BlockedCall, CallBlockListViewHolder>(BlockedCallDiffCallback()) {

    private val unknownNumberString = res.getString(R.string.block_list_unknown_number)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CallBlockListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return CallBlockListViewHolder(inflater.inflate(R.layout.block_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: CallBlockListViewHolder, position: Int) = with(holder) {
        val blockedCall = getItem(position)
        numberView.text = blockedCall.number ?: unknownNumberString
        dateView.text = blockedCall.date
    }
}

private class CallBlockListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val numberView: TextView = itemView.findViewById(R.id.number)
    val dateView: TextView = itemView.findViewById(R.id.date)
}
