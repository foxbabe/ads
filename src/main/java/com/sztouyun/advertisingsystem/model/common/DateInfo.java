package com.sztouyun.advertisingsystem.model.common;

import com.sztouyun.advertisingsystem.utils.UUIDUtils;

import javax.persistence.*;
import java.util.Date;


@Entity
@Table(indexes = {
        @Index(name = "index_date_year_month_day",columnList = "date,year,month,day")
        })
public class DateInfo {
    @Id
    @Column(nullable = false,length = 36)
    private String id = UUIDUtils.generateOrderedUUID();

    @Column(nullable = false,name = "date")
    private Date date;

    @Column(nullable = false,name = "year")
    private int year;

    @Column(nullable = false,name = "month")
    private int month;

    @Column(nullable = false,name = "day")
    private int day;

    @Column(nullable = false)
    private int week;

    @Column(nullable = false)
    private int dayOfYear;

    @Column(nullable = false)
    private int weekOfYear;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public int getDayOfYear() {
        return dayOfYear;
    }

    public void setDayOfYear(int dayOfYear) {
        this.dayOfYear = dayOfYear;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getWeekOfYear() {
        return weekOfYear;
    }

    public void setWeekOfYear(int weekOfYear) {
        this.weekOfYear = weekOfYear;
    }
}
