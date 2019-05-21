package com.utils;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Utils {
    public static final Logger logger = LoggerFactory.getLogger(Utils.class);
    Calendar calendar = new GregorianCalendar();

    static {
        try {
            setConfiguration("./src/main/resources/config.properties");
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
    }

    private static PropertiesConfiguration config;


    public static void setConfiguration(String file) throws ConfigurationException {
        config = new PropertiesConfiguration(file);
    }

    public static String getConfig(String configKey) {
        return config.getString(configKey);

    }

    public static String getUUID() {
        return UUID.randomUUID().toString();
    }

    public static String getDateInPatternGMT(String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        return format.format(new Date());

    }


    public static boolean isTimeBetweenTwoTime(String initialTime, String finalTime, String currentTime) throws ParseException {
        String reg = "^([0-1][0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])$";
        if (initialTime.matches(reg) && finalTime.matches(reg) && currentTime.matches(reg)) {
            boolean valid = false;
            //Start Time
            Date inTime = new SimpleDateFormat("HH:mm:ss").parse(initialTime);
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(inTime);
            //Current Time
            Date checkTime = new SimpleDateFormat("HH:mm:ss").parse(currentTime);
            Calendar calendar3 = Calendar.getInstance();
            calendar3.setTime(checkTime);
            //End Time
            Date finTime = new SimpleDateFormat("HH:mm:ss").parse(finalTime);
            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTime(finTime);
            if (finalTime.compareTo(initialTime) < 0) {
                calendar2.add(Calendar.DATE, 1);
                calendar3.add(Calendar.DATE, 1);
            }
            Date actualTime = calendar3.getTime();
            if ((actualTime.after(calendar1.getTime()) || actualTime.compareTo(calendar1.getTime()) == 0)
                    && actualTime.before(calendar2.getTime())) {
                valid = true;
            }
            return valid;
        } else {
            throw new IllegalArgumentException("Not a valid time, expecting HH:MM:SS format");
        }
    }



    /***
     * coverts GMT time to IST time
     * @param strDate format- yyyy-mm-dd HH:mm:ss
     * @return IST time in format - yyyy-mm-dd HH:mm:ss
     */

    public static String convertToIST_Date(String strDate)  {


        DateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        utcFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        DateFormat indianFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        indianFormat.setTimeZone(TimeZone.getTimeZone("IST"));
        Date timestamp = null;
        try {
            timestamp = utcFormat.parse(strDate);
            System.out.println(timestamp);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String istTime = indianFormat.format(timestamp);
        System.out.println(istTime);
        return istTime;
    }
}
