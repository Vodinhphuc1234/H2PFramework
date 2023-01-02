package com.h2p.builders.select;

public class GroupBy {
    String columns;
    Condition<Builder> condition;

    private GroupBy(Builder builder) {
        columns = builder.columns;
        condition = builder.condition;
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
        Condition<Builder> condition;

        private void setColumns(String columns) {
            this.columns = columns;
        }

        private void setCondition(Condition<Builder> condition) {
            this.condition = condition;
        }

        private final Condition.Builder<Builder> builderHaving = Condition.<Builder>newBuilder().withParentBuilder(this);

        public Condition.Builder<Builder> having() {
            return this.builderHaving;
        }
        public GroupBy.Builder columns(String columns) {
            this.columns = columns;
            return this;
        }
        @Override
        protected GroupBy build() {
            return new GroupBy(this);
        }
    }
}
