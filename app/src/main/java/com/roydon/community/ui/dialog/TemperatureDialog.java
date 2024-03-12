package com.roydon.community.ui.dialog;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.roydon.community.R;
import com.roydon.community.adapter.MyAdapter;
import com.roydon.community.aop.SingleClick;
import com.roydon.community.widget.PickerLayoutManager;
import com.roydon.library.BaseDialog;

import java.util.ArrayList;

/**
 * desc: 温度选择弹窗
 */
public final class TemperatureDialog {

    public static final class Builder extends CommonDialog.Builder<Builder> implements Runnable {

        private final RecyclerView mIntegerView;
        private final RecyclerView mDecimalView;

        private final PickerLayoutManager mIntegerManager;
        private final PickerLayoutManager mDecimalManager;

        private final PickerAdapter mIntegerAdapter;
        private final PickerAdapter mDecimalAdapter;

        ArrayList<String> integerData;
        ArrayList<String> decimalData;

        private OnListener mListener;

        @SuppressWarnings("all")
        public Builder(Context context) {
            super(context);
            setCustomView(R.layout.dialog_temperature);
            setTitle(R.string.time_title);

            mIntegerView = findViewById(R.id.rv_temperature_integer);
            mDecimalView = findViewById(R.id.rv_temperature_decimal);

            mIntegerAdapter = new PickerAdapter(context);
            mDecimalAdapter = new PickerAdapter(context);

            // 生产整数
            integerData = new ArrayList<>(7);
            for (int i = 36; i <= 42; i++) {
//                integerData.add(i + " " + getString(R.string.common_point));
                integerData.add(i + "");
            }

            // 生产小数
            decimalData = new ArrayList<>(10);
            for (int i = 0; i <= 9; i++) {
                decimalData.add(i + "");
//                decimalData.add(i + " " + getString(R.string.common_c));
            }

            mIntegerAdapter.setData(integerData);
            mDecimalAdapter.setData(decimalData);

            mIntegerManager = new PickerLayoutManager.Builder(context).build();
            mDecimalManager = new PickerLayoutManager.Builder(context).build();

            mIntegerView.setLayoutManager(mIntegerManager);
            mDecimalView.setLayoutManager(mDecimalManager);

            mIntegerView.setAdapter(mIntegerAdapter);
            mDecimalView.setAdapter(mDecimalAdapter);

            postDelayed(this, 1000);
        }

        @Override
        public void run() {
//            if (mIntegerView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE && mDecimalView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE) {
//                Calendar calendar = Calendar.getInstance();
//                calendar.set(Calendar.HOUR_OF_DAY, mIntegerManager.getPickedPosition());
//                calendar.set(Calendar.MINUTE, mDecimalManager.getPickedPosition());
//                if (System.currentTimeMillis() - calendar.getTimeInMillis() < 3000) {
//                    calendar = Calendar.getInstance();
////                    setHour(calendar.get(Calendar.HOUR_OF_DAY));
////                    setMinute(calendar.get(Calendar.MINUTE));
//                    postDelayed(this, 1000);
//                }
//            }
        }

        public Builder setListener(OnListener listener) {
            mListener = listener;
            return this;
        }

        @SingleClick
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_ui_confirm:
                    autoDismiss();
                    if (mListener != null) {
                        mListener.onSelected(getDialog(), integerData.get(mIntegerManager.getPickedPosition()), decimalData.get(mDecimalManager.getPickedPosition()));
                    }
                    break;
                case R.id.tv_ui_cancel:
                    autoDismiss();
                    if (mListener != null) {
                        mListener.onCancel(getDialog());
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private static final class PickerAdapter extends MyAdapter<String> {

        private PickerAdapter(Context context) {
            super(context);
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder();
        }

        final class ViewHolder extends MyAdapter.ViewHolder {

            private final TextView mPickerView;

            ViewHolder() {
                super(R.layout.item_picker);
                mPickerView = (TextView) findViewById(R.id.tv_picker_name);
            }

            @Override
            public void onBindView(int position) {
                mPickerView.setText(getItem(position));
            }
        }
    }

    public interface OnListener {

        /**
         * 选择完时间后回调
         *
         * @param integer 整数部分
         * @param decimal 小数部分
         */
        void onSelected(BaseDialog dialog, String integer, String decimal);

        /**
         * 点击取消时回调
         */
        default void onCancel(BaseDialog dialog) {
        }
    }
}