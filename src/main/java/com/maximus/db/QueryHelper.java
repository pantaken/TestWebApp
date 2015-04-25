package com.maximus.db;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
/**
 * 
 * @author wxl
 * <h4>数据库查询助手</h4>
 */
@Transactional
public class QueryHelper {
	
	private JdbcTemplate jdbcTemplate;
	private SimpleJdbcTemplate simpleJdbcTemplate;
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	public void setSimpleJdbcTemplate(SimpleJdbcTemplate simpleJdbcTemplate) {
		this.simpleJdbcTemplate = simpleJdbcTemplate;
	}
	/**
	 * 单条更新
	 * @param sql
	 * @param args
	 * @return 受影响的记录数
	 */
	public int update(String sql, Object...args) {
		return simpleJdbcTemplate.update(sql, args);
	}
	/**
	 * 批量更新
	 * @param sql
	 * @param batchArgs
	 * @return 受影响的记录数
	 */
	public int batchUpdate(String sql, List<Object[]> batchArgs) {
		int[] counts = null;
		counts = simpleJdbcTemplate.batchUpdate(sql, batchArgs);
		return counts == null ? -1 : counts.length;
	}
	/**
	 * 
	 * @param sql
	 * @param args
	 * @return 记录数
	 */
	public int count(String sql, Object...args) {
		return simpleJdbcTemplate.queryForInt(sql, args);
	}
	/**
	 * 
	 * @param sql
	 * @param args
	 * @return Map类型的List集合
	 */
	public List<Map<String, Object>> query(String sql, Object...args) {
		return simpleJdbcTemplate.queryForList(sql, args);
	}
	/**
	 * 
	 * @param sql
	 * @param args
	 * @return JSONArray格式的数据
	 */
	public JSONArray jsonQuery(String sql, Object...args) {
		List<Map<String, Object>> list = query(sql, args);
		JSONArray j_array = new JSONArray();
		for(Map<String, Object> map : list) {
			Set<String> set = map.keySet();
			Iterator<String> it = set.iterator();
			JSONObject j_obj = new JSONObject();
			while(it.hasNext()) {
				String key = (String) it.next();
				Object value = map.get(key);
				j_obj.put(key, value);
			}
			j_array.add(j_obj);
		}
		return j_array;
	}
	@Transactional(rollbackFor = RuntimeException.class)
	public void transactionUpdate(String sql, Object...params) {
		jdbcTemplate.update(sql, params);
	}
}
