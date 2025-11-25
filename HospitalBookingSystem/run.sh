#!/bin/bash

echo "========================================"
echo "医院预约和挂号系统 - 启动脚本"
echo "========================================"
echo ""

echo "正在编译项目..."
mvn clean compile

if [ $? -ne 0 ]; then
    echo "编译失败！"
    exit 1
fi

echo ""
echo "正在启动服务器..."
mvn exec:java



