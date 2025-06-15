package com.scaffolding.infrastructure.statemachine.service;


import com.scaffolding.infrastructure.statemachine.config.OrderEvents;
import org.springframework.messaging.Message;
import org.springframework.statemachine.annotation.EventHeaders;
import org.springframework.statemachine.annotation.OnEventNotAccepted;
import org.springframework.statemachine.annotation.OnTransition;
import org.springframework.statemachine.annotation.WithStateMachine;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@WithStateMachine
public class OrderStateListener {

    /**
     * 进入UNPAID状态时的回调
     */
    @OnTransition(target = "UNPAID")
    public void enterUnpaidState() {
        System.out.println("进入待支付状态...");
        System.out.println("请尽快完成支付！");
    }

    /**
     * 从UNPAID到WAITING_RECEIVE状态转移
     */
    @OnTransition(source = "UNPAID", target = "WAITING_RECEIVE")
    public void handlePayment() {
        System.out.println("支付成功！");
        System.out.println("准备发货...");
    }

    /**
     * 进入WAITING_RECEIVE状态时的回调
     */
    @OnTransition(target = "WAITING_RECEIVE")
    public void enterWaitingReceiveState() {
        System.out.println("商品已发货！");
        System.out.println("等待买家收货...");
    }

    /**
     * 从WAITING_RECEIVE到DONE状态转移
     */
    @OnTransition(source = "WAITING_RECEIVE", target = "DONE")
    public void handleReceive() {
        System.out.println("买家已确认收货！");
        System.out.println("订单完成！");
    }

    /**
     * 进入DONE状态时的回调
     */
    @OnTransition(target = "DONE")
    public void enterDoneState() {
        System.out.println("订单流程全部完成");
        System.out.println("结束订单处理...");
    }

    /**
     * 取消订单状态转移（从UNPAID/WAITING_RECEIVE到CANCELLED）
     */
    @OnTransition(source = {"UNPAID", "WAITING_RECEIVE"}, target = "CANCELLED")
    public void handleCancel() {
        System.out.println("订单已被取消！");
        System.out.println("处理退款等后续操作...");
    }

    /**
     * 进入CANCELLED状态时的回调
     */
    @OnTransition(target = "CANCELLED")
    public void enterCancelledState() {
        System.out.println("订单已标记为取消状态");
        System.out.println("执行清理操作...");
    }

    /**
     * 任何状态转移前的回调
     */
    @OnTransition
    public void anyTransition(@EventHeaders Map<String, Object> headers) {
        OrderEvents event = (OrderEvents) headers.get("event");
        System.out.println("--- 即将触发: " + event + " ---");
    }

    /**
     * 事件未接受时回调
     */
    @OnEventNotAccepted
    public void eventNotAccepted(Message<OrderEvents> event) {
        System.out.println("无法处理: " + event.getPayload());
        System.out.println("当前状态下不允许此操作！");
    }
}
