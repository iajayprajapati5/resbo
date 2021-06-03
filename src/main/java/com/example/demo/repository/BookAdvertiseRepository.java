package com.example.demo.repository;

import java.util.List;
import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.Entity.BookAdvertiseEntity;
import com.example.demo.dto.AdvertiseDetailsDto;

@Repository
public interface BookAdvertiseRepository extends JpaRepository<BookAdvertiseEntity, Long>, CustomBookAdvertiseRepository{
	
	List<BookAdvertiseEntity> findAllByUserId(Long user_id);
}
