package com.example.breaktimeawareness.breakOverview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.breaktimeawareness.R
import com.example.breaktimeawareness.database.BreakTime
import com.example.breaktimeawareness.databinding.ListItemBreakTimeBinding

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val ITEM_VIEW_TYPE_HEADER = 0
private const val ITEM_VIEW_TYPE_ITEM = 1

class BreakTimeAdapter(val clickListener: BreakTimeListener) : ListAdapter<DataItem,
        RecyclerView.ViewHolder>(BreakTimeDiffCallback()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder)
        {
            is ViewHolder -> {
                val breakTimeItem = getItem(position) as DataItem.BreakTimeItem
                holder.bind(clickListener ,breakTimeItem.breakTime)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_HEADER -> TextViewHolder.from(parent)
            ITEM_VIEW_TYPE_ITEM -> ViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType ${viewType}")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(getItem(position)){
            is DataItem.Header -> ITEM_VIEW_TYPE_HEADER
            is DataItem.BreakTimeItem -> ITEM_VIEW_TYPE_ITEM
        }
    }
    fun addHeaderAndSubmitList(list : List<BreakTime>?){
        adapterScope.launch {
            val items = when(list){
                null -> listOf(DataItem.Header)
                else -> listOf(DataItem.Header) + list.map { DataItem.BreakTimeItem(it) }
            }
            withContext(Dispatchers.Main){
                submitList(items)
            }
        }
    }
    class ViewHolder private constructor(val binding: ListItemBreakTimeBinding)
        : RecyclerView.ViewHolder(binding.root)     {

        fun bind(clickListener: BreakTimeListener, item: BreakTime) {
            binding.breakTime = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemBreakTimeBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }
    class TextViewHolder(view: View): RecyclerView.ViewHolder(view) {
        companion object {
            fun from(parent: ViewGroup): TextViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.header, parent, false)
                return TextViewHolder(view)
            }
        }
    }
}

/**
 * Callback for calculating the diff between two non-null items in a list.
 *
 * Used by ListAdapter to calculate the minumum number of changes between and old list and a new
 * list that's been passed to `submitList`.
 */
class BreakTimeDiffCallback : DiffUtil.ItemCallback<DataItem>() {
    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem == newItem
    }
}

class BreakTimeListener(val clickListener: (breakTImeId: Long) -> Unit) {
    fun onClick(breakTime: BreakTime) = clickListener(breakTime.breakTimeId)
}

sealed class DataItem {
    data class BreakTimeItem(val breakTime: BreakTime): DataItem() {
        override val id = breakTime.breakTimeId
    }

    object Header: DataItem() {
        override val id = Long.MIN_VALUE
    }

    abstract val id: Long
}