package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.Entity.UserInterestAdvertiseEntity;
import com.example.demo.Entity.UserInterestKey;
import com.example.demo.dto.AdvertiseBidsListDto;

public interface UserInterestRepository extends JpaRepository<UserInterestAdvertiseEntity, UserInterestKey>{
	@Query(value = "SELECT count(*) from user_interest_advertise WHERE finalised = 't' AND advertise_id = :advertise_id", nativeQuery = true)
	Long getFinalisedCount(@Param("advertise_id")Long advertise_id);
	
	@Query(value = "SELECT b.user_id, CONCAT(u.firstname, ' ', u.lastname) as name, u.email, u.mobile, u.address, b.finalised, b.created_at from users u JOIN user_interest_advertise b ON u.id = b.user_id where advertise_id = :advertise_id order by b.finalised desc, b.created_at", nativeQuery = true)
	List<AdvertiseBidsListDto> findAllBidsByAdvertise(@Param("advertise_id") Long advertise_id);
}
