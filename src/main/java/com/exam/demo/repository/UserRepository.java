package com.exam.demo.repository;

 import com.exam.demo.model.User;
 import org.springframework.data.jpa.repository.JpaRepository;
 import org.springframework.data.jpa.repository.Query;
 import org.springframework.data.repository.query.Param;
 import org.springframework.stereotype.Repository;

 import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	User findByEmail(String email);


    @Query("SELECT v FROM User v where v.email <> :email ")
    List<User> listusers(@Param("email") int email);

}
