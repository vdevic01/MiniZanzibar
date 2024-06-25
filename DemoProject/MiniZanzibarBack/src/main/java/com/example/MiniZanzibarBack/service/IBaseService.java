package com.example.MiniZanzibarBack.service;

import java.util.List;

public interface IBaseService<T> {
    T save(T entity);
    List<T> saveAll(List<T> entities);
    T findById(Long id);
    List<T> findAll();
    void deleteById(Long id);
}
