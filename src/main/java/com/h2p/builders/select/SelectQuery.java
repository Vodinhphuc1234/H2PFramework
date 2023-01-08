package com.h2p.builders.select;

public class SelectQuery {
    Condition<Builder> condition;
    GroupBy groupBy;

    private SelectQuery(Builder builder) {
        this.condition = builder.condition;
        this.groupBy = builder.groupBy;
    }

    public String toQuery(String tableName, String subQuery) {
        String initQuery = " SELECT %s FROM %s " + subQuery;
        if (groupBy == null || groupBy.columns == null || groupBy.columns.isEmpty()){
            initQuery = String.format(initQuery, "*", tableName);
        } else {
            initQuery = String.format(initQuery, groupBy.columns, tableName);
        }
        StringBuilder whereBuilder = new StringBuilder(initQuery);
        if (condition != null && condition.toQuery() != null && !condition.toQuery().isEmpty()) {
            whereBuilder.append(" WHERE ").append(condition.toQuery());
        }
        if (groupBy != null && groupBy.toQuery() != null && !groupBy.toQuery().isEmpty()) {
            whereBuilder.append(groupBy.toQuery());
        }
        return whereBuilder.append(" ").toString();
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {
        Condition<Builder> condition;
        GroupBy groupBy;

        private void setCondition(Condition<Builder> condition) {
            this.condition = condition;
        }

        private Builder setGroupBy(GroupBy groupBy) {
            this.groupBy = groupBy;
            return this;
        }

        // to add manually
        private final Condition.Builder<Builder> builderWhere = Condition.<Builder>newBuilder().withParentBuilder(this);
        private final GroupBy.Builder builderGroupBy = GroupBy.newBuilder().withParentBuilder(this);

        public Condition.Builder<Builder> where() {
            return this.builderWhere;
        }

        public GroupBy.Builder groupBy() {
            return this.builderGroupBy;
        }

        public void reset() {
            this.condition = null;
            this.groupBy = null;
            builderWhere.reset();
            builderGroupBy.reset();
        }

        public SelectQuery build() {
            return new SelectQuery(this);
        }
    }
}
