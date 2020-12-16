package com.platform.insight.utils;

import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DateTimeUtil {
    public static final String DIVIDE_LINE = "-";
    public static final String DIVIDE_COLON = "-";
    public static final long SECOND = 1000L;
    public static final long MINUTE = 60000L;
    public static final long HOUR = 3600000L;
    public static final long DAY = 86400000L;
    public static final long WEEK = 604800000L;
    public static final String SHORTFORMAT = "yyyy-MM-dd";
    public static final String SHORTFORMATNOSPIT = "yyyyMMdd";
    public static final String LONGFORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String SUPERLONGFORMAT = "yyyy-MM-dd HH:mm:ss sss";
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public DateTimeUtil() {
    }

    public static String getTimestampString(Date date) {
        return sdf.format(date).toString();
    }

    public static Timestamp getTimestamp(Date date) {
        return Timestamp.valueOf(getTimestampString(date));
    }

    public static Timestamp formatTimestamp(String timestamp) {
        return StringUtils.isEmpty(timestamp) ? null : Timestamp.valueOf(timestamp);
    }

    public static String getDate(Date date, String format) {
        String result = null;

        try {
            SimpleDateFormat myFormat = new SimpleDateFormat(format);
            result = myFormat.format(date);
            return result;
        } catch (Exception var4) {
            return null;
        }
    }

    public static String getStyleDate(String style) {
        SimpleDateFormat lFormat = new SimpleDateFormat(style);
        String gRtnStr = lFormat.format(new Date());
        return gRtnStr;
    }

    public static Map<String, String> getMonthStartAndEnd(int year, int month, String... keys) {
        SimpleDateFormat d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Map<String, String> date = new HashMap();
        Calendar calendar = Calendar.getInstance();
        calendar.set(1, year);
        calendar.set(2, month - 1);
        calendar.set(5, 1);
        date.put(keys[0], d.format(calendar.getTime()));
        calendar.add(2, 1);
        calendar.add(5, -1);
        date.put(keys[1], d.format(calendar.getTime()));
        return date;
    }

    public static String getTimeString(String time) {
        String[] ti = time.split(":");
        if (ti[1].length() == 1) {
            time = ti[0] + "0" + ti[1];
        } else {
            time = ti[0] + ti[1];
        }

        return time;
    }






    public static int getDateInWeek(String strDate) {
        DateFormat df = DateFormat.getDateInstance();

        try {
            df.parse(strDate);
            Calendar c = df.getCalendar();
            int day = c.get(7) - 1;
            return day;
        } catch (ParseException var4) {
            return -1;
        }
    }

    public static String getSysDate() {
        SimpleDateFormat d = new SimpleDateFormat();
        d.applyPattern("yyyy-MM-dd");
        Date nowdate = new Date();
        String str_date = d.format(nowdate);
        return str_date;
    }

    public static String getSysdateTime() {
        SimpleDateFormat lFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String gRtnStr = lFormat.format(new Date());
        return gRtnStr;
    }

    public static String getDateTime(Date date) {
        SimpleDateFormat d = new SimpleDateFormat();
        d.applyPattern("yyyy-MM-dd HH:mm:ss");
        String str_date = d.format(date);
        return str_date;
    }

    public static int getDayNum(int year, int month) {
        if (month != 2) {
            String SmallMonth = ",4,6,9,11,";
            return SmallMonth.indexOf(String.valueOf(String.valueOf((new StringBuffer(",")).append(String.valueOf(month)).append(",")))) < 0 ? 31 : 30;
        } else {
            return year % 400 == 0 || year % 4 == 0 && year % 100 != 0 ? 29 : 28;
        }
    }

    public static int DateDiff(Date date1, Date date2) {
        int i = (int)((date1.getTime() - date2.getTime()) / 3600L / 24L / 1000L);
        return i;
    }

    public static int getYearMonthDate(String strDate, String style) {
        if (strDate == null) {
            return 0;
        } else {
            int firstDash = strDate.indexOf(45);
            int secondDash = strDate.indexOf(45, firstDash + 1);
            if (firstDash > 0 & secondDash > 0 & secondDash < strDate.length() - 1) {
                int year = Integer.parseInt(strDate.substring(0, firstDash));
                int month = Integer.parseInt(strDate.substring(firstDash + 1, secondDash));
                int day = Integer.parseInt(strDate.substring(secondDash + 1));
                if (style.equalsIgnoreCase("Y")) {
                    return year;
                } else if (style.equalsIgnoreCase("M")) {
                    return month;
                } else {
                    return style.equalsIgnoreCase("D") ? day : 0;
                }
            } else {
                return 0;
            }
        }
    }

    public static boolean isYearFirstDay(java.sql.Date date) {
        boolean i = false;
        if (getYearMonthDate(date.toString(), "M") == 1 && getYearMonthDate(date.toString(), "D") == 1) {
            i = true;
        }

        return i;
    }

    public static boolean isHalfYearFirstDay(java.sql.Date date) {
        boolean i = false;
        if (getYearMonthDate(date.toString(), "M") == 1 && getYearMonthDate(date.toString(), "D") == 1 || getYearMonthDate(date.toString(), "M") == 7 && getYearMonthDate(date.toString(), "D") == 1) {
            i = true;
        }

        return i;
    }

    public static String getHalfYearFirstDay(java.sql.Date date) {
        String month = "01";
        if (getYearMonthDate(date.toString(), "M") >= 7) {
            month = "07";
        }

        String day = Integer.toString(getYearMonthDate(date.toString(), "Y")) + "-" + month + "-01";
        return day;
    }

    public static boolean isHalfYearLastDay(java.sql.Date date) {
        boolean i = false;
        if (getYearMonthDate(date.toString(), "M") == 12 && getYearMonthDate(date.toString(), "D") == 31 || getYearMonthDate(date.toString(), "M") == 6 && getYearMonthDate(date.toString(), "D") == 30) {
            i = true;
        }

        return i;
    }

    public static String getHalfYearLastDay(java.sql.Date date) {
        String month = "-06-30";
        if (getYearMonthDate(date.toString(), "M") >= 7) {
            month = "-12-31";
        }

        String day = Integer.toString(getYearMonthDate(date.toString(), "Y")) + "-" + month;
        return day;
    }

    public static boolean isYearLastDay(java.sql.Date date) {
        boolean i = false;
        if (getYearMonthDate(date.toString(), "M") == 12 && getYearMonthDate(date.toString(), "D") == 31) {
            i = true;
        }

        return i;
    }

    public static boolean isQuarterFirstDay(java.sql.Date date) {
        boolean i = false;
        if ((getYearMonthDate(date.toString(), "M") == 1 || getYearMonthDate(date.toString(), "M") == 4 || getYearMonthDate(date.toString(), "M") == 7 || getYearMonthDate(date.toString(), "M") == 10) && getYearMonthDate(date.toString(), "D") == 1) {
            i = true;
        }

        return i;
    }

    public static String getQuarterFirstDay(java.sql.Date date) {
        String month = "01";
        if (getYearMonthDate(date.toString(), "M") >= 10) {
            month = "10";
        } else if (getYearMonthDate(date.toString(), "M") >= 7) {
            month = "07";
        } else if (getYearMonthDate(date.toString(), "M") >= 4) {
            month = "04";
        } else if (getYearMonthDate(date.toString(), "M") >= 1) {
            month = "01";
        }

        String day = Integer.toString(getYearMonthDate(date.toString(), "Y")) + "-" + month + "-01";
        return day;
    }

    public static boolean isQuarterLastDay(java.sql.Date date) {
        boolean i = false;
        if (getYearMonthDate(date.toString(), "M") == 3 && getYearMonthDate(date.toString(), "D") == 31) {
            i = true;
        }

        if (getYearMonthDate(date.toString(), "M") == 6 && getYearMonthDate(date.toString(), "D") == 30) {
            i = true;
        }

        if (getYearMonthDate(date.toString(), "M") == 9 && getYearMonthDate(date.toString(), "D") == 30) {
            i = true;
        }

        if (getYearMonthDate(date.toString(), "M") == 12 && getYearMonthDate(date.toString(), "D") == 31) {
            i = true;
        }

        return i;
    }

    public static String getQuarterLastDay(java.sql.Date date) {
        String month = "-03-31";
        if (getYearMonthDate(date.toString(), "M") >= 10) {
            month = "-12-31";
        } else if (getYearMonthDate(date.toString(), "M") >= 7) {
            month = "-09-30";
        } else if (getYearMonthDate(date.toString(), "M") >= 4) {
            month = "-06-30";
        }

        String day = Integer.toString(getYearMonthDate(date.toString(), "Y")) + "-" + month;
        return day;
    }

    public static boolean isMonthLastDay(java.sql.Date date) {
        boolean i = false;
        java.sql.Date des_date = null;
        String str_date = date.toString();
        String year = str_date.substring(0, str_date.indexOf("-"));
        int m = new Integer(str_date.substring(str_date.indexOf("-") + 1, str_date.lastIndexOf("-"))) + 1;
        String month = (new Integer(m)).toString();
        if (m < 10) {
            month = "0" + month;
        }

        java.sql.Date mid_date = null;
        mid_date = java.sql.Date.valueOf(year + "-" + month + "-01");
        des_date = DateAdd(mid_date, -1);
        if (DateDiff(des_date, date) == 0) {
            i = true;
        }

        return i;
    }

    public static boolean isMonthFisrtDay(java.sql.Date date) {
        boolean i = false;
        if (getYearMonthDate(date.toString(), "D") == 1) {
            i = true;
        }

        return i;
    }

    public static String getMonthFisrtDay(java.sql.Date date) {
        String month;
        if (getYearMonthDate(date.toString(), "M") > 9) {
            month = Integer.toString(getYearMonthDate(date.toString(), "M"));
        } else {
            month = "0" + Integer.toString(getYearMonthDate(date.toString(), "M"));
        }

        String day = Integer.toString(getYearMonthDate(date.toString(), "Y")) + "-" + month + "-01";
        return day;
    }

    public static String getDate(Date date) {
        return getDate(date, "yyyy-MM-dd HH:mm:ss");
    }



    public static Date formatDate(String datestr, String format) {
        try {
            if (StringUtils.isEmpty(datestr)) {
                return null;
            } else {
                SimpleDateFormat myFormat = new SimpleDateFormat(format);
                Date date = myFormat.parse(datestr);
                return date;
            }
        } catch (Exception var4) {
            return null;
        }
    }

    public static java.sql.Date getSqlDate(Date date) {
        java.sql.Date result = null;

        try {
            SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
            String mystrdate = myFormat.format(date);
            result = java.sql.Date.valueOf(mystrdate);
            return result;
        } catch (Exception var4) {
            return null;
        }
    }

    public static Date addMonth(Date date, int month) {
        String strdate = getDate(date, "yyyy-MM-dd");
        int curmonth = Integer.parseInt(strdate.substring(5, 7));
        int year = Integer.parseInt(strdate.substring(0, 4));
        int addyear = month / 12;
        year += addyear;
        curmonth += month % 12;
        if (curmonth > 12) {
            curmonth = 1;
            ++year;
        }

        String strmonth = String.valueOf(curmonth);
        if (strmonth.length() == 1) {
            strmonth = "0" + strmonth;
        }

        strdate = year + "-" + strmonth + "-" + strdate.substring(8, 10);
        return formatDate(strdate, "yyyy-MM-dd");
    }


    public static String getDate() {
        SimpleDateFormat d = new SimpleDateFormat();
        d.applyPattern("yyyy-MM-dd");
        Date nowdate = new Date();
        String str_date = d.format(nowdate);
        return str_date;
    }

    public static String getShortDate(String dt) {
        if (dt != null) {
            SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");

            try {
                Date date = myFormat.parse(dt);
                return getDate(date, "yyyyMMdd");
            } catch (ParseException var3) {
                return dt;
            }
        } else {
            return dt;
        }
    }

    public static String getLongDate(String dt) {
        if (dt != null) {
            SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");

            try {
                Date date = myFormat.parse(dt);
                return getDate(date, "yyyy-MM-dd");
            } catch (ParseException var3) {
                return dt;
            }
        } else {
            return dt;
        }
    }

    public static boolean isSameYearMonth(String date) {
        try {
            String currdate = getSysDate();
            currdate = getShortDate(currdate).substring(0, 6);
            String lastdate = getShortDate(date).substring(0, 6);
            return lastdate.equals(currdate);
        } catch (NumberFormatException var3) {
            return false;
        }
    }

    public static java.sql.Date DateAdd(java.sql.Date date, int addday) {
        java.sql.Date datenew = null;
        int year = getYearMonthDate(date.toString(), "Y");
        int month = getYearMonthDate(date.toString(), "M");
        int day = getYearMonthDate(date.toString(), "D");
        day += addday;
        String dayStr = Integer.toString(year) + "-" + Integer.toString(month) + "-" + Integer.toString(day);
        datenew = java.sql.Date.valueOf(dayStr);
        return datenew;
    }

    public static java.sql.Date DateBefore(java.sql.Date date, int addday) {
        java.sql.Date datenew = null;
        int year = getYearMonthDate(date.toString(), "Y");
        int month = getYearMonthDate(date.toString(), "M");
        int day = getYearMonthDate(date.toString(), "D");
        day -= addday;
        String dayStr = Integer.toString(year) + "-" + Integer.toString(month) + "-" + Integer.toString(day);
        datenew = java.sql.Date.valueOf(dayStr);
        return datenew;
    }

    public static String getShortDate(Date date) {
        if (date == null) {
            date = new Date();
        }

        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
        String shortDate = myFormat.format(date);
        return shortDate;
    }

    public static String getLongDate(Date date) {
        if (date == null) {
            date = new Date();
        }

        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String longDate = myFormat.format(date);
        return longDate;
    }

    public static String getTimestampStr() {
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long stamp = System.currentTimeMillis();
        String timestamp = myFormat.format(stamp);
        return timestamp;
    }

    public static Timestamp getTimestamp() {
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long stamp = System.currentTimeMillis();
        String timestamp = myFormat.format(stamp);
        return Timestamp.valueOf(timestamp);
    }

    public static String getTS() {
        long ts = System.currentTimeMillis();
        return Long.toString(ts);
    }

    public static void main(String[] args) {
        System.out.println(getTS());
    }

    public static String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("kk:mm:ss");
        String time = sdf.format(Calendar.getInstance().getTime());
        return time;
    }
}

