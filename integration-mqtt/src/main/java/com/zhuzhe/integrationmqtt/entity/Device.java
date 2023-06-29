package com.zhuzhe.integrationmqtt.entity;

public record Device(int id, long sn, String mac, boolean online, boolean active){}
