package com.zhuzhe.integrationmqtt.entity;

// 实体类：手表
public record Watch(int id, String sn, String mac, boolean online, boolean active, Network network){}
