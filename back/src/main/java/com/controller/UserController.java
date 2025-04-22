
package com.controller;


import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.annotation.IgnoreAuth;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.entity.TokenEntity;
import com.entity.UserEntity;
import com.service.TokenService;
import com.service.UserService;
import com.utils.CommonUtil;
import com.utils.MPUtil;
import com.utils.PageUtils;
import com.utils.R;
import com.utils.ValidatorUtils;

/**
 * 登录相关
 */
@RequestMapping("users")//这个类里的所有接口，访问路径都会以 /users 开头。例如：登录接口路径就是 /users/login。


@RestController//告诉 Spring Boot 这是一个控制器类，所有方法返回的结果会自动变成 JSON。
public class UserController{
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private TokenService tokenService;

	/**
	 * 登录
	 */
	@IgnoreAuth//这个接口 不需要登录验证（比如你还没登录怎么验证你身份呢？）
	@PostMapping(value = "/login")//这是一个 POST 请求，前端会提交表单或 JSON 到这个地址。
	public R login(String username, String password, String captcha, HttpServletRequest request) {
		UserEntity user = userService.selectOne(new EntityWrapper<UserEntity>().eq("username", username));//UserEntity user = userService.selectOne(...)：从数据库查找是否有这个用户名。


		if(user==null || !user.getPassword().equals(password)) {//user == null || 密码不对：说明账号不存在或密码错了，直接报错。
			return R.error("账号或密码不正确");
		}
		String token = tokenService.generateToken(user.getId(),username, "users", user.getRole());//登录成功后生成一个“身份凭证” token。
		return R.ok().put("token", token);//返回一个 JSON 对象，里面有登录凭证。
	}
	
	/**
	 * 注册
	 */
	@IgnoreAuth//这个接口 不需要登录验证（比如你还没登录怎么验证你身份呢？）
	@PostMapping(value = "/register")
	public R register(@RequestBody UserEntity user){//@RequestBody 表示前端会发来一个 JSON 格式的用户对象，Spring 会自动解析成 UserEntity。
//    	ValidatorUtils.validateEntity(user);
    	if(userService.selectOne(new EntityWrapper<UserEntity>().eq("username", user.getUsername())) !=null) {
    		return R.error("用户已存在");//检查数据库里是否有这个用户名。如果存在，返回“用户已存在”的错误。
    	}
        userService.insert(user);
        return R.ok();//如果不存在，就用 insert(user) 把用户存进数据库。
    }

	/**
	 * 退出
	 */
	@GetMapping(value = "logout")
	public R logout(HttpServletRequest request) {
		request.getSession().invalidate();//注销当前用户的会话（让他退出登录）。
		return R.ok("退出成功");
	}
	
	/**
     * 密码重置
     */
    @IgnoreAuth
	@RequestMapping(value = "/resetPass")
    public R resetPass(String username, HttpServletRequest request){
    	UserEntity user = userService.selectOne(new EntityWrapper<UserEntity>().eq("username", username));
    	if(user==null) {
    		return R.error("账号不存在");
    	}
    	user.setPassword("123456");
        userService.update(user,null);
        return R.ok("密码已重置为：123456");
    }
	
	/**
     * 列表
     */
    @RequestMapping("/page")//用于在前端“分页展示用户数据”（比如每页显示10个用户，共20页）。
    public R page(@RequestParam Map<String, Object> params,UserEntity user){//@RequestParam Map<String, Object> params：接收前端传过来的参数，比如页码、每页条数、搜索内容等。


		EntityWrapper<UserEntity> ew = new EntityWrapper<UserEntity>();//EntityWrapper：用来构造数据库查询的“条件对象”。
    	PageUtils page = userService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.allLike(ew, user), params), params));//MPUtil：封装了一些常用条件拼接方法，比如模糊查询、范围查询、排序等。userService.queryPage(...)：调用 service 层的分页查询方法
        return R.ok().put("data", page);//最后把查询结果返回给前端。
    }

	/**
     * 列表
     */
    @RequestMapping("/list")//和 /page 类似，但不分页，返回所有符合条件的用户，比如前端下拉框中“选择所有用户”时用。
    public R list( UserEntity user){
       	EntityWrapper<UserEntity> ew = new EntityWrapper<UserEntity>();
      	ew.allEq(MPUtil.allEQMapPre( user, "user")); 
        return R.ok().put("data", userService.selectListView(ew));
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{id}")//前端点击一个用户，弹出详细信息的时候会用这个接口。
    public R info(@PathVariable("id") String id){//@PathVariable("id") 表示从 URL 地址中提取 id。
        UserEntity user = userService.selectById(id);//userService.selectById(id)：从数据库查出这个用户的完整信息。
        return R.ok().put("data", user);
    }
    
    /**
     * 获取用户的session用户信息
     */
    @RequestMapping("/session")//在前端页面需要显示“当前登录用户信息”时调用这个接口。
    public R getCurrUser(HttpServletRequest request){
    	Long id = (Long)request.getSession().getAttribute("userId");
        UserEntity user = userService.selectById(id);
        return R.ok().put("data", user);
    }

    /**
     * 保存
     */
    @PostMapping("/save")//创建一个新用户（管理员功能）。
    public R save(@RequestBody UserEntity user){
//    	ValidatorUtils.validateEntity(user);
    	if(userService.selectOne(new EntityWrapper<UserEntity>().eq("username", user.getUsername())) !=null) {
    		return R.error("用户已存在");
    	}
        userService.insert(user);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")//修改某个用户的信息（根据 ID 修改）。
    public R update(@RequestBody UserEntity user){
//        ValidatorUtils.validateEntity(user);
    	UserEntity u = userService.selectOne(new EntityWrapper<UserEntity>().eq("username", user.getUsername()));
    	if(u!=null && u.getId()!=user.getId() && u.getUsername().equals(user.getUsername())) {
    		return R.error("用户名已存在。");
    	}
        userService.updateById(user);//全部更新
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")//批量删除用户，比如前端选择多个用户然后点“删除”按钮。
    public R delete(@RequestBody Long[] ids){
        userService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }
}
