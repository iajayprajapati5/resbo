package com.example.demo.repository;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.ColumnResult;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.example.demo.dto.AdvertiseDetailsDto;

@Repository
public class BookAdvertiseRepositoryImpl implements CustomBookAdvertiseRepository{
	@Value("${images.endpoint}")
	private String imageGetPath;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public List<Map<String, Object>> getFilteredAdvertise(String searchKey) {
		List<String> searchStringList = new ArrayList<>();
		if(searchKey!=null && !searchKey.isEmpty()) searchStringList.add("ad.name ILIKE :searchKey");
		
		String condSearchString = (searchStringList.size() > 0 ? " WHERE " : "") + String.join(" AND ", searchStringList);
		
		String queryString = "select ad.id, ad.description, ad.genre, ad.image_name, ad.name, ad.price, ad.user_id, concat(u.firstname, ' ', u.lastname) AS full_name from book_advertise ad JOIN users u ON ad.user_id = u.id" + condSearchString;
		
		Query q = entityManager.createNativeQuery(queryString);
		
		if(searchKey!=null && !searchKey.isEmpty()) q.setParameter("searchKey", "%"+searchKey+"%");
		
		List<Object[]> filteredData = q.getResultList();
		List<Map<String, Object>> result = new ArrayList<>();
		
		for(Object[] record : filteredData) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", ((BigInteger) record[0]).longValue());
			map.put("description", (String)record[1]);
			map.put("genre", (String)record[2]);
			map.put("image_name", imageGetPath + (String)record[3]);
			map.put("name", (String)record[4]);
			map.put("price", (Integer)record[5]);
			map.put("user_id", ((BigInteger) record[6]).longValue());
			map.put("full_name", (String)record[7]);
			result.add(map);
		}
		
		return result;
	}

	@Override
	public List<Map<String, Object>> getInterestedAdvertise(Long user_id) {
		String queryString = "SELECT ba.id, ba.name, ba.genre, ba.image_name, ba.price, ba.finalised, uia.user_id, uia.amount, uia.finalised as finalisedToMe FROM book_advertise ba INNER JOIN user_interest_advertise uia ON ba.id = uia.advertise_id WHERE ((ba.finalised = true AND uia.finalised = true) OR ba.finalised = false) AND uia.user_id = :user_id";
		
		Query q = entityManager.createNativeQuery(queryString);
		q.setParameter("user_id", user_id);
		
		List<Object[]> filteredData = q.getResultList();
		List<Map<String, Object>> result = new ArrayList<>();
		
		for(Object[] record : filteredData) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", ((BigInteger) record[0]).longValue());
			map.put("name", (String)record[1]);
			map.put("genre", (String)record[2]);
			map.put("image_name", imageGetPath + (String)record[3]);
			map.put("price", (Integer)record[4]);
			map.put("finalised", (Boolean)record[5]);
			map.put("user_id", ((BigInteger) record[6]).longValue());
			map.put("amount", (Integer)record[7]);
			map.put("finalisedToMe", (Boolean)record[8]);
			
			result.add(map);
		}
		
		return result;
	}

	@Override
	public Optional<AdvertiseDetailsDto> findAdvertiseAndUserDetailsById(Long advertise_id, Long user_id) {
		String query = "SELECT b.id, b.name, b.description, b.image_name as image, b.genre, b.price, b.user_id, CONCAT(u.firstname, ' ', u.lastname) AS user_fullname, u.pincode, b.finalised, COALESCE(to_char(b.created_at, 'MM-DD-YYYY HH24:MI:SS'), '') AS created_at, (SELECT count(*) FROM user_interest_advertise WHERE user_id = :user_id AND advertise_id = :advertise_id) AS user_interested FROM book_advertise b JOIN users u ON b.user_id = u.id WHERE b.id = :advertise_id";
 
		Query q = entityManager.createNativeQuery(query);
		q.setParameter("user_id", user_id);
		q.setParameter("advertise_id", advertise_id);
		
		List<Object[]> filteredData = q.getResultList();
		
		if(filteredData.size() == 0) return null;
		Object[] result = filteredData.get(0);
		
		AdvertiseDetailsDto advertiseDetails = new AdvertiseDetailsDto(
				((BigInteger) result[0]).longValue(),
				(String) result[1],
				(String) result[2],
				(String) result[3],
				(String) result[4],
				(int) result[5],
				((BigInteger) result[6]).longValue(),
				(String) result[7],
				(String) result[8],
				(boolean) result[9],
				(String) result[10],
				((BigInteger) result[11]).intValue()
			);
		return Optional.of(advertiseDetails);
	}

}
