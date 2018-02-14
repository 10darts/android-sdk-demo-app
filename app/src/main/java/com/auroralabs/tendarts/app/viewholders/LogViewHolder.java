package com.auroralabs.tendarts.app.viewholders;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.auroralabs.tendarts.R;
import com.auroralabs.tendarts.domain.entities.LogEntity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by luismacb on 12/2/18.
 */

public class LogViewHolder extends RecyclerView.ViewHolder {

    public static final int LAYOUT_RESOURCE_ID = R.layout.view_holder_log;

    private LogEntity logEntity;

    @BindView(R.id.log_text)
    TextView logText;

    public LogViewHolder(View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);
    }

    public void bind(LogEntity logEntity) {

        this.logEntity = logEntity;

        StringBuilder message = new StringBuilder();
        message.append(logEntity.getLogDate());

        if (!TextUtils.isEmpty(logEntity.getCategory())) {
            message.append(", category: '");
            message.append(logEntity.getCategory());
            message.append("'");
        }

        if (!TextUtils.isEmpty(logEntity.getType())) {
            message.append(", type: '");
            message.append(logEntity.getType());
            message.append("'");
        }

        if (!TextUtils.isEmpty(logEntity.getMessage())) {
            message.append(", message: '");
            message.append(logEntity.getMessage());
            message.append("'");
        }

        logText.setText(message.toString());

    }

}
