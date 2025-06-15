package com.scaffolding.infrastructure.statemachine.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

import java.util.EnumSet;

/**
 * Spring State Machine 的配置类
 * @author superxie
 */
@Configuration
@EnableStateMachine
@EnableStateMachineFactory // 启用状态机工厂
public class StateMachineConfig extends StateMachineConfigurerAdapter<OrderStates, OrderEvents> {

    @Override
    public void configure(StateMachineStateConfigurer<OrderStates, OrderEvents> states)
            throws Exception {
        states
                .withStates()
                .initial(OrderStates.UNPAID) // 初始状态
                .states(EnumSet.allOf(OrderStates.class)); // 所有状态
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<OrderStates, OrderEvents> transitions)
            throws Exception {
        transitions
                // UNPAID → PAY → WAITING_RECEIVE
                .withExternal()
                .source(OrderStates.UNPAID)
                .target(OrderStates.WAITING_RECEIVE)
                .event(OrderEvents.PAY)
                .and()

                // WAITING_RECEIVE → RECEIVE → DONE
                .withExternal()
                .source(OrderStates.WAITING_RECEIVE)
                .target(OrderStates.DONE)
                .event(OrderEvents.RECEIVE)
                .and()

                // UNPAID → CANCEL → CANCELLED
                .withExternal()
                .source(OrderStates.UNPAID)
                .target(OrderStates.CANCELLED)
                .event(OrderEvents.CANCEL)
                .and()

                // WAITING_RECEIVE → CANCEL → CANCELLED
                .withExternal()
                .source(OrderStates.WAITING_RECEIVE)
                .target(OrderStates.CANCELLED)
                .event(OrderEvents.CANCEL);
    }

    @Override
    public void configure(StateMachineConfigurationConfigurer<OrderStates, OrderEvents> config)
            throws Exception {
        config
                .withConfiguration()
                .autoStartup(true) // 自动启动
                .listener(stateMachineListener()); // 添加全局监听器
    }

    // 5. 全局状态机监听器
    @Bean
    public StateMachineListener<OrderStates, OrderEvents> stateMachineListener() {
        return new StateMachineListenerAdapter<OrderStates, OrderEvents>() {
            @Override
            public void stateChanged(State<OrderStates, OrderEvents> from, State<OrderStates, OrderEvents> to) {
                String fromState = from != null ? from.getId().name() : "初始";
                System.out.println("[全局监听] 状态变更: " + fromState + " → " + to.getId());
            }

            @Override
            public void stateMachineStarted(StateMachine<OrderStates, OrderEvents> stateMachine) {
                System.out.println("[全局监听] 状态机启动成功");
            }
        };
    }
}