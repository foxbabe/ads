package com.sztouyun.advertisingsystem.scheduled;

import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.mapper.OrderDailyStoreMonitorStaticMapper;
import com.sztouyun.advertisingsystem.model.job.ScheduledJob;
import com.sztouyun.advertisingsystem.model.order.OrderOperationEnum;
import com.sztouyun.advertisingsystem.model.order.OrderOperationLog;
import com.sztouyun.advertisingsystem.model.order.OrderStatusEnum;
import com.sztouyun.advertisingsystem.repository.job.ScheduledJobRepository;
import com.sztouyun.advertisingsystem.repository.order.OrderInfoRepository;
import com.sztouyun.advertisingsystem.service.account.AuthenticationService;
import com.sztouyun.advertisingsystem.service.order.OrderOperationService;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

//@Component
public class OrderOperationScheduledTask extends BaseScheduledTask{

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ScheduledJobRepository scheduledJobRepository;
    @Autowired
    private OrderInfoRepository orderInfoRepository;
    @Autowired
    private OrderOperationService orderOperationService;
    @Autowired
    private OrderDailyStoreMonitorStaticMapper orderDailyStoreMonitorStaticMapper;

    @Scheduled(cron = "${update.monitor.jobs.cron.minute}")
    @Transactional
    public void updateOrderOperationMonitor() {
        AuthenticationService.setAdminLogin();

        List<String> orderIdToFinishList=orderInfoRepository.findAllOrderToFinish(OrderStatusEnum.Delivering.getValue(),new LocalDate().toDateTimeAtStartOfDay().toDate());
        autoOperationOrder(orderIdToFinishList, OrderOperationEnum.Finish,"订单投放到期，自动完成");

        List<String> orderIdToDeliveryList=orderInfoRepository.findAllOrderToDelivery(OrderStatusEnum.PendingDelivery.getValue(),new LocalDate().plusDays(1).toDateTimeAtStartOfDay().toDate());
        autoOperationOrder(orderIdToDeliveryList, OrderOperationEnum.AutoDelivery,"");
    }

    @Scheduled(cron = "${order.status.jobs.cron.minute}")
    @Transactional
    public void autoCancelOrder() {
        AuthenticationService.setAdminLogin();
        //订单处于待上刊状态，超过24小时，广告系统自动取消订单，订单状态从待上刊变更为已取消
        List<String> orderIdToCancelList=orderInfoRepository.findAllOrderToCancel(OrderStatusEnum.PendingPublishing.getValue(),new LocalDate().toDateTimeAtCurrentTime().minusHours(Constant.TIME_INTERVAL).toDate());
        autoOperationOrder(orderIdToCancelList, OrderOperationEnum.Cancel,"订单24小时内未发起上刊审核操作，订单自动取消");
    }

    private void autoOperationOrder(List<String> list ,OrderOperationEnum orderOperationEnum,String remarkLog) {
        String operationName = orderOperationEnum.getDisplayName();

        logger.info("执行完成Job："+ operationName +"，暂无数据！");
        if(list.isEmpty())
            return;

        ScheduledJob scheduledJob=new ScheduledJob(operationName);
        StringBuffer remark=new StringBuffer();
        scheduledJob.setTaskSize(list.size());
        list.parallelStream().forEach(id -> {
            try {
                orderOperationService.operate(getOrderOperationLog(id,orderOperationEnum,remarkLog));
            }catch (Exception ex){
                scheduledJob.setFailedTaskSize(scheduledJob.getFailedTaskSize()+1);
                remark.append(id+" failed;");
                logger.error(ex.getMessage());
            }
        });
        remark.append("系统更新"+ operationName +"成功;");
        scheduledJob.setRemark(remark.toString());
        scheduledJobRepository.save(scheduledJob);
    }

    private OrderOperationLog getOrderOperationLog(String id,OrderOperationEnum orderOperationEnum,String remarkLog){
        OrderOperationLog orderOperationLog = new OrderOperationLog();
        orderOperationLog.setOperation(orderOperationEnum.getValue());
        orderOperationLog.setOrderId(id);
        orderOperationLog.setRemark(remarkLog);
        return orderOperationLog;
    }

    @Scheduled(cron = "${update.order.daily.monitor.jobs.cron.minute}")
    public void initPartnerOrderDailyDeliveryInfo(){
        Date date= new LocalDate().toDateTimeAtStartOfDay().toDate();
        orderDailyStoreMonitorStaticMapper.updateOrderDailyAvailableStoreMonitorStatic(date);
    }
}
