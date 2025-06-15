package com.scaffolding.infrastructure.statemachine.service;

import com.scaffolding.infrastructure.statemachine.config.OrderEvents;
import com.scaffolding.infrastructure.statemachine.config.OrderStates;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {
    private final StateMachineFactory<OrderStates, OrderEvents> factory;
    private final Map<Long, StateMachine<OrderStates, OrderEvents>> machines = new ConcurrentHashMap<>();

    // 创建订单
    public void createOrder(Long orderId) {
        // 为每个订单创建独立的状态机实例
        StateMachine<OrderStates, OrderEvents> sm = factory.getStateMachine();
        sm.start();
        machines.put(orderId, sm);
        System.out.println("订单已创建，ID: " + orderId);
        System.out.println("当前状态: " + sm.getState().getId());
    }

    // 处理订单事件
    public boolean handleEvent(Long orderId, OrderEvents event) {
        StateMachine<OrderStates, OrderEvents> sm = machines.get(orderId);
        if (sm == null) {
            System.err.println("订单不存在: " + orderId);
            return false;
        }

        return sm.sendEvent(event);
    }

    // 获取订单当前状态
    public OrderStates getOrderStatus(Long orderId) {
        StateMachine<OrderStates, OrderEvents> sm = machines.get(orderId);
        return sm != null ? sm.getState().getId() : null;
    }

    public static < E > void print(E [] array){
        for(E element : array){
            System.out.println(element);
        }
    }


    public static void main(String[] args) {
        String[] array = {"apple", "banana", "cherry"};
        print(array); // 调用泛型方法打印字符串数组

        Integer[] intArray = {1, 2, 3, 4, 5};
        print(intArray); // 调用泛型方法打印整数数组
    }
}