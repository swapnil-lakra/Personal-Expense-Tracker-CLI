package com.expensetracker.util;

import java.time.LocalDate;

import com.expensetracker.constants.AppConstants;

public final class ConsoleUtil {
    private ConsoleUtil(){}

    private static String spaces(int count){
        if(count <= 0){
            return "";
        }

        StringBuilder sb = new StringBuilder(count);
        for(int i = 0;i < count;i++){
            sb.append(' ');
        }
        return sb.toString();
    }

    public static void printLine(int length, char symbol){
        if(length <= 0){
            return;
        }

        StringBuilder sb = new StringBuilder();
        for(int i = 0;i < length;i++){
            sb.append(symbol);
        }
        System.out.println(sb.toString());
    }

    public static void printLine(int length){
        printLine(length, '=');
    }

    public static void printLine(){
        printLine(AppConstants.MENU_SEPARATOR_LENGTH, '=');
    }

    public static void printHeader(String title){
        printLine();
        if(title != null && !title.isEmpty()){
            int width = AppConstants.MENU_SEPARATOR_LENGTH;
            int padding = Math.max(0, (width - title.length()) / 2);
            System.out.println(spaces(padding)+title);
        } else {
            System.out.println();
        }
        printLine();
    }

    public static String formatAmount(double amount){
        return String.format("Rs. %.2f", amount);
    }

    public static String formatDate(LocalDate date){
        return DateUtil.format(date);
    }

    public static String formatPercentage(double percent){
        return String.format("%.1f%%", percent);
    }
}
