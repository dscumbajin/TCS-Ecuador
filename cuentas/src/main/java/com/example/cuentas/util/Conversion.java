package com.example.cuentas.util;

import com.example.cuentas.exception.MovimientoNotFoundException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Conversion {
    public static Date convertStringToDate(String dateString) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            return simpleDateFormat.parse(dateString);
        } catch (ParseException e) {
            throw new MovimientoNotFoundException("Error al parsear la fecha: " + e.getMessage() + " Formato valido: dd/MM/yyyy");
        }
    }
    
    public static Date convertStringToStartOfDay(String dateString) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date date = simpleDateFormat.parse(dateString);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            return calendar.getTime();
        } catch (ParseException e) {
            throw new MovimientoNotFoundException("Error al parsear la fecha: " + e.getMessage() + " Formato valido: dd/MM/yyyy");
        }
    }
    
    public static Date convertStringToEndOfDay(String dateString) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date date = simpleDateFormat.parse(dateString);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            calendar.set(Calendar.MILLISECOND, 999);
            return calendar.getTime();
        } catch (ParseException e) {
            throw new MovimientoNotFoundException("Error al parsear la fecha: " + e.getMessage() + " Formato valido: dd/MM/yyyy");
        }
    }
    
    public static String convertDateToString(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return simpleDateFormat.format(date);
    }
}
