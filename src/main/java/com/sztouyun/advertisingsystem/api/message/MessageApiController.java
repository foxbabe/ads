package com.sztouyun.advertisingsystem.api.message;


import com.sztouyun.advertisingsystem.api.BaseApiController;
import com.sztouyun.advertisingsystem.common.InvokeResult;
import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.model.message.MessageReceiver;
import com.sztouyun.advertisingsystem.service.message.MessageService;
import com.sztouyun.advertisingsystem.utils.ApiBeanUtils;
import com.sztouyun.advertisingsystem.viewmodel.common.PageList;
import com.sztouyun.advertisingsystem.viewmodel.message.MessagePageInfoPackage;
import com.sztouyun.advertisingsystem.viewmodel.message.MessagePageInfoViewModel;
import com.sztouyun.advertisingsystem.viewmodel.message.MessageStatisticViewModel;
import com.sztouyun.advertisingsystem.viewmodel.message.MessageViewModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Api(value = "消息通知接口")
@RestController
@RequestMapping("/api/message")
public class MessageApiController extends BaseApiController {
    @Autowired
    private MessageService messageService;

    @ApiOperation(value = "获取消息列表", notes = "创建人: 王伟权")
    @PostMapping(value = "")
    public InvokeResult<MessagePageInfoPackage> getMessagePageInfoItems(@Validated @RequestBody MessagePageInfoViewModel viewModel, BindingResult result) {
        if(result.hasErrors())
            return ValidateFailResult(result);

        Page<MessageReceiver> pages = messageService.getMessageList(viewModel);
        PageList<? extends MessageViewModel> resultList = ApiBeanUtils.convertToPageList(pages, messageReceiver -> {
            MessageViewModel messageViewModel = messageService.getMessageViewModel(messageReceiver.getMessage());
            messageViewModel.setHasRead(messageReceiver.isHasRead());
            messageViewModel.setId(messageReceiver.getMessageId());
            return messageViewModel;
        });
        MessagePageInfoPackage messagePageInfoPackage = new MessagePageInfoPackage();
        messagePageInfoPackage.setPageInfoItemPageList(resultList);
        messagePageInfoPackage.setHasUnReadMessage(messageService.hasUnReadMessage(viewModel));
        return InvokeResult.SuccessResult(messagePageInfoPackage);
    }

    @ApiOperation(value = "获取消息统计信息", notes = "创建人: 王伟权")
    @GetMapping(value = "/getMessageStatistic")
    public InvokeResult<MessageStatisticViewModel> getMessageStatistic() {
        return InvokeResult.SuccessResult(messageService.getMessageStatistic());
    }

    @ApiOperation(value = "阅读单个消息", notes = "创建人: 文丰")
    @PostMapping(value = "readOne/{messageId}")
    public InvokeResult readOne(@PathVariable("messageId") String messageId) {
        if (StringUtils.isEmpty(messageId))
            throw new BusinessException("消息ID不能为空");
        messageService.readMessage(messageId);
        return InvokeResult.SuccessResult();
    }

    @ApiOperation(value = "批量标记消息为已读", notes = "创建人: 文丰")
    @PostMapping(value = "readBatches")
    public InvokeResult readBatches(@Validated @RequestBody MessagePageInfoViewModel request, BindingResult result) {
        if (result.hasErrors())
            return ValidateFailResult(result);
        messageService.readMessages(request);
        return InvokeResult.SuccessResult();
    }
}
