
package com.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.entity.TokenEntity;
import com.utils.PageUtils;


/**
 * token
 */
public interface TokenService extends IService<TokenEntity> {//说明：这是一个服务接口类，继承了 MyBatis-Plus 提供的 IService<TokenEntity>泛型是 TokenEntity，说明这是处理 Token 记录的数据库表。
 	PageUtils queryPage(Map<String, Object> params);// 功能：分页查询 Token 表的数据
    // 实现：通过封装好的分页工具类（Query、PageUtils）处理前端传来的分页请求
   	List<TokenEntity> selectListView(Wrapper<TokenEntity> wrapper);//功能：使用包装器条件（比如 where 条件）查询 Token 列表数据
   	//管理员后台查看所有 token 或用户登录记录时可能用到
   	PageUtils queryPage(Map<String, Object> params,Wrapper<TokenEntity> wrapper);
	//功能：分页 + 条件查询
   	String generateToken(Long userid,String username,String tableName, String role);
   	//功能：生成一个 token 非常关键的方法！用于用户登录成功之后生成一个 token 返回给前端  参数解释：userid：用户的唯一 ID ,username：用户名 ,tableName：用户所属表（可能支持多个用户类型，比如用户、管理员）,role：用户的角色（admin、user 等
   	TokenEntity getTokenEntity(String token);// 功能：通过 token 字符串查找 token 对象
}//常用于验证用户身份，比如前端请求头里带了 token，后端就可以通过这个方法找到对应用户
/*小结：TokenService 的作用
Token 生成	用户登录成功后，调用 generateToken() 创建一个加密的 token
Token 查询	用户每次请求都会带 token，后端可以用 getTokenEntity() 来验证身份
分页管理	管理员可能要查看所有 token，支持分页、条件查询等
*/
