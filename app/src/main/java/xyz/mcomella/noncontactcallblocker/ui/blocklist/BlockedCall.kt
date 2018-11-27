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

import androidx.recyclerview.widget.DiffUtil

/**
 * A blocked call's user-facing appearance, already formatted to the current locale and time zone.
 *
 * @param number formatted number for the blocked call or null if the number is unknown.
 */
data class BlockedCall(
        // A sealed class would be better but it's not supported by data classes
        val number: String?,
        val date: String
)

class BlockedCallDiffCallback : DiffUtil.ItemCallback<BlockedCall>() {
    override fun areItemsTheSame(oldItem: BlockedCall, newItem: BlockedCall): Boolean {
        // The DB can return different references to the same item: this method is used as a shallow
        // comparison by the list before calling areContentsTheSame. Since we have no shallow
        // comparison (e.g. id), we just compare the items.
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: BlockedCall, newItem: BlockedCall): Boolean {
        return oldItem == newItem
    }
}
