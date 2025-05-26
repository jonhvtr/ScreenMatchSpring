package com.jonhvtr.alura.spring_screenmatch.service;

public interface IConvertingData {
    <T> T getData(String json, Class<T> tClass);
}
