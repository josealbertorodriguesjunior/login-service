package com.juniorlima.login.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.juniorlima.login.model.LoginModel;

@Repository
public interface LoginRepository extends JpaRepository<LoginModel, Long>{
	Long countByEmail(String email);
	Long countByToken(String finalToken);
        LoginModel findUserById(Long id);
        LoginModel findByToken(String token);
        Long countByPassword(String password);
        Long countByEmailAndPassword(String email,String password);
}
