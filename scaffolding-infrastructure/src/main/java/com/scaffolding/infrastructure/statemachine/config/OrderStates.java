package com.scaffolding.infrastructure.statemachine.config;

/**
 * 定义订单的所有可能状态
 * @author superxie
 */
public enum OrderStates {
    UNPAID,         // 待支付
    WAITING_RECEIVE, // 待收货
    DONE,           // 已完成
    CANCELLED       // 已取消
}

