
package com.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.entity.UserEntity;
import com.utils.PageUtils;


/**
 * 系统用户
 */
public interface UserService extends IService<UserEntity> {//继承了 MyBatis Plus 提供的通用增删改查（CRUD）接口。你写这个接口后，自动就拥有了基本的操作，比如 insert, delete, selectById, updateById 等。
//interface表示只是一个接口，不是具体实现

	PageUtils queryPage(Map<String, Object> params);//用于分页查询用户列表,返回：分页工具类 PageUtils，里面包含查询结果、总页数、当前页码等
    
   	List<UserEntity> selectListView(Wrapper<UserEntity> wrapper);//功能：返回不分页的用户列表（比如用在下拉菜单、全部数据导出）.参数：wrapper 是封装的查询条件（比如模糊查询、条件筛选等）
   	
   	PageUtils queryPage(Map<String, Object> params,Wrapper<UserEntity> wrapper);//功能：分页查询 + 带查询条件 参数：除了分页参数，还可以加上筛选条件（比如用户名包含“张”）
	   	
}
