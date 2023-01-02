package com.h2p.builders.select;

public class Condition<B> {
    StringBuilder conditionString;

    private Condition(Builder<B> builder) {
        conditionString = builder.conditionString;
    }

    public String toQuery() {
        return conditionString.toString();
    }
    public static <B> Builder<B> newBuilder() {
        return new Builder<>();
    }

    public static final class Builder<B> extends NestedBuilder<B, Condition<B>> {
        StringBuilder conditionString = new StringBuilder();

        public void setConditionString(String conditionString) {
            this.conditionString.append(conditionString);
        }

        public Builder<B> and() {
            this.conditionString.append(" AND ");
            return this;
        }

        public Builder<B> or() {
            this.conditionString.append(" OR ");
            return this;
        }

        public Builder<B> logic(String str) {
            this.conditionString.append(" ").append(str).append(" ");
            return this;
        }

        @Override
        public Condition<B> build() {
            return new Condition<B>(this);
        }
    }
}
