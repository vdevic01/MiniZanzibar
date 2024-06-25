package com.example.MiniZanzibarBack.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface BaseJPARepository<T> extends JpaRepository<T, Long> {
    @NonNull Optional<T> findById(@NonNull Long id);
    @NonNull List<T> findAllById(@NonNull Iterable<Long> ids);

    void deleteById(@NonNull Long id);
    void delete(@NonNull T entity);
    void deleteAllById(@NonNull Iterable<? extends Long> ids);
    void deleteAll(@NonNull Iterable<? extends T> entities);
}
