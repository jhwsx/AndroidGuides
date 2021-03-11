package com.example.wifi

import android.net.wifi.p2p.WifiP2pDevice
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.wifi.databinding.WifiP2pDeviceRecycleItemBinding

class RecyclerViewListAdapter(private val itemCallback: (WifiP2pDevice) -> Unit) :
        ListAdapter<WifiP2pDevice, RecyclerViewListAdapter.MyViewHolder>(diffCallback) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val binding = WifiP2pDeviceRecycleItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return MyViewHolder(itemCallback, binding)
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            holder.bindItem(currentList[position])
        }

        class MyViewHolder(
            private val itemCallback: (WifiP2pDevice) -> Unit,
            private val binding: WifiP2pDeviceRecycleItemBinding
        ) :
            RecyclerView.ViewHolder(binding.root) {
            fun bindItem(item: WifiP2pDevice) {
                binding.tvDeviceAddress.text = "设备地址：${item.deviceAddress}"
                binding.tvDeviceName.text = "设备名称：${item.deviceName}"
                binding.tvStatus.text = "状态：${item.status.getDeviceStringStatus()}"
                binding.pb.visibility =
                    if (item.status == WifiP2pDevice.INVITED) View.VISIBLE else View.GONE
                itemView.setOnClickListener {
                    itemCallback.invoke(item)
                }
            }
        }

        companion object {
            private val diffCallback: DiffUtil.ItemCallback<WifiP2pDevice> =
                object : DiffUtil.ItemCallback<WifiP2pDevice>() {
                    override fun areItemsTheSame(
                        oldItem: WifiP2pDevice,
                        newItem: WifiP2pDevice
                    ): Boolean {
                        return false
                    }

                    override fun areContentsTheSame(
                        oldItem: WifiP2pDevice,
                        newItem: WifiP2pDevice
                    ): Boolean {
                        return false
                    }
                }
        }
    }