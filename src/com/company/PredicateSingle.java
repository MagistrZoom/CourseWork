package com.company;

public class PredicateSingle <T> extends Predicate <T> {
    private Operation m_operation;
    private T m_value;

    public enum Operation { EQ, NEQ, L, LE, G, GE };

    public PredicateSingle(Operation operation, T value) {
        m_operation = operation;
        m_value = value;
    }

    @Override
    public String GenerateSQL(String attribute, boolean is_first) {
        String query;
        query = attribute +
                " " + GetStringOperation() +
                " " + String.valueOf(m_value);

        return query;
    }

    private String GetStringOperation() {
        switch (m_operation) {
            case EQ:
                return "=";
            case NEQ:
                return "!=";
            case L:
                return "<";
            case LE:
                return "<=";
            case G:
                return ">";
            case GE:
                return ">=";
        }

        return "";
    }
}
