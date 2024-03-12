package com.roydon.community.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.roydon.community.R;
import com.roydon.community.domain.entity.EpidemicTemperatureReport;
import com.roydon.community.listener.OnItemClickListener;
import com.roydon.community.utils.string.TimeUtils;

import java.util.List;

public class TemperatureReportRecordAdapter extends RecyclerView.Adapter<TemperatureReportRecordAdapter.TemperatureReportRecordHolder> {

    private final Context mContext;
    private List<EpidemicTemperatureReport> data;
    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setData(List<EpidemicTemperatureReport> data) {
        this.data = data;
    }

    public TemperatureReportRecordAdapter(Context context) {
        this.mContext = context;
    }

    public TemperatureReportRecordAdapter(Context context, List<EpidemicTemperatureReport> data) {
        this.mContext = context;
        this.data = data;
    }

    @NonNull
    @Override
    public TemperatureReportRecordAdapter.TemperatureReportRecordHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_temperature_report_record, parent, false);
        return new TemperatureReportRecordAdapter.TemperatureReportRecordHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull TemperatureReportRecordAdapter.TemperatureReportRecordHolder holder, int position) {
        EpidemicTemperatureReport report = data.get(position);
        holder.realName.setText(report.getRealName());
        holder.telephone.setText(report.getTelephone());
        holder.temperature.setText(report.getTemperature() + " ℃");
        holder.createTime.setText(TimeUtils.getSmartDate(report.getCreateTime().getTime()));
    }

    @Override
    public int getItemCount() {
        return (data == null) ? 0 : data.size();
    }

    public class TemperatureReportRecordHolder extends RecyclerView.ViewHolder {
        private final TextView realName, telephone, temperature,createTime;

        public TemperatureReportRecordHolder(@NonNull View view) {
            super(view);
            realName = view.findViewById(R.id.tv_real_name);
            telephone = view.findViewById(R.id.tv_telephone);
            temperature = view.findViewById(R.id.tv_temperature);
            createTime = view.findViewById(R.id.tv_create_time);
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

