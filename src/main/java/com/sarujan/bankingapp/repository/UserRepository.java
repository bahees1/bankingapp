// These files talk directly with the db
package com.sarujan.bankingapp.repository;

import com.sarujan.bankingapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// JPARepository gives ready-made CRUD like, findAll(), findById(), save(),deletebyid()
// with these no need to write SQL queries, hibernate + JPA do it for us
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Optional custom queries later
    User findByUsername(String username);
}
