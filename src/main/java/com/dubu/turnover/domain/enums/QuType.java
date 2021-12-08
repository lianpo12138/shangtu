package com.dubu.turnover.domain.enums;

/**
 * QuType
 * @author yehefeng
 *
 * http://www.dubuinfo.com
 * 上海笃步
 */
public enum QuType{

	YESNO("是非题"), 
	RADIO("单选题"), 
	CHECKBOX("多选题"), 
	FILLBLANK("填空题"),	
	COMPRADIO("复合单选"), 
	COMPCHECKBOX("复合多选"), 	
	MULTIFILLBLANK("多项填空题"),//组合填空题
	ANSWER("多行填空题"),//原问答题
	BIGQU("大题"),	
	ENUMQU("枚举题"),
	SCORE("评分题"),
	ORDERQU("排序题"), 
	PROPORTION ("比重题"), 
	CHENRADIO("矩阵单选题"), 
	CHENFBK("矩阵填空题"), 
	CHENCHECKBOX("矩阵多选题"),
	COMPCHENRADIO("复合矩阵单选题"), 	
	UPLOADFILE("文件上传题"),
	PAGETAG("分页标记"),
	PARAGRAPH("段落说明"),
	CHENSCORE("矩阵评分题"),
	TITLE("信息题"),
	NUMBER("数字题"),
	SELECT("下拉选择题"),
	CE("CE"),
	GPS("地理位置题"),
	TIMENOTES("反应时间记录题"),
	MEDIA("媒体题");
	
	private String name;
	private int index;
	private QuType(String name){
		this.name=name;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
}
