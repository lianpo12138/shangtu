package com.dubu.turnover.domain.enums;



public interface MessagesEnums {

  
    enum Status {
        VALID, INVALID
    }

    enum StatisticTypes {
        DAY, WEEK, MONTH, TOTAL
    }

    enum OrderStatus {
        BUYING, SUCCESS, FAIL
    }

    enum ApplyStatus {
        NO_APPLY, WAIT_AUDIT, ACCEPT, REFUSE
    }

    /**
     * 阅读消息大类，INFO:资讯消息，USER_PUSH：评论我的，
     */
    enum ReadType {
        INFO("资讯消息"),
        USER_PUSH("消息推送"),
        INVITE("邀请消息"),
        NOTICE("系统消息");
        private String value;

        ReadType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
