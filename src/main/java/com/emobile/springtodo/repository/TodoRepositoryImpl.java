package com.emobile.springtodo.repository;

import com.emobile.springtodo.entity.TodoEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class TodoRepositoryImpl implements TodoRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void save(TodoEntity todo) {
        todo.setDescription(todo.getDescription() != null ? todo.getDescription() : "");
        todo.setCreatedAt(LocalDateTime.now());
        todo.setUpdatedAt(LocalDateTime.now());
        entityManager.persist(todo);
    }

    @Override
    public Optional<TodoEntity> findById(Long id) {
        return Optional.ofNullable(entityManager.find(TodoEntity.class, id));
    }

    @Override
    public Page<TodoEntity> findAll(Pageable pageable) {
        String hql = "SELECT t FROM TodoEntity t ORDER BY t.createdAt DESC";
        List<TodoEntity> content = entityManager.createQuery(hql, TodoEntity.class)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
        Long total = entityManager.createQuery("SELECT COUNT(t) FROM TodoEntity t", Long.class)
                                    .getSingleResult();

        return new PageImpl<>(content, pageable, total != null ? total : 0);
    }

    @Override
    public void update(TodoEntity entity) {
        entity.setUpdatedAt(LocalDateTime.now());
        entityManager.merge(entity);
    }

    @Override
    public void deleteById(Long id) {
        entityManager.remove(entityManager.find(TodoEntity.class, id));
    }

    @Override
    public boolean existsById(Long id) {
        Long count = entityManager
                .createQuery("SELECT COUNT(t) FROM TodoEntity t WHERE t.id = :id", Long.class)
                .setParameter("id", id)
                .getSingleResult();
        return count > 0;
    }
}
