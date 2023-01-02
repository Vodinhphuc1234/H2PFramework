package com.h2p.builders.select;

/**
 * Created by vodinhphuc on 30/12/2022
 */
public class SelectQuery {
    SelectCondition condition;
    GroupBy groupBy;

    private SelectQuery(Builder builder) {
        this.condition = builder.selectCondition;
        this.groupBy = builder.groupBy;
    }

    public String toQuery() {
        StringBuilder whereBuilder = new StringBuilder();
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
        SelectCondition selectCondition;
        GroupBy groupBy;

        public void setSelectCondition(SelectCondition selectCondition) {
            this.selectCondition = selectCondition;
        }

        public Builder setGroupBy(GroupBy groupBy) {
            this.groupBy = groupBy;
            return this;
        }

        // to add manually
        private final SelectCondition.Builder builderWhere = SelectCondition.newBuilder().withParentBuilder(this);
        private final GroupBy.Builder builderGroupBy = GroupBy.newBuilder().withParentBuilder(this);

        public SelectCondition.Builder where() {
            return this.builderWhere;
        }

        public GroupBy.Builder groupBy() {
            return this.builderGroupBy;
        }

        public void reset() {
            this.selectCondition = null;
            this.groupBy = null;
        }

        public SelectQuery build() {
            return new SelectQuery(this);
        }
    }
}
