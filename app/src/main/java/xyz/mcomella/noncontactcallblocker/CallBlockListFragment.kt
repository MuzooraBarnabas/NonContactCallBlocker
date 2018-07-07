package xyz.mcomella.noncontactcallblocker

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_call_block_list.view.*

class CallBlockListFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layout = inflater.inflate(R.layout.fragment_call_block_list, container, false)

        layout.callBlockList.apply {
            adapter = CallBlockListAdapter()
        }

        return layout
    }

    companion object {
        fun newInstance() = CallBlockListFragment()
    }
}

private class CallBlockListAdapter : RecyclerView.Adapter<CallBlockListViewHolder>() {

    override fun getItemCount() = 0

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): CallBlockListViewHolder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBindViewHolder(p0: CallBlockListViewHolder, p1: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

private class CallBlockListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
}