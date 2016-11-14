package com.company;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public abstract class Predicate <T> {
    abstract public String SelectWhereStatement(String attribute, boolean is_first);

    protected String FormatParameter(Object parameter) {
        if (parameter == null) {
            return "NULL";
        } else {
            if (parameter instanceof String) {
                return "'" + ((String) parameter).replace("'", "''") + "'";
            } else if (parameter instanceof Timestamp) {
                return "to_timestamp('" + new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS").
                        format(parameter) + "', 'mm/dd/yyyy hh24:mi:ss.ff3')";
            } else if (parameter instanceof Date) {
                return "to_date('" + new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").
                        format(parameter) + "', 'mm/dd/yyyy hh24:mi:ss')";
            } else if (parameter instanceof Boolean) {
                return ((Boolean) parameter).booleanValue() ? "1" : "0";
            } else {
                return parameter.toString();
            }
        }
    }
}
