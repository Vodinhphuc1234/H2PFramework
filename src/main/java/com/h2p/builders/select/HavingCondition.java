package com.h2p.builders.select;

/**
 * Created by vodinhphuc on 30/12/2022
 */
public class HavingCondition {
    StringBuilder conditionString;

    private HavingCondition(Builder builder) {
        conditionString = builder.conditionString;
    }

    public String toQuery() {
        return conditionString.toString();
    }
    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder extends NestedBuilder<GroupBy.Builder, HavingCondition> {
        StringBuilder conditionString = new StringBuilder();

        public void setConditionString(String conditionString) {
            this.conditionString.append(conditionString);
        }

        public Builder and() {
            this.conditionString.append(" AND ");
            return this;
        }

        public Builder or() {
            this.conditionString.append(" OR ");
            return this;
        }

        public Builder logic(String str) {
            this.conditionString.append(" ").append(str).append(" ");
            return this;
        }

        @Override
        public HavingCondition build() {
            return new HavingCondition(this);
        }
    }
}
