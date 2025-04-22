package com.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import com.utils.ValidatorUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.annotation.IgnoreAuth;

import com.entity.DingdanEntity;
import com.entity.view.DingdanView;

import com.service.DingdanService;
import com.service.TokenService;
import com.utils.PageUtils;
import com.utils.R;
import com.utils.MD5Util;
import com.utils.MPUtil;
import com.utils.CommonUtil;


/**
 * 订单
 * 后端接口
 * @author 
 * @email 
 * @date 2021-04-24 23:05:57
 */
@RestController//@RestController 注解表示这是一个控制器，处理 RESTful 风格的 HTTP 请求。
@RequestMapping("/dingdan")//@RequestMapping("/dingdan") 指定了该控制器的基本 URL 路径为 /dingdan，即所有相关接口的路径都会以 /dingdan 开头。
public class DingdanController {
    @Autowired//@Autowired 注解表示自动注入依赖的服务类。DingdanService 处理订单相关的业务逻辑，TokenService 用于处理与令牌相关的操作。
    private DingdanService dingdanService;
    


    /**
     * 后端列表
     */
    @RequestMapping("/page")//@RequestMapping("/page")：这是一个 GET 请求，路径为 /dingdan/page，用于获取订单的分页列表。
    public R page(@RequestParam Map<String, Object> params,DingdanEntity dingdan,//@RequestParam Map<String, Object> params：从请求中获取分页参数（如当前页码和每页显示数量）。DingdanEntity dingdan：用于接收前端传来的查询条件，例如订单的字段（如用户名、订单状态等）。
		HttpServletRequest request){//HttpServletRequest request：通过请求获取会话中的信息，例如当前用户的角色和用户名。
		String tableName = request.getSession().getAttribute("tableName").toString();//String tableName = request.getSession().getAttribute("tableName").toString();：从当前会话中获取 tableName，判断当前用户角色是 yonghu（用户角色），如果是用户角色，则设置 dingdan 对象的 Yonghuming 属性为当前会话中的用户名。
		if(tableName.equals("yonghu")) {
			dingdan.setYonghuming((String)request.getSession().getAttribute("username"));
		}
        EntityWrapper<DingdanEntity> ew = new EntityWrapper<DingdanEntity>();//EntityWrapper<DingdanEntity> ew = new EntityWrapper<DingdanEntity>();：创建一个 EntityWrapper 用于构造查询条件。
		PageUtils page = dingdanService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, dingdan), params), params));//dingdanService.queryPage(...)：调用 DingdanService 层的分页查询方法，获取符合条件的订单数据。
//MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, dingdan), params), params)：这是 MyBatis-Plus 提供的一些工具方法，完成排序、范围查询（BETWEEN）以及模糊查询（LIKE）的功能。
        return R.ok().put("data", page);//return R.ok().put("data", page);：返回分页数据的响应。
    }
    
    /**
     * 前端列表
     */
    @RequestMapping("/list")//@RequestMapping("/list")：这是一个 GET 请求，路径为 /dingdan/list，用于查询前端显示的订单列表。
    public R list(@RequestParam Map<String, Object> params,DingdanEntity dingdan, //@RequestParam Map<String, Object> params：获取请求中的分页参数。DingdanEntity dingdan：传入的查询条件（如订单编号、状态等）。
		HttpServletRequest request){//HttpServletRequest request：获取会话信息。
        EntityWrapper<DingdanEntity> ew = new EntityWrapper<DingdanEntity>();
		PageUtils page = dingdanService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, dingdan), params), params));//dingdanService.queryPage(...)：调用 DingdanService 层的分页查询方法，获取分页的订单数据，并返回。
        return R.ok().put("data", page);
    }

	/**
     * 列表
     */
    @RequestMapping("/lists")//@RequestMapping("/lists")：这是一个 GET 请求，路径为 /dingdan/lists，用于查询所有订单。
    public R list( DingdanEntity dingdan){
       	EntityWrapper<DingdanEntity> ew = new EntityWrapper<DingdanEntity>();
      	ew.allEq(MPUtil.allEQMapPre( dingdan, "dingdan")); //ew.allEq(MPUtil.allEQMapPre(dingdan, "dingdan"))：使用 MPUtil.allEQMapPre() 方法，将 dingdan 对象中的属性与数据库字段进行精确匹配，查询所有符合条件的订单。
        return R.ok().put("data", dingdanService.selectListView(ew));//dingdanService.selectListView(ew)：调用 DingdanService 层的方法获取查询结果。
    }//返回订单列表数据。

	 /**
     * 查询
     */
    @RequestMapping("/query")//@RequestMapping("/query")：这是一个 GET 请求，路径为 /dingdan/query，用于查询单个订单。
    public R query(DingdanEntity dingdan){
        EntityWrapper< DingdanEntity> ew = new EntityWrapper< DingdanEntity>();
 		ew.allEq(MPUtil.allEQMapPre( dingdan, "dingdan")); //ew.allEq(MPUtil.allEQMapPre(dingdan, "dingdan"))：与之前的查询方法相同，通过 dingdan 对象构建精确查询条件。
		DingdanView dingdanView =  dingdanService.selectView(ew);//dingdanService.selectOne(ew)：调用 DingdanService 层的方法查询单个符合条件的订单。
		return R.ok("查询订单成功").put("data", dingdanView);//返回单个订单数据。
    }
	
    /**
     * 后端详情
     */
    @RequestMapping("/info/{id}")//@RequestMapping("/info/{id}")：这是一个 GET 请求，路径为 /dingdan/info/{id}，用于查询订单的详情。
    public R info(@PathVariable("id") Long id){//@PathVariable("id") Long id：从 URL 中提取订单的 ID。
        DingdanEntity dingdan = dingdanService.selectById(id);//dingdanService.selectById(id)：通过订单 ID 查询该订单的详细信息
        return R.ok().put("data", dingdan);//返回查询到的订单数据。
    }

    /**
     * 前端详情
     */
    @RequestMapping("/detail/{id}")//@RequestMapping("/detail/{id}")：这是一个 GET 请求，路径为 /dingdan/detail/{id}，用于查询订单的详细信息，通常用于前端显示。
    public R detail(@PathVariable("id") Long id){//@PathVariable("id") Long id：从 URL 中提取订单的 ID。
        DingdanEntity dingdan = dingdanService.selectById(id);//dingdanService.selectById(id)：通过订单 ID 查询该订单的详细信息。
        return R.ok().put("data", dingdan);//返回查询到的订单数据。
    }
    



    /**
     * 后端保存
     */
    @RequestMapping("/save")//@RequestMapping("/save")：这是一个 POST 请求，路径为 /dingdan/save，用于保存订单数据。
    public R save(@RequestBody DingdanEntity dingdan, HttpServletRequest request){//@RequestBody DingdanEntity dingdan：从请求体中接收一个 DingdanEntity 对象，表示要保存的订单数据。
    	dingdan.setId(new Date().getTime()+new Double(Math.floor(Math.random()*1000)).longValue());//dingdan.setId(...)：生成一个唯一的订单 ID，使用当前时间戳和随机数。
    	//ValidatorUtils.validateEntity(dingdan);
        dingdanService.insert(dingdan);//dingdanService.insert(dingdan)：调用 dingdanService 的 insert 方法，将订单数据插入数据库。
        return R.ok();//返回保存成功的响应。
    }
    
    /**
     * 前端保存
     */
    @RequestMapping("/add")//这段代码与上面的保存订单接口类似，但这里的接口是针对前端的。接收前端传来的订单数据并保存。
    public R add(@RequestBody DingdanEntity dingdan, HttpServletRequest request){
    	dingdan.setId(new Date().getTime()+new Double(Math.floor(Math.random()*1000)).longValue());
    	//ValidatorUtils.validateEntity(dingdan);
        dingdanService.insert(dingdan);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")//@RequestMapping("/update")：这是一个 POST 请求，路径为 /dingdan/update，用于更新订单的详细信息。
    public R update(@RequestBody DingdanEntity dingdan, HttpServletRequest request){//@RequestBody DingdanEntity dingdan：从请求体中接收一个 DingdanEntity 对象，包含了需要更新的订单数据。
        //ValidatorUtils.validateEntity(dingdan);
        dingdanService.updateById(dingdan);//全部更新dingdanService.updateById(dingdan)：调用 dingdanService 的 updateById 方法，根据订单的 ID 更新数据库中的订单数据。
        return R.ok();
    }
    

    /**
     * 删除
     */
    @RequestMapping("/delete")//@RequestMapping("/delete")：这是一个 POST 请求，路径为 /dingdan/delete，用于删除订单。
    public R delete(@RequestBody Long[] ids){//@RequestBody Long[] ids：从请求体中接收一个包含订单 ID 数组的参数，表示需要删除的订单。
        dingdanService.deleteBatchIds(Arrays.asList(ids));//dingdanService.deleteBatchIds(Arrays.asList(ids))：调用 dingdanService 的 deleteBatchIds 方法，批量删除指定的订单。
        return R.ok();
    }
    
    /**
     * 提醒接口
     */
	@RequestMapping("/remind/{columnName}/{type}")
	public R remindCount(@PathVariable("columnName") String columnName, HttpServletRequest request, 
						 @PathVariable("type") String type,@RequestParam Map<String, Object> map) {
		map.put("column", columnName);
		map.put("type", type);
		
		if(type.equals("2")) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar c = Calendar.getInstance();
			Date remindStartDate = null;
			Date remindEndDate = null;
			if(map.get("remindstart")!=null) {
				Integer remindStart = Integer.parseInt(map.get("remindstart").toString());
				c.setTime(new Date()); 
				c.add(Calendar.DAY_OF_MONTH,remindStart);
				remindStartDate = c.getTime();
				map.put("remindstart", sdf.format(remindStartDate));
			}
			if(map.get("remindend")!=null) {
				Integer remindEnd = Integer.parseInt(map.get("remindend").toString());
				c.setTime(new Date());
				c.add(Calendar.DAY_OF_MONTH,remindEnd);
				remindEndDate = c.getTime();
				map.put("remindend", sdf.format(remindEndDate));
			}
		}
		
		Wrapper<DingdanEntity> wrapper = new EntityWrapper<DingdanEntity>();
		if(map.get("remindstart")!=null) {
			wrapper.ge(columnName, map.get("remindstart"));
		}
		if(map.get("remindend")!=null) {
			wrapper.le(columnName, map.get("remindend"));
		}

		String tableName = request.getSession().getAttribute("tableName").toString();
		if(tableName.equals("yonghu")) {
			wrapper.eq("yonghuming", (String)request.getSession().getAttribute("username"));
		}

		int count = dingdanService.selectCount(wrapper);
		return R.ok().put("count", count);
	}
	


}
