package com.dubu.turnover.domain.enums;


public interface AuctionEnum {
	 /***
	  * 0只竞拍1只一口价2同时竞拍一口价
	  */
	 enum AuctionType { BIDDING,BUY,BIDBUY}
	 
	 /***
	  * 0竞拍支付买入1一口价买入2申请发货3申请转卖4一口价已转出5竞拍已转出6取消转卖7到期8收货9流拍10不支付11委托直接进仓库12退拍
	  */
	 enum StoreBusinessType { BIDDING,BUY,SHIPMENT,SALE,BUYED,BIDED,CANCEL,EXPIRE,RECEIPTED,OVER,NOPAY,INSTORE,RETURN,SALENOPAY}
	 
	 /***
	  * 0待上挂1竞拍中2已成交3已流拍4已撤销'5预展中6待预展7委托直接进仓库8退拍
	  */
	 enum AuctionStatus{DRAW,BIDDING,DEAL,OVER,CANCEL,PERVIEW,WAITPREIVW,INSTORE,RETURN}
	 
	 /**
	  * 0待导入1待制图2待整理检查3审核4待申请入库5待委托确认6待上传专场7完成8删除
	  *
	  */
	 enum ConractStatus{IMPORT,CAHRTING,SORTOUT,APPROVAL,RECIEPT,CONFIRM,UPLOAD,COMPLETE,DELETED}
	 
	 enum OrderStatus{WAITPAY,PAYMENTED,SETTLEMENTED,NOPAY}
	 
	 /**
	  * 默认结标时间间隔40秒
	  */
	 final static int DEFAULT_INTERVAL = 40;
	 
	/***
	 * 费用科目：0成交委托服务费1成交保险费2流拍保险费3流拍委托服务费4买家服务费5提货费
	 */
	enum AuctionFee {
		DEAL_SERVICE_FEE, INSURE_FEE, OVER_INSURE_FEE,OVER_DEAL_SERVIEC_FEE, BUY_FEE,PICK_UP_FEE
	}
	/**
	 * 0优品场1转卖常规场2寄售3支付4首次常规场
	 * @author 21902141155
	 *
	 */
	enum FeeSubjectType{
		PRIORITY,CONVERNTIONAL,SALE,PAYMENT,FISTCONVERNTIONAL
	}
}
