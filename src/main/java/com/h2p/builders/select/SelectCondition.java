//package com.h2p.builders.select;
//
//public class SelectCondition {
//    StringBuilder conditionString;
//
//    private SelectCondition(Builder builder) {
//        conditionString = builder.conditionString;
//    }
//
//    public static Builder newBuilder() {
//        return new Builder();
//    }
//
//    public String toQuery() {
//        return conditionString.toString();
//    }
//
//    public static final class Builder extends NestedBuilder<SelectQuery.Builder, SelectCondition> {
//        StringBuilder conditionString = new StringBuilder();
//
//        private void setConditionString(StringBuilder conditionString) {
//            this.conditionString = conditionString;
//        }
//
//        public Builder and() {
//            this.conditionString.append(" AND ");
//            return this;
//        }
//
//        public Builder or() {
//            this.conditionString.append(" OR ");
//            return this;
//        }
//
//        public Builder logic(String str) {
//            this.conditionString.append(" ").append(str).append(" ");
//            return this;
//        }
//
//        @Override
//        public SelectCondition build() {
//            return new SelectCondition(this);
//        }
//    }
//}
