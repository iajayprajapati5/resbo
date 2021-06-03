package com.example.demo.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.example.demo.dto.AdvertiseDetailsDto;

public interface CustomBookAdvertiseRepository {
	List<Map<String, Object>> getFilteredAdvertise(String advertiseName);
	
	List<Map<String, Object>> getInterestedAdvertise(Long user_id);
	
	Optional<AdvertiseDetailsDto> findAdvertiseAndUserDetailsById(Long advertise_id, Long user_id);
}
