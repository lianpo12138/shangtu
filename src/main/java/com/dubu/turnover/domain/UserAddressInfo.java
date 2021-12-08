package com.dubu.turnover.domain;

public class UserAddressInfo {
	private String receiver; // 鏀惰揣浜哄悕绉�
	private String trueName; // 瀹㈡埛濮撳悕
	private String mobile; // 鏀惰揣浜烘墜鏈�
	private String zone; // 搴ф満zone
	private String telephone; // 鏀惰揣浜哄骇鏈�
	private String ext; // 搴ф満ext
	private String email; // 閭欢email
	private Long countryId; // 鍥藉ID
	private Long provinceId; // 鐪両D
	private Long cityId; // 甯侷D
	private Long districtId; // 琛屾斂鍖哄幙ID
	private String address; // 鍦板潃淇℃伅
	private String postcode; // 閭斂缂栫爜
	private Long addressId; // 鍦板潃ID
	private Integer isDefault; // 鏄惁榛樿0鍚�1鏄�
	private Long userId; // 鐢ㄦ埛ID
	private String nickname; // 鍦板潃绠�绉�
	private Integer status; // 鐘舵��(0:瀹℃牳涓� 1:閫氳繃)
	private String type; // 0:鍧囦笉鏄紝1:榛樿鍙戣揣鍦板潃, 2:榛樿閫�璐у湴鍧� , 3:榛樿鍦板潃

	private String countryName; // 鍥藉鍚嶇О
	private String countryNameEn; // 鍥藉鑻辨枃鍚嶇О
	private String countryNameTw; // 鍙版咕鍚嶇О
	private String provinceName; // 鐪佸悕绉�
	private String provinceNameEn; // 鐪佽嫳鏂囧悕绉�
	private String provinceNameTw; // 鐪佸彴婀惧悕绉�
	private String cityNameEn; // 鍙版咕鑻辨枃鍚嶇О
	private String cityNameTw; // 鍩庡競鍙版咕鍚嶇О
	private String cityName; // 鍩庡競鍚嶇О
	private Long mappingProId;
	private Long mappingCityId;
	private Long mappingCountryId;
	private String firstName;
	private String lastName;

	private String region; // 1 涓浗 2 娓境鍙� 3 鍥藉

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getTrueName() {
		return trueName;
	}

	public void setTrueName(String trueName) {
		this.trueName = trueName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getExt() {
		return ext;
	}

	public void setExt(String ext) {
		this.ext = ext;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Long getCountryId() {
		return countryId;
	}

	public void setCountryId(Long countryId) {
		this.countryId = countryId;
	}

	public Long getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(Long provinceId) {
		this.provinceId = provinceId;
	}

	public Long getCityId() {
		return cityId;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}

	public Long getDistrictId() {
		return districtId;
	}

	public void setDistrictId(Long districtId) {
		this.districtId = districtId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public Long getAddressId() {
		return addressId;
	}

	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}

	public Integer getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Integer isDefault) {
		this.isDefault = isDefault;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getCountryNameEn() {
		return countryNameEn;
	}

	public void setCountryNameEn(String countryNameEn) {
		this.countryNameEn = countryNameEn;
	}

	public String getCountryNameTw() {
		return countryNameTw;
	}

	public void setCountryNameTw(String countryNameTw) {
		this.countryNameTw = countryNameTw;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getProvinceNameEn() {
		return provinceNameEn;
	}

	public void setProvinceNameEn(String provinceNameEn) {
		this.provinceNameEn = provinceNameEn;
	}

	public String getProvinceNameTw() {
		return provinceNameTw;
	}

	public void setProvinceNameTw(String provinceNameTw) {
		this.provinceNameTw = provinceNameTw;
	}

	public String getCityNameEn() {
		return cityNameEn;
	}

	public void setCityNameEn(String cityNameEn) {
		this.cityNameEn = cityNameEn;
	}

	public String getCityNameTw() {
		return cityNameTw;
	}

	public void setCityNameTw(String cityNameTw) {
		this.cityNameTw = cityNameTw;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public Long getMappingProId() {
		return mappingProId;
	}

	public void setMappingProId(Long mappingProId) {
		this.mappingProId = mappingProId;
	}

	public Long getMappingCityId() {
		return mappingCityId;
	}

	public void setMappingCityId(Long mappingCityId) {
		this.mappingCityId = mappingCityId;
	}

	public Long getMappingCountryId() {
		return mappingCountryId;
	}

	public void setMappingCountryId(Long mappingCountryId) {
		this.mappingCountryId = mappingCountryId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}
	
}
