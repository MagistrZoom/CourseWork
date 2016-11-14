package com.company;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Class is used to describe WHERE clause
 */
public class PredicateList<T> extends Predicate<T> {
    private Operation m_operation;
    private ArrayList<T> m_value;

    public enum Operation { AND, OR, NOT, NOP }

    public PredicateList(Operation operation, ArrayList<T> value) {
        m_operation = operation;
        m_value = value;
    }

    public ArrayList<T> GetValue() {
        return m_value;
    }

    public void AddValue(T val) { m_value.add(val); }

    @Override
    public String GenerateSQL(String attribute, boolean is_first) {
        String query = "";
        if (m_value.isEmpty()) {
            return "";
        }

        if (m_operation == PredicateList.Operation.AND ||
                m_operation == PredicateList.Operation.OR &&
                !is_first) {
            query += GetStringOperation() + " ";
        }
        query += FormatParameter(attribute) + " ";
        if (PredicateList.Operation.NOT == m_operation) {
            query += GetStringOperation() + " ";
        }
        query += "IN (";
        for (Iterator<T> iter = m_value.iterator(); iter.hasNext(); ) {
            T cid = iter.next();
            query += FormatParameter(cid);
            if (iter.hasNext()) {
                query += ",";
            }
        }
        query += ") ";

        return query;
    }

    private String GetStringOperation() {
        switch (m_operation) {
            case AND:
                return "AND";
            case OR:
                return "OR";
            case NOT:
                return "NOT";
        }
        return "";
    }
}
