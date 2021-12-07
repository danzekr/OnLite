package com.iwdael.dblite;

/**
 * author  : iwdael
 * e-mail  : iwdael@outlook.com
 * github  : http://github.com/iwdael
 * project : DbLite
 */
public class BinaryCondition extends Condition {
    public enum Operator {
        EQUAL(" = "),
        DIFF(" != "),
        GREATER_THAN(" > "),
        LESS_THAN(" < ");
        private final String operator;

        Operator(String operator) {
            this.operator = operator;
        }
    }

    private Operator operator;
    private String field;
    private String value;

    public BinaryCondition() {
    }

    public BinaryCondition(String field, Operator operator, String value) {
        this.operator = operator;
        this.field = field;
        this.value = value;
    }

    public BinaryCondition setField(String field) {
        this.field = field;
        return this;
    }

    public BinaryCondition setOperator(Operator operator) {
        this.operator = operator;
        return this;
    }

    public void setValue(Object value) {
        this.value = String.valueOf(value);
    }

    @Override
    public String whereClause() {
        return String.format(String.format("%s%s?", field, operator.operator));
    }

    @Override
    public String[] whereArgs() {
        return new String[]{value};
    }

}
