package com.h2p.builders.select;

/**
 * Created by vodinhphuc on 30/12/2022
 */
public class GroupBy {
    String columns;
    HavingCondition condition;

    private GroupBy(Builder builder) {
        columns = builder.columns;
        condition = builder.havingCondition;
    }
    public static Builder newBuilder() {
        return new Builder();
    }

    public String toQuery() {
        StringBuilder groupByBuilder = new StringBuilder();
        if (columns != null){
            groupByBuilder.append(" GROUP BY ").append(columns);
        }
        if (condition != null && condition.toQuery() != null && ! condition.toQuery().isEmpty() ){
            groupByBuilder.append(" HAVING ").append(condition.toQuery());
        }
        return groupByBuilder.toString();
    }

    public static final class Builder extends NestedBuilder<SelectQuery.Builder, GroupBy> {
        String columns;
        HavingCondition havingCondition;

        public void setColumns(String columns) {
            this.columns = columns;
        }

        public void setHavingCondition(HavingCondition havingCondition) {
            this.havingCondition = havingCondition;
        }

        private final HavingCondition.Builder builderHaving = HavingCondition.newBuilder().withParentBuilder(this);

        public HavingCondition.Builder having() {
            return this.builderHaving;
        }
        public GroupBy.Builder columns(String columns) {
            this.columns = columns;
            return this;
        }
        @Override
        public GroupBy build() {
            return new GroupBy(this);
        }
    }
}
