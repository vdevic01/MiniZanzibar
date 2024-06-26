package com.example.MiniZanzibarBack.service.implementation;

import com.example.MiniZanzibarBack.repository.BaseJPARepository;
import com.example.MiniZanzibarBack.service.IBaseService;

import java.util.List;
import java.util.Optional;

public abstract class BaseService<T> implements IBaseService<T> {
    protected abstract BaseJPARepository<T> getRepository();

    @Override
    public T save(T entity) {
        return getRepository().save(entity);
    }

    @Override
    public List<T> saveAll(List<T> entities) {
        return getRepository().saveAll(entities);
    }

    @Override
    public T findById(Long id) {
        Optional<T> optional = getRepository().findById(id);
        return optional.orElse(null);
    }

    @Override
    public List<T> findAll() {
        return getRepository().findAll();
    }

    @Override
    public void deleteById(Long id) {
        getRepository().deleteById(id);
    }
}

