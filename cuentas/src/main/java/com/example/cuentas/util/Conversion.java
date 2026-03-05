package com.example.cuentas.util;

import com.example.cuentas.exception.MovimientoNotFoundException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Conversion {
    public static Date convertStringToDate(String dateString) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d/M/yyyy");
            return simpleDateFormat.parse(dateString);
        } catch (ParseException e) {
            throw new MovimientoNotFoundException("Error al parsear la fecha: " + e.getMessage() + " Formato valido: d/M/yyyy");
        }
    }
    public static String convertDateToString(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d/M/yyyy");
        return simpleDateFormat.format(date);
    }
}
