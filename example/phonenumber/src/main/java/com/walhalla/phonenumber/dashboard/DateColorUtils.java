package com.walhalla.phonenumber.dashboard;

import com.walhalla.phonenumber.R;
import com.walhalla.ui.DLog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateColorUtils {
    public static int getColorForDate(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMdd");
        try {


            // Получаем числовое представление даты в формате "yyMMdd"
            int dateValue = Integer.parseInt(dateFormat.format(new Date()));
            int targetDateValue = Integer.parseInt(dateString);

            // Разница между текущей датой и целевой датой
            int difference = dateValue - targetDateValue;

            // Количество дней в неделе и месяце
            int daysInWeek = 7;
            int daysInMonth = 30;

            if (difference > daysInMonth) {
                DLog.d("красный");
                return R.color.versionRed;
            } else if (difference > daysInWeek) {
                DLog.d("желтый");
                return R.color.versionYellow;
            } else {
                DLog.d("зеленый");
                return R.color.versionGreen;
            }

        } catch (Exception e) {
            DLog.handleException(e);
            return -1;
        }
    }
}
