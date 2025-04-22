
package com.service.impl;


import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.dao.UserDao;
import com.entity.UserEntity;
import com.service.UserService;
import com.utils.PageUtils;
import com.utils.Query;


/**
 * 系统用户
 */
@Service("userService")//告诉 Spring 这是一个服务类（Service 组件），名字叫 "userService"。
public class UserServiceImpl extends ServiceImpl<UserDao, UserEntity> implements UserService {//implements UserService：实现了我们自己定义的用户服务接口。
//继承了 MyBatis Plus 的 ServiceImpl，自动获得了基本的增删改查功能（比如 insert(), selectById() 等
	@Override
	public PageUtils queryPage(Map<String, Object> params) {//new Query<UserEntity>(params).getPage()：通过传入的 params 参数构建分页信息。queryPage 方法用于实现分页查询用户。
		Page<UserEntity> page = this.selectPage(//this.selectPage()：调用 ServiceImpl 类提供的 selectPage 方法进行分页查询
                new Query<UserEntity>(params).getPage(),
                new EntityWrapper<UserEntity>()//EntityWrapper<UserEntity>()：用于构建查询条件，这里没有指定条件，表示查询所有用户。
        );
        return new PageUtils(page);//return new PageUtils(page)：将分页结果封装为 PageUtils 对象并返回
	}

	@Override
	public List<UserEntity> selectListView(Wrapper<UserEntity> wrapper) {
		return baseMapper.selectListView(wrapper);//baseMapper.selectListView(wrapper)：使用 baseMapper（由 ServiceImpl 提供）查询数据。wrapper 包含了查询的条件。
	}//selectListView 方法用于查询用户列表，并返回一个视图（即满足某些条件的用户列表）。

	@Override
	public PageUtils queryPage(Map<String, Object> params,
			Wrapper<UserEntity> wrapper) {
		 Page<UserEntity> page =new Query<UserEntity>(params).getPage();
	        page.setRecords(baseMapper.selectListView(page,wrapper));
	    	PageUtils pageUtil = new PageUtils(page);
	    	return pageUtil;//分页+查询条件
	}
}
