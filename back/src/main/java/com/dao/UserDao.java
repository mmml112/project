
package com.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.entity.UserEntity;

/**
 * 用户
 */
public interface UserDao extends BaseMapper<UserEntity> {
	//而其他像 insert, updateById 之类的基本操作，是继承 BaseMapper 后自动就有的！
	List<UserEntity> selectListView(@Param("ew") Wrapper<UserEntity> wrapper);

	List<UserEntity> selectListView(Pagination page,@Param("ew") Wrapper<UserEntity> wrapper);
	//多了一个 Pagination page 参数：表示当前查询是分页的，需要知道“查第几页，每页多少条
}
