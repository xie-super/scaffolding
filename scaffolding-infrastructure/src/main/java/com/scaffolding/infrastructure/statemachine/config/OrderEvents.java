package com.scaffolding.infrastructure.statemachine.config;

/**
 * 定义触发状态转换的事件
 */
public enum OrderEvents {
    PAY,            // 支付
    RECEIVE,        // 确认收货
    CANCEL          // 取消订单
}
