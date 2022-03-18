package com.example.restfulservice.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> { // JpaRepository 상속 & <DataSet Type, PK Type>



}
