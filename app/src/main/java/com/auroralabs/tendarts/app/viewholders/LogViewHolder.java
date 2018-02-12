package com.auroralabs.tendarts.app.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.auroralabs.tendarts.R;
import com.auroralabs.tendarts.domain.entities.LogEntity;

import butterknife.BindView;

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
    }

    public void bind(LogEntity logEntity) {

        this.logEntity = logEntity;

        if (logEntity.getCategory() == null) {
            logEntity.setCategory("");
        }

        if (logEntity.getType() == null) {
            logEntity.setType("");
        }

        if (logEntity.getMessage() == null) {
            logEntity.setMessage("");
        }

        String message = String.format("category: '%s', type: '%s', message: '%s'",
                logEntity.getCategory(), logEntity.getType(), logEntity.getMessage());

        logText.setText(message);

    }

}
