package xyz.mcomella.noncontactcallblocker

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
import xyz.mcomella.noncontactcallblocker.db.AppDB
import xyz.mcomella.noncontactcallblocker.db.BlockedCallEntity
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
