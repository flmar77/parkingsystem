package com.parkit.parkingsystem.constants;

public abstract class DBConstants {

    public static final String GET_NEXT_PARKING_SPOT = "select min(PARKING_NUMBER) from parking where AVAILABLE = true and TYPE = ?";
    public static final String UPDATE_PARKING_SPOT = "update parking set available = ? where PARKING_NUMBER = ?";

    public static final String SAVE_TICKET = "insert into ticket(PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME, DISCOUNT) values(?,?,?,?,?,?)";
    public static final String UPDATE_TICKET = "update ticket set PRICE=?, OUT_TIME=? where ID=?";
    public static final String GET_CURRENT_TICKET = "select t.PARKING_NUMBER, t.ID, t.PRICE, t.IN_TIME, t.OUT_TIME, t.DISCOUNT, p.TYPE from ticket t,parking p where p.parking_number = t.parking_number and t.VEHICLE_REG_NUMBER=? and t.OUT_TIME IS NULL";
    public static final String SEARCH_VEHICLE_REG_NUMBER = "select t.ID from ticket t where t.VEHICLE_REG_NUMBER=? limit 1";

    public static final String CLEAN_TEST_PARKING = "update parking set available = true";
    public static final String SET_PARKING_NOT_AVAILABLE = "update parking set available = false";
    public static final String CLEAN_TEST_TICKET = "truncate table ticket";
}
