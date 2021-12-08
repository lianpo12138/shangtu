package com.dubu.turnover.domain.enums;



/**
 *
 */
public enum Direction implements IEnum {

		IN("收入"), OUT("支出");
		private String descr;

        private Direction(String descr) {
            this.descr = descr;
        }

        public String getName() {
            return super.name();
        }

        public String getDescr() {
            return descr;
        }
   
	
}
