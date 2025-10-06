package com.vegas.sistema_gestion_operativa.branches.repository;

import com.vegas.sistema_gestion_operativa.branches.domain.Branch;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.function.Function;

@Repository
public class InMemoryBranchRepository implements IBranchRepository {


    @Override
    public void flush() {

    }

    @Override
    public <S extends Branch> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Branch> List<S> saveAllAndFlush(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public void deleteAllInBatch(Iterable<Branch> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<String> strings) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Branch getOne(String s) {
        return null;
    }

    @Override
    public Branch getById(String s) {
        return null;
    }

    @Override
    public Branch getReferenceById(String s) {
        return null;
    }

    @Override
    public <S extends Branch> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Branch> List<S> findAll(Example<S> example) {
        return List.of();
    }

    @Override
    public <S extends Branch> List<S> findAll(Example<S> example, Sort sort) {
        return List.of();
    }

    @Override
    public <S extends Branch> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Branch> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Branch> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Branch, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public <S extends Branch> S save(S entity) {
        return null;
    }

    @Override
    public <S extends Branch> List<S> saveAll(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public Optional<Branch> findById(String s) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(String s) {
        return false;
    }

    @Override
    public List<Branch> findAll() {
        return List.of();
    }

    @Override
    public List<Branch> findAllById(Iterable<String> strings) {
        return List.of();
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(String s) {

    }

    @Override
    public void delete(Branch entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends String> strings) {

    }

    @Override
    public void deleteAll(Iterable<? extends Branch> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<Branch> findAll(Sort sort) {
        return List.of();
    }

    @Override
    public Page<Branch> findAll(Pageable pageable) {
        return null;
    }
}