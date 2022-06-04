package com.tptogiar.network.nio.task;


import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 用于封装具体的任务及优先级
 * TODO 优先级
 *
 * @author Tptogiar
 * @description
 * @date 2022/5/30 - 13:35
 */
@AllArgsConstructor
@Data
public class EventTask {

    private Runnable runnable;

}
