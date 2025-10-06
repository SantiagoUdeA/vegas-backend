package com.vegas.sistema_gestion_operativa.users.repository;

import com.vegas.sistema_gestion_operativa.branches.domain.Branch;
import com.vegas.sistema_gestion_operativa.roles.domain.Rol;
import com.vegas.sistema_gestion_operativa.users.domain.User;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository("inMemoryUserRepository")
public class InMemoryUserRepository implements IUserRepository {

    // Almacenamiento en memoria: id -> User
    private final Map<String, User> storage = new ConcurrentHashMap<>();

    public InMemoryUserRepository() {
        // Usuarios por defecto
        User u1 = new User();
        u1.setId("u1");
        u1.setEmail("juan.perez@example.com");
        u1.setGivenName("Juan");
        u1.setFamilyName("Pérez");
        u1.setIdType("CI");
        u1.setPhoneNumber("+5491112345678");
        Rol r1 = new Rol();
        r1.setNombre("ADMIN");
        u1.setRol(r1);
        Branch b1 = Branch.builder().id("b1").name("Sucursal Centro").build();
        u1.setBranch(b1);

        User u2 = new User();
        u2.setId("u2");
        u2.setEmail("maria.lopez@example.com");
        u2.setGivenName("María");
        u2.setFamilyName("López");
        u2.setIdType("CI");
        u2.setPhoneNumber("+5491198765432");
        Rol r2 = new Rol();
        r2.setNombre("USER");
        u2.setRol(r2);
        Branch b2 = Branch.builder().id("b2").name("Sucursal Norte").build();
        u2.setBranch(b2);

        storage.put(u1.getId(), u1);
        storage.put(u2.getId(), u2);
    }

    @Override
    public void flush() {
        // no-op para implementación en memoria
    }

    @Override
    public <S extends User> S saveAndFlush(S entity) {
        return save(entity);
    }

    @Override
    public <S extends User> List<S> saveAllAndFlush(Iterable<S> entities) {
        List<S> result = new ArrayList<>();
        for (S e : entities) {
            result.add(save(e));
        }
        return result;
    }

    @Override
    public void deleteAllInBatch(Iterable<User> entities) {
        for (User u : entities) {
            storage.remove(u.getId());
        }
    }

    @Override
    public void deleteAllByIdInBatch(Iterable<String> strings) {
        for (String id : strings) {
            storage.remove(id);
        }
    }

    @Override
    public void deleteAllInBatch() {
        storage.clear();
    }

    @Override
    public User getOne(String s) {
        return getById(s);
    }

    @Override
    public User getById(String s) {
        return storage.get(s);
    }

    @Override
    public User getReferenceById(String s) {
        return getById(s);
    }

    @Override
    public <S extends User> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends User> List<S> findAll(Example<S> example) {
        return List.of();
    }

    @Override
    public <S extends User> List<S> findAll(Example<S> example, Sort sort) {
        return List.of();
    }

    @Override
    public <S extends User> Page<S> findAll(Example<S> example, Pageable pageable) {
        return Page.empty();
    }

    @Override
    public <S extends User> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends User> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends User, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public <S extends User> S save(S entity) {
        if (entity.getId() == null || entity.getId().isBlank()) {
            // generar id simple
            String id = "u" + (storage.size() + 1);
            entity.setId(id);
        }
        storage.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public <S extends User> List<S> saveAll(Iterable<S> entities) {
        List<S> result = new ArrayList<>();
        for (S e : entities) {
            result.add(save(e));
        }
        return result;
    }

    @Override
    public Optional<User> findById(String s) {
        return Optional.ofNullable(storage.get(s));
    }

    @Override
    public boolean existsById(String s) {
        return storage.containsKey(s);
    }

    @Override
    public List<User> findAll() {
        return storage.values().stream().collect(Collectors.toList());
    }

    @Override
    public List<User> findAllById(Iterable<String> strings) {
        List<User> result = new ArrayList<>();
        for (String id : strings) {
            User u = storage.get(id);
            if (u != null) result.add(u);
        }
        return result;
    }

    @Override
    public long count() {
        return storage.size();
    }

    @Override
    public void deleteById(String s) {
        storage.remove(s);
    }

    @Override
    public void delete(User entity) {
        if (entity != null) storage.remove(entity.getId());
    }

    @Override
    public void deleteAllById(Iterable<? extends String> strings) {
        for (String id : strings) storage.remove(id);
    }

    @Override
    public void deleteAll(Iterable<? extends User> entities) {
        for (User u : entities) storage.remove(u.getId());
    }

    @Override
    public void deleteAll() {
        storage.clear();
    }

    @Override
    public List<User> findAll(Sort sort) {
        // ignorar sort en implementación simple
        return findAll();
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        List<User> all = findAll();
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), all.size());
        if (start > end) {
            return new PageImpl<>(Collections.emptyList(), pageable, all.size());
        }
        return new PageImpl<>(all.subList(start, end), pageable, all.size());
    }
}
