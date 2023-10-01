package com.roydon.community.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.roydon.community.R;
import com.roydon.community.domain.entity.EpidemicNatOrder;
import com.roydon.community.enums.NatOrderStatusEnum;
import com.roydon.community.listener.OnItemClickListener;
import com.roydon.community.utils.string.IdCardNumUtil;
import com.roydon.community.utils.string.TelephoneUtils;
import com.roydon.community.utils.string.TimeUtils;
import com.roydon.community.widget.RoundImageView;

import java.util.List;
import java.util.Objects;

public class NatOrderRecordAdapter extends RecyclerView.Adapter<NatOrderRecordAdapter.NatOrderRecordHolder> {

    private final Context mContext;
    private List<EpidemicNatOrder> data;
    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setData(List<EpidemicNatOrder> data) {
        this.data = data;
    }

    public NatOrderRecordAdapter(Context context) {
        this.mContext = context;
    }

    public NatOrderRecordAdapter(Context context, List<EpidemicNatOrder> data) {
        this.mContext = context;
        this.data = data;
    }

    @NonNull
    @Override
    public NatOrderRecordAdapter.NatOrderRecordHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_nat_order_record, parent, false);
        return new NatOrderRecordAdapter.NatOrderRecordHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull NatOrderRecordAdapter.NatOrderRecordHolder holder, int position) {
        EpidemicNatOrder report = data.get(position);
        holder.realName.setText(report.getRealName());
        holder.telephone.setText(TelephoneUtils.replaceSomeCharByAsterisk(report.getTelephone()));
        holder.idCard.setText(IdCardNumUtil.replaceSomeCharByAsterisk(report.getIdCard()));
        holder.orderTime.setText(TimeUtils.getSmartDate(report.getOrderTime().getTime()));
        String orderStatus = report.getOrderStatus();
        String orderInfo = (Objects.requireNonNull(NatOrderStatusEnum.getByCode(orderStatus)).getInfo());
        holder.orderStatus.setText(orderInfo);
        switch (orderStatus) {
            case "0":
                // 已预约
                Glide.with(mContext).load(R.mipmap.icon_ordering).centerCrop().into(holder.ivOrderStatus);
                holder.orderStatus.setTextColor(Color.BLUE);
                break;
            case "1":
                // 已完成
                Glide.with(mContext).load(R.mipmap.icon_completed).centerCrop().into(holder.ivOrderStatus);
                holder.orderStatus.setTextColor(Color.GREEN);
                break;
            case "2":
                // 已取消
                Glide.with(mContext).load(R.mipmap.icon_canceled).centerCrop().into(holder.ivOrderStatus);
                holder.orderStatus.setTextColor(Color.GRAY);
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return (data == null) ? 0 : data.size();
    }

    public class NatOrderRecordHolder extends RecyclerView.ViewHolder {
        private final TextView realName, telephone, idCard, orderTime, orderStatus;
        private final RoundImageView ivOrderStatus;

        public NatOrderRecordHolder(@NonNull View view) {
            super(view);
            realName = view.findViewById(R.id.tv_real_name);
            telephone = view.findViewById(R.id.tv_telephone);
            idCard = view.findViewById(R.id.tv_id_card);
            orderTime = view.findViewById(R.id.tv_order_time);
            orderStatus = view.findViewById(R.id.tv_order_status);
            ivOrderStatus = view.findViewById(R.id.iv_order_status);
            view.setOnClickListener(v -> {
                mOnItemClickListener.onItemClick(v, getLayoutPosition());
            });
            view.setOnLongClickListener(v -> {
                mOnItemClickListener.onItemLongClick(v, getLayoutPosition());
                return true;
            });
        }
    }


}

