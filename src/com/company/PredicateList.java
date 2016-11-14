package com.company;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Class is used to describe WHERE clause
 */
public class PredicateList<T> extends Predicate<T> {
    private Logic m_logic;
    private boolean m_not;
    private ArrayList<T> m_value;

    public enum Logic { AND, OR }

    public PredicateList(Logic logic, boolean not, ArrayList<T> value) {
        m_not = not;
        m_logic = logic;
        m_value = value;
    }

    public ArrayList<T> GetValue() {
        return m_value;
    }

    public void AddValue(T val) { m_value.add(val); }

    @Override
    public String SelectWhereStatement(String attribute, boolean is_first) {
        String query = "";
        if (m_value.isEmpty()) {
            return "";
        }

        if (m_logic == Logic.AND ||
                m_logic == Logic.OR &&
                !is_first) {
            query += GetStringOperation() + " ";
        }
        query += attribute + " ";
        if (m_not) {
            query += "NOT ";
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
        switch (m_logic) {
            case AND:
                return "AND";
            case OR:
                return "OR";
        }
        return "";
    }
}
