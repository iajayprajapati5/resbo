package com.example.demo.repository;

import java.util.List;
import java.util.Map;

public interface CustomBookAdvertiseRepository {
	List<Map<String, Object>> getFilteredAdvertise(String advertiseName);
	
	List<Map<String, Object>> getInterestedAdvertise(Long user_id);
}
