package cn.com.jpa;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class AbstractService<T, ID> implements BaseService<T, ID> {

    @Autowired
    protected BaseRepository<T, ID> baseRepository;

    @Autowired
    protected JPAQueryFactory jpaQueryFactory;

    @Override
    @Transactional(readOnly = true, rollbackFor = Throwable.class)
    public List<T> findAll() {
        return this.baseRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Throwable.class)
    public List<T> findAll(Sort sort) {
        return this.baseRepository.findAll(sort);
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Throwable.class)
    public Page<T> findAll(Pageable pageable) {
        return this.baseRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Throwable.class)
    public List<T> findAllById(Iterable<ID> iterable) {
        return this.baseRepository.findAllById(iterable);
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Throwable.class)
    public long count() {
        return this.baseRepository.count();
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void deleteById(ID id) {
        this.baseRepository.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void delete(T t) {
        this.baseRepository.delete(t);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void deleteAll(Iterable<? extends T> iterable) {
        this.baseRepository.deleteAll(iterable);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void deleteAll() {
        this.baseRepository.deleteAll();
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public <S extends T> S save(S s) {
        return this.baseRepository.save(s);
    }

    @Override
    public <S extends T> List<S> saveAll(Iterable<S> iterable) {
        return this.baseRepository.saveAll(iterable);
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Throwable.class)
    public Optional<T> findById(ID id) {
        return this.baseRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Throwable.class)
    public boolean existsById(ID id) {
        return this.baseRepository.existsById(id);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void flush() {
        this.baseRepository.flush();
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public <S extends T> S saveAndFlush(S s) {
        return this.baseRepository.saveAndFlush(s);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void deleteInBatch(Iterable<T> iterable) {
        this.baseRepository.deleteInBatch(iterable);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void deleteAllInBatch() {
        this.baseRepository.deleteAllInBatch();
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Throwable.class)
    public T getOne(ID id) {
        return this.baseRepository.getOne(id);
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Throwable.class)
    public <S extends T> Optional<S> findOne(Example<S> example) {
        return this.baseRepository.findOne(example);
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Throwable.class)
    public <S extends T> List<S> findAll(Example<S> example) {
        return this.baseRepository.findAll(example);
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Throwable.class)
    public <S extends T> List<S> findAll(Example<S> example, Sort sort) {
        return this.baseRepository.findAll(example, sort);
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Throwable.class)
    public <S extends T> Page<S> findAll(Example<S> example, Pageable pageable) {
        return this.baseRepository.findAll(example, pageable);
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Throwable.class)
    public <S extends T> long count(Example<S> example) {
        return this.baseRepository.count(example);
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Throwable.class)
    public <S extends T> boolean exists(Example<S> example) {
        return this.baseRepository.exists(example);
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Throwable.class)
    public Optional<T> findOne(Specification<T> specification) {
        return this.baseRepository.findOne(specification);
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Throwable.class)
    public List<T> findAll(Specification<T> specification) {
        return this.baseRepository.findAll(specification);
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Throwable.class)
    public Page<T> findAll(Specification<T> specification, Pageable pageable) {
        return this.baseRepository.findAll(specification, pageable);
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Throwable.class)
    public List<T> findAll(Specification<T> specification, Sort sort) {
        return this.baseRepository.findAll(specification, sort);
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Throwable.class)
    public long count(Specification<T> specification) {
        return this.baseRepository.count(specification);
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Throwable.class)
    public Optional<T> findOne(Predicate predicate) {
        return this.baseRepository.findOne(predicate);
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Throwable.class)
    public Iterable<T> findAll(Predicate predicate) {
        return this.baseRepository.findAll(predicate);
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Throwable.class)
    public Iterable<T> findAll(Predicate predicate, Sort sort) {
        return this.baseRepository.findAll(predicate, sort);
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Throwable.class)
    public Iterable<T> findAll(Predicate predicate, OrderSpecifier<?>... orderSpecifiers) {
        return this.baseRepository.findAll(predicate, orderSpecifiers);
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Throwable.class)
    public Iterable<T> findAll(OrderSpecifier<?>... orderSpecifiers) {
        return null;
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Throwable.class)
    public Page<T> findAll(Predicate predicate, Pageable pageable) {
        return this.baseRepository.findAll(predicate, pageable);
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Throwable.class)
    public long count(Predicate predicate) {
        return this.baseRepository.count(predicate);
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Throwable.class)
    public boolean exists(Predicate predicate) {
        return this.baseRepository.exists(predicate);
    }

    @Transactional(rollbackFor = Throwable.class)
    public <R> R apply(Function<JPAQueryFactory, R> function) {
        return function.apply(this.jpaQueryFactory);
    }

    @Transactional(readOnly = true, rollbackFor = Throwable.class)
    public <R> R applyReadOnly(Function<JPAQueryFactory, R> function) {
        return function.apply(this.jpaQueryFactory);
    }
}
