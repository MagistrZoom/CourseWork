package com.company;

import sun.rmi.runtime.Log;

public class PredicateSingle <T> extends Predicate <T> {
    private Logic m_logic;
    private Operation m_operation;
    private T m_value;

    public enum Operation { EQ, NEQ, L, LE, G, GE }
    public enum Logic { OR, AND }

    public PredicateSingle(Logic logic, Operation operation, T value) {
        m_logic = logic;
        m_operation = operation;
        m_value = value;
    }

    @Override
    public String SelectWhereStatement(String attribute, boolean is_first) {
        String query = "";
        if (!is_first) {
            query += GetStringLogic() + " ";
        }

        query += attribute +
                " " + GetStringOperation() +
                " " + String.valueOf(m_value);

        return query;
    }

    private String GetStringLogic() {
        switch (m_logic) {
            case OR:
                return "OR";
            case AND:
                return "AND";
        }

        return "";
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
