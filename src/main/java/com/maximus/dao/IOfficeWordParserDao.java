package com.maximus.dao;

import java.util.List;

import com.maximus.bean.BeautyQuestion;

public interface IOfficeWordParserDao {

	public int insert(BeautyQuestion beautyQuestion);
	public int insertBatch(List<?> list);
	public int insertBatchBlank(List<?> list);
	public int delete();
	public int update();
	public List<?> select();
}
