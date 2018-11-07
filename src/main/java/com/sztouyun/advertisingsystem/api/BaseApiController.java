package com.sztouyun.advertisingsystem.api;

import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.common.EnumMessage;
import com.sztouyun.advertisingsystem.common.InvokeResult;
import com.sztouyun.advertisingsystem.controller.BaseController;
import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.model.system.Area;
import com.sztouyun.advertisingsystem.model.system.TerminalTypeEnum;
import com.sztouyun.advertisingsystem.service.account.UserService;
import com.sztouyun.advertisingsystem.service.customer.CustomerService;
import com.sztouyun.advertisingsystem.service.partner.CooperationPartnerService;
import com.sztouyun.advertisingsystem.service.system.AreaService;
import com.sztouyun.advertisingsystem.service.system.AuditReasonConfigService;
import com.sztouyun.advertisingsystem.service.system.DataDictService;
import com.sztouyun.advertisingsystem.utils.ApiBeanUtils;
import com.sztouyun.advertisingsystem.utils.EnumUtils;
import com.sztouyun.advertisingsystem.utils.excel.MyWorkbook;
import com.sztouyun.advertisingsystem.viewmodel.common.MyPageRequest;
import com.sztouyun.advertisingsystem.viewmodel.common.PageList;
import lombok.experimental.var;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BaseApiController extends BaseController {
    @Autowired
    protected UserService userService;

    @Autowired
    protected AreaService areaService;

    @Autowired
    protected CustomerService customerService;

    @Autowired
    protected CooperationPartnerService cooperationPartnerService;

    @Autowired
    protected AuditReasonConfigService auditReasonConfigService;

    @Autowired
    protected DataDictService dataDictService;

    @Value(value = "${advertisement.upload.excel.size}")
    private Long excelLimitSize;

    protected InvokeResult ValidateFailResult(BindingResult result) {
        FieldError fieldError = result.getFieldErrors().get(0);
        return InvokeResult.Fail(fieldError.isBindingFailure() ? fieldError.getField() + "参数类型错误！" : fieldError.getDefaultMessage(), 100);
    }

    protected String getUserNickname(String userId) {
        if (StringUtils.isEmpty(userId))
            return "";
        return userService.getNicknameFromCache(userId);
    }

    protected String getAreaName(String areaId) {
        if (StringUtils.isEmpty(areaId))
            return "";
        return areaService.getAreaNameFromCache(areaId);
    }

    protected String getAreaCode(String areaId) {
        if (StringUtils.isEmpty(areaId))
            return "";
         Area area=areaService.getAreaFromCache(areaId);
        return area==null?"":area.getCode();
    }

    protected String getAreaName(String areaId, Map<String,String> areaMap) {
        if (StringUtils.isEmpty(areaId) || !areaMap.keySet().contains(areaId))
            return "";
        return areaMap.get(areaId);
    }

    protected String getCustomerName(String customerId) {
        if (StringUtils.isEmpty(customerId))
            return "";
        return customerService.getCustomerNameFromCache(customerId);
    }

    protected String getPartnerName(String partnerId) {
        if (StringUtils.isEmpty(partnerId))
            return "";
        return cooperationPartnerService.getPartnerNameFromCache(partnerId);
    }

    protected String getAuditReasonFromCache(String auditReasonId) {
        if (StringUtils.isEmpty(auditReasonId))
            return "";
        return auditReasonConfigService.getAuditReasonFromCache(auditReasonId);
    }

    protected String getDataDictName(String dataDictKey){
        if (StringUtils.isEmpty(dataDictKey))
            return "";
        var dataDict = dataDictService.getDataDictFromCache(dataDictKey);
        if(dataDict ==null)
            return "";
        return dataDict.getName();
    }

    protected void outputStream(String filename, HttpServletResponse response, Workbook wb)  {
        OutputStream outputStream = null;
        try {
            filename = new String(filename.getBytes(), "iso-8859-1");
            response.setContentType("application/octet-stream;charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename="+filename);
            outputStream = response.getOutputStream();
            wb.write(outputStream);
        } catch (IOException e) {
           throw new BusinessException("导出失败");
        }finally {
            try {
                outputStream.flush();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    protected void outputStream(String filename, HttpServletResponse response, MyWorkbook wb){
        outputStream(filename,response,wb.getWb());
    }

    protected Boolean canView(List<String> userIds) {
        return userService.getAccessedUserIds().containsAll(userIds);
    }

    protected String getPlatformName(String platform){
        if(ALL_TERMINATE_TYPE.equals(platform))
            return Constant.ALL_PLATFORM;
        return org.apache.commons.lang3.StringUtils.replaceEach(platform,PLATFORM_KEYS,PLATFORM_VALUES);
    }

    private static String ALL_TERMINATE_TYPE="1\\2\\3";

    private static  String[] PLATFORM_KEYS=new String[]{
            TerminalTypeEnum.CashRegister.getValue().toString(),
            TerminalTypeEnum.IOS.getValue().toString(),
            TerminalTypeEnum.Android.getValue().toString()
    };

    private static  String[] PLATFORM_VALUES=new String[]{
            TerminalTypeEnum.CashRegister.getDisplayName(),
            TerminalTypeEnum.IOS.getDisplayName(),
            TerminalTypeEnum.Android.getDisplayName()
    };


    protected <T extends EnumMessage> String getJointName(List<Integer> jointList, Class<T> e) {
        if(jointList==null||jointList.size()==0){
            return "";
        }
        StringBuffer s = new StringBuffer();
        jointList.forEach(a->s.append(a).append(Constant.SEPARATOR_COMMA));
        List<T> allItems = EnumUtils.getAllItems(e);
        List<String> JOINTNAME_KEYS=new ArrayList<>();
        List<String> JOINTNAME_VALUES=new ArrayList<>();
        allItems.forEach(q->{
            JOINTNAME_KEYS.add(q.getValue().toString());
            JOINTNAME_VALUES.add(q.getDisplayName());
        });
        return  org.apache.commons.lang3.StringUtils.replaceEach(s.toString().substring(0,s.length()-1),JOINTNAME_KEYS.toArray(new String[allItems.size()]),JOINTNAME_VALUES.toArray(new String[allItems.size()]));
    }

    protected  <T> Page<T> pageResult(List<T> resultList, Pageable pageable, long total){
        return  new PageImpl<>(resultList, pageable, total);
    }

    protected <T> InvokeResult<PageList<T>> emptyInvokeResult(Integer pageSize){
        return  InvokeResult.SuccessResult(ApiBeanUtils.convertToPageList(pageResult(new ArrayList(),new MyPageRequest(0,pageSize),0L)));
    }

    protected void validateFile(MultipartFile file){
        String fileName=file.getOriginalFilename();
        if(!fileName.endsWith("xls") && !fileName.endsWith("xlsx"))
            throw new BusinessException("格式不支持");
        if(file.getSize()>excelLimitSize)
            throw new BusinessException("文件大小超过限制");
    }
}
