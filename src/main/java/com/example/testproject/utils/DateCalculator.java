package com.example.testproject.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateCalculator {

    public static String formatDate(LocalDateTime dateTime) {
        LocalDateTime now = LocalDateTime.now();
        LocalDate date = dateTime.toLocalDate();
        LocalDate today = now.toLocalDate();
        LocalDate yesterday = today.minusDays(1);

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        DateTimeFormatter dayMonthFormatter = DateTimeFormatter.ofPattern("ddMMM.");
        DateTimeFormatter dayMonthYearFormatter = DateTimeFormatter.ofPattern("ddMMM. yyyy'г.'");

        if (date.isEqual(today)) {
            return "Сегодня в " + dateTime.format(timeFormatter);
        } else if (date.isEqual(yesterday)) {
            return "Вчера в " + dateTime.format(timeFormatter);
        } else if (date.getYear() == today.getYear()) {
            return dateTime.format(dayMonthFormatter) + " в " + dateTime.format(timeFormatter);
        } else {
            return dateTime.format(dayMonthYearFormatter) + " в " + dateTime.format(timeFormatter);
        }
    }
}
