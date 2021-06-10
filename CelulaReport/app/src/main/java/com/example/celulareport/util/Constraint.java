package com.example.celulareport.util;

import java.util.ArrayList;

public interface Constraint {

    public static final String MONTH_SELECTED = "com.example.celulareport.MONTH";
    public static final String MONTH_VIEW_TRANSITION = "com.example.celulareport.MONTH_TRANSITION";
    public static final String[] MONTHS = new String[]{

            "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho",
            "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"

    };

    //Moths
    public static final String VALUE_JANUARY = "01";
    public static final String VALUE_FEBRUARY = "02";
    public static final String VALUE_MARCH = "03";
    public static final String VALUE_APRIL = "04";
    public static final String VALUE_MAY = "05";
    public static final String VALUE_JUNE = "06";
    public static final String VALUE_JULY = "07";
    public static final String VALUE_AUGUST = "08";
    public static final String VALUE_SEPTEMBER = "09";
    public static final String VALUE_OCTOBER = "10";
    public static final String VALUE_NOVEMBER = "11";
    public static final String VALUE_DECEMBER = "12";

    //Moths
    public static final String STRING_JANUARY = "Janeiro";
    public static final String STRING_FEBRUARY = "Fevereiro";
    public static final String STRING_MARCH = "Março";
    public static final String STRING_APRIL = "Abril";
    public static final String STRING_MAY = "Maio";
    public static final String STRING_JUNE = "Junho";
    public static final String STRING_JULY = "Julho";
    public static final String STRING_AUGUST = "Agosto";
    public static final String STRING_SEPTEMBER = "Setembro";
    public static final String STRING_OCTOBER = "Outubro";
    public static final String STRING_NOVEMBER = "Novembro";
    public static final String STRING_DECEMBER = "Dezembro";


    public static final int ID = 0;
    public static final int IMG_CELULA = 1;
    public static final int CELULA = 2;
    public static final int LIDER = 3;
    public static final int COLIDER = 4;
    public static final int MEETING_DATE = 5;
    public static final int N_MEMBERS = 6;
    public static final int N_VISITORS = 7;
    public static final int COMMITS = 8;
    public static final int OFERTA = 9;
}
