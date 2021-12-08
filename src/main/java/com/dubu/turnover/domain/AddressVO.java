package com.dubu.turnover.domain;

public class AddressVO {
	 private Long userId;
	    private String userName;		//瀹㈡埛鍚�
	    private String status;          //鐘舵��
	    private String addessId;        //鍦板潃id
	    private String mobile;          //鏀惰揣浜烘墜鏈�
	    private String telephone;       //鏀惰揣浜哄骇鏈�


	    private Integer countryId;      //鍥藉ID
	    private Integer provinceId;     //鐪両D
	    private Integer cityId;         //甯侷D
	    private Integer districtId;     //琛屾斂鍖哄幙ID
	    private String postcode;        //閭斂缂栫爜
	    private String receiveName;     //瀹㈡埛濮撳悕
		public Long getUserId() {
			return userId;
		}
		public void setUserId(Long userId) {
			this.userId = userId;
		}
		public String getUserName() {
			return userName;
		}
		public void setUserName(String userName) {
			this.userName = userName;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		public String getAddessId() {
			return addessId;
		}
		public void setAddessId(String addessId) {
			this.addessId = addessId;
		}
		public String getMobile() {
			return mobile;
		}
		public void setMobile(String mobile) {
			this.mobile = mobile;
		}
		public String getTelephone() {
			return telephone;
		}
		public void setTelephone(String telephone) {
			this.telephone = telephone;
		}
		public Integer getCountryId() {
			return countryId;
		}
		public void setCountryId(Integer countryId) {
			this.countryId = countryId;
		}
		public Integer getProvinceId() {
			return provinceId;
		}
		public void setProvinceId(Integer provinceId) {
			this.provinceId = provinceId;
		}
		public Integer getCityId() {
			return cityId;
		}
		public void setCityId(Integer cityId) {
			this.cityId = cityId;
		}
		public Integer getDistrictId() {
			return districtId;
		}
		public void setDistrictId(Integer districtId) {
			this.districtId = districtId;
		}
		public String getPostcode() {
			return postcode;
		}
		public void setPostcode(String postcode) {
			this.postcode = postcode;
		}
		public String getReceiveName() {
			return receiveName;
		}
		public void setReceiveName(String receiveName) {
			this.receiveName = receiveName;
		}
	    
}
