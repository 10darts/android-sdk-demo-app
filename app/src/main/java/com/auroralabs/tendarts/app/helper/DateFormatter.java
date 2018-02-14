package com.auroralabs.tendarts.app.helper;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by luisma on 13/2/18.
 */

public class DateFormatter {

    private SimpleDateFormat logDateFormat = new SimpleDateFormat("dd-MM HH:mm:ss", Locale.getDefault());

    private static final ThreadLocal<DateFormatter> formatter = new ThreadLocal<DateFormatter>()
    {
        @Override
        protected DateFormatter initialValue() {
            return new DateFormatter();
        }
    };

    public static DateFormatter getThreadInstance()
    {
        return formatter.get();
    }

    public SimpleDateFormat getLogDateFormat() {
        return logDateFormat;
    }

}
