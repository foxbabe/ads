package com.sztouyun.advertisingsystem.model.contract;

import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.utils.DateUtils;
import com.sztouyun.advertisingsystem.utils.NumberFormatUtil;
import com.sztouyun.advertisingsystem.utils.NumberToCNUtil;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ContractForm {

    private String contractId;

    /**
     * 合同编码
     */
    private String contractCode;

    /**
     * 甲方名称
     */
    private String firstPartyName;

    /**
     * 甲方责任联系人
     */
    private String firstPartyResponsibilityPerson;

    /**
     * 甲方合同接收地址
     */
    private String firstPartyContractReceiveAddress;

    /**
     * 甲方邮件
     */
    private String firstPartyEmail;

    /**
     * 甲方电话
     */
    private String firstPartyPhone;

    /**
     * 乙方名称
     */
    private String secondPartyName;

    /**
     * 乙方责任联系人
     */
    private String secondPartyResponsibilityPerson;

    /**
     * 乙方合同接收地址
     */
    private String secondPartyContractReceiveAddress;

    /**
     * 乙方邮件
     */
    private String secondPartyEmail;

    /**
     * 乙方电话
     */
    private String secondPartyPhone;

    /**
     * 媒体费用
     */
    private String mediumCost="";


    /**
     * 制作费用
     */
    private String productCost="";

    /**
     * 总费用
     */
    private String totalCost="";

    /**
     * 大写总费用
     */
    private String totalCostUpper;

    /**
     * 前述费用支付日期
     */
    private String signAfterDay;

    /**
     * 银行账户名称
     */
    private String bankAccountName;

    /**
     * 银行账户号码
     */
    private String bankAccountNumber;

    /**
     * 银行名称
     */
    private String bankName;

    /**
     * 合作周期开始时间
     */
    private Integer yearOfStart;
    private Integer monthOfStart;
    private Integer dayOfStart;

    /**
     * 合作周期截止时间
     */
    private Integer yearOfEnd;
    private Integer monthOfEnd;
    private Integer dayOfEnd;

    /**
     * 总月数
     */
    private String totalMonths;

    /**
     * 实际投放周期
     */
    private String contractAdvertisementPeriod;

    /**
     * 备注
     */
    private String remark;

    /**
     * 播放时长
     */
    private String playDuration="";

    /**
     * 播放次数
     */
    private String playTime="";

    private String playsize;

    private Integer storeOfTypeA;

    private Integer storeOfTypeB;

    private Integer storeOfTypeC;

    private Integer storeOfTypeD;

    private Integer storeOfAll;

    /**
     * 合同模板ID
     */
    private String contractTemplateId;

    /**
     * 投放城市数
     */
    private Integer amountOfCities=0;

    /**
     * 所有投放城市
     */
    private String cities="";

    /**
     *投放网点最多的城市
     */
    private String topCity="";

    /**
     * 投放网点做多的城市的投放网点数
     */
    private Integer storeOfTopCity=0;

    /**
     * 附属协议
     */
    private String supplementary;

    public void setStoreCount(Integer storeOfTypeA,Integer storeOfTypeB,Integer storeOfTypeC,Integer storeOfTypeD){
        this.setStoreOfTypeA(storeOfTypeA);
        this.setStoreOfTypeB(storeOfTypeB);
        this.setStoreOfTypeC(storeOfTypeC);
        this.setStoreOfTypeD(storeOfTypeD);
        this.setStoreOfAll(storeOfTypeA+storeOfTypeB+storeOfTypeC+storeOfTypeD);
    }

    public Integer getStoreOfTypeA() {
        return storeOfTypeA;
    }

    public void setStoreOfTypeA(Integer storeOfTypeA) {
        this.storeOfTypeA = storeOfTypeA;
    }

    public Integer getStoreOfTypeB() {
        return storeOfTypeB;
    }

    public void setStoreOfTypeB(Integer storeOfTypeB) {
        this.storeOfTypeB = storeOfTypeB;
    }

    public Integer getStoreOfTypeC() {
        return storeOfTypeC;
    }

    public void setStoreOfTypeC(Integer storeOfTypeC) {
        this.storeOfTypeC = storeOfTypeC;
    }

    public Integer getStoreOfAll() {
        return storeOfAll;
    }

    public void setStoreOfAll(Integer storeOfAll) {
        this.storeOfAll = storeOfAll;
    }

    /**
     * 门店列表
     */
    private List<StoreForm> storeList;

    /**
     * 门店列表
     */
    private List<PlayInfo> playinfos=new ArrayList<>();

    public List<StoreForm> getStoreList() {
        return storeList;
    }

    public void setStoreList(List<StoreForm> storeList) {
        this.storeList = storeList;
    }


    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public String getPlayDuration() {
        return playDuration;
    }

    public void setPlayDuration(String playDuration) {
        this.playDuration = playDuration;
    }

    public String getPlaysize() {
        return playsize;
    }

    public void setPlaysize(double length,double width) {
        this.playsize = length+"*"+width;
    }

    public void setPlaysize(String size) {
        this.playsize = size;
    }

    public String getPlayTime() {
        return playTime;
    }

    public void setPlayTime(String playTime) {
        this.playTime = playTime;
    }

    public String getFirstPartyName() {
        return firstPartyName;
    }

    public void setFirstPartyName(String firstPartyName) {
        this.firstPartyName = firstPartyName;
    }

    public String getFirstPartyResponsibilityPerson() {
        return firstPartyResponsibilityPerson;
    }

    public void setFirstPartyResponsibilityPerson(String firstPartyResponsibilityPerson) {
        this.firstPartyResponsibilityPerson = firstPartyResponsibilityPerson;
    }

    public String getFirstPartyContractReceiveAddress() {
        return firstPartyContractReceiveAddress;
    }

    public void setFirstPartyContractReceiveAddress(String firstPartyContractReceiveAddress) {
        this.firstPartyContractReceiveAddress = firstPartyContractReceiveAddress;
    }

    public String getFirstPartyEmail() {
        return firstPartyEmail;
    }

    public void setFirstPartyEmail(String firstPartyEmail) {
        this.firstPartyEmail = firstPartyEmail;
    }

    public String getFirstPartyPhone() {
        return firstPartyPhone;
    }

    public void setFirstPartyPhone(String firstPartyPhone) {
        this.firstPartyPhone = firstPartyPhone;
    }

    public String getSecondPartyName() {
        return secondPartyName;
    }

    public void setSecondPartyName(String secondPartyName) {
        this.secondPartyName = secondPartyName;
    }

    public String getSecondPartyResponsibilityPerson() {
        return secondPartyResponsibilityPerson;
    }

    public void setSecondPartyResponsibilityPerson(String secondPartyResponsibilityPerson) {
        this.secondPartyResponsibilityPerson = secondPartyResponsibilityPerson;
    }

    public String getSecondPartyContractReceiveAddress() {
        return secondPartyContractReceiveAddress;
    }

    public void setSecondPartyContractReceiveAddress(String secondPartyContractReceiveAddress) {
        this.secondPartyContractReceiveAddress = secondPartyContractReceiveAddress;
    }

    public String getSecondPartyEmail() {
        return secondPartyEmail;
    }

    public void setSecondPartyEmail(String secondPartyEmail) {
        this.secondPartyEmail = secondPartyEmail;
    }

    public String getSecondPartyPhone() {
        return secondPartyPhone;
    }

    public void setSecondPartyPhone(String secondPartyPhone) {
        this.secondPartyPhone = secondPartyPhone;
    }

    public String getMediumCost() {
        return mediumCost;
    }

    public void setMediumCost(String mediumCost) {
        this.mediumCost = mediumCost;
    }

    public String getProductCost() {
        return productCost;
    }

    public void setProductCost(String productCost) {
        this.productCost = productCost;
    }

    public String getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(String totalCost) {
        this.totalCost = totalCost;
    }

    public String getSignAfterDay() {
        return signAfterDay;
    }

    public void setSignAfterDay(String signAfterDay) {
        this.signAfterDay = signAfterDay;
    }

    public String getBankAccountName() {
        return bankAccountName;
    }

    public void setBankAccountName(String bankAccountName) {
        this.bankAccountName = bankAccountName;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public Integer getYearOfStart() {
        return yearOfStart;
    }

    public void setYearOfStart(Integer yearOfStart) {
        this.yearOfStart = yearOfStart;
    }

    public Integer getMonthOfStart() {
        return monthOfStart;
    }

    public void setMonthOfStart(Integer monthOfStart) {
        this.monthOfStart = monthOfStart;
    }

    public Integer getDayOfStart() {
        return dayOfStart;
    }

    public void setDayOfStart(Integer dayOfStart) {
        this.dayOfStart = dayOfStart;
    }

    public Integer getYearOfEnd() {
        return yearOfEnd;
    }

    public void setYearOfEnd(Integer yearOfEnd) {
        this.yearOfEnd = yearOfEnd;
    }

    public Integer getMonthOfEnd() {
        return monthOfEnd;
    }

    public void setMonthOfEnd(Integer monthOfEnd) {
        this.monthOfEnd = monthOfEnd;
    }

    public Integer getDayOfEnd() {
        return dayOfEnd;
    }

    public void setDayOfEnd(Integer dayOfEnd) {
        this.dayOfEnd = dayOfEnd;
    }

    public String getTotalMonths() {
        return totalMonths;
    }

    public void setTotalMonths(String totalMonths) {
        this.totalMonths = totalMonths;
    }

    public String getRemark() {
        return StringUtils.isEmpty(remark)?"":remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public void setDateOfStart(Date dateOfStart){
        Calendar start= DateUtils.dateToCalendar(dateOfStart);
        this.setYearOfStart(start.get(Calendar.YEAR));
        this.setMonthOfStart(start.get(Calendar.MONTH)+1);
        this.setDayOfStart(start.get(Calendar.DAY_OF_MONTH));
    }
    public void setDateOfEnd(Date dateOfEnd){
        Calendar end= DateUtils.dateToCalendar(dateOfEnd);
        this.setYearOfEnd(end.get(Calendar.YEAR));
        this.setMonthOfEnd(end.get(Calendar.MONTH)+1);
        this.setDayOfEnd(end.get(Calendar.DAY_OF_MONTH));
    }

    public void setCost(double mediumCost,double productCost){
        BigDecimal medCost=new BigDecimal(mediumCost);
        BigDecimal prtCost=new BigDecimal(productCost);
        BigDecimal totalCost=NumberFormatUtil.format(medCost.add(prtCost));
        this.setMediumCost(NumberFormatUtil.formatToString(medCost));
        this.setProductCost(NumberFormatUtil.formatToString(prtCost));
        this.setTotalCost(totalCost.toString());
        this.setTotalCostUpper(NumberToCNUtil.number2CNMontrayUnit(totalCost));
    }

    public String getTotalCostUpper() {
        return totalCostUpper;
    }

    public void setTotalCostUpper(String totalCostUpper) {
        this.totalCostUpper = totalCostUpper;
    }

    public String getContractTemplateId() {
        return contractTemplateId;
    }

    public void setContractTemplateId(String contractTemplateId) {
        this.contractTemplateId = contractTemplateId;
    }

    public Integer getAmountOfCities() {
        return amountOfCities;
    }

    public void setAmountOfCities(Integer amountOfCities) {
        this.amountOfCities = amountOfCities;
    }

    public String getCities() {
        return cities;
    }

    public void setCities(String cities) {
        this.cities = cities;
    }

    public String getTopCity() {
        return topCity;
    }

    public void setTopCity(String topCity) {
        this.topCity = topCity;
    }

    public Integer getStoreOfTopCity() {
        return storeOfTopCity;
    }

    public void setStoreOfTopCity(Integer storeOfTopCity) {
        this.storeOfTopCity = storeOfTopCity;
    }

    public String getSupplementary() {
        return StringUtils.isEmpty(supplementary)? Constant.EMPTY_SUPPLEMENTARY:supplementary;
    }

    public void setSupplementary(String supplementary) {
       this.supplementary=supplementary;
    }

    public List<PlayInfo> getPlayinfos() {
        return playinfos;
    }

    public void setPlayinfos(List<PlayInfo> playinfos) {
        this.playinfos = playinfos;
    }
    public void addPlayInfo(PlayInfo playInfo){
        this.playinfos.add(playInfo);
    }

    public Integer getStoreOfTypeD() {
        return storeOfTypeD;
    }

    public void setStoreOfTypeD(Integer storeOfTypeD) {
        this.storeOfTypeD = storeOfTypeD;
    }

    public String getContractAdvertisementPeriod() {
        return contractAdvertisementPeriod;
    }

    public void setContractAdvertisementPeriod(String contractAdvertisementPeriod) {
        this.contractAdvertisementPeriod = contractAdvertisementPeriod;
    }
}
