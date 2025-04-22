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

import com.entity.FankuiEntity;
import com.entity.view.FankuiView;

import com.service.FankuiService;
import com.service.TokenService;
import com.utils.PageUtils;
import com.utils.R;
import com.utils.MD5Util;
import com.utils.MPUtil;
import com.utils.CommonUtil;


/**
 * 反馈
 * 后端接口
 * @author 
 * @email 
 * @date 2021-04-24 23:05:57
 */
@RestController
@RequestMapping("/fankui")//@RequestMapping("/fankui")：指定控制器的基本路径为 /fankui，即所有的请求路径都以 /fankui 开头。
public class FankuiController {
    @Autowired
    private FankuiService fankuiService;//@Autowired 注解用于自动注入 FankuiService 和 TokenService 类，分别用于处理反馈相关的业务逻辑和令牌的处理。
    


    /**
     * 后端列表
     *///这个接口用于查询反馈的分页列表，并返回给前端。管理员可以通过该接口查看所有反馈记录，普通用户只能查看自己提交的反馈。
    @RequestMapping("/page")//路径：/fankui/page，用于后端管理系统查询反馈列表。
    public R page(@RequestParam Map<String, Object> params,FankuiEntity fankui,//@RequestParam Map<String, Object> params：接收前端传来的分页查询参数。FankuiEntity fankui：接收前端传来的反馈条件（例如用户名、反馈内容等）。
		HttpServletRequest request){//HttpServletRequest request：通过请求获取会话信息，确认用户的角色或其他信息。
		String tableName = request.getSession().getAttribute("tableName").toString();
		if(tableName.equals("yonghu")) {//tableName.equals("yonghu")：如果当前用户是普通用户（yonghu），则设置 fankui 对象中的 Yonghuming 属性为当前会话中的用户名
			fankui.setYonghuming((String)request.getSession().getAttribute("username"));
		}
        EntityWrapper<FankuiEntity> ew = new EntityWrapper<FankuiEntity>();//EntityWrapper<FankuiEntity> ew = new EntityWrapper<FankuiEntity>();：构建查询条件，ew 用于包装 SQL 查询。
		PageUtils page = fankuiService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, fankui), params), params));//MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, fankui), params), params)：利用 MPUtil 提供的工具方法构建复杂的查询条件，包括排序、范围查询、模糊匹配等。
//fankuiService.queryPage(params, ...)：调用 fankuiService 中的分页查询方法，返回分页数据。
        return R.ok().put("data", page);//返回分页查询的结果。
    }
    
    /**
     * 前端列表
     *///功能：该接口与后端查询接口类似，用于前端查询用户提交的反馈信息。用户可以通过分页查询来查看他们提交的所有反馈
    @RequestMapping("/list")//路径：/fankui/list，这是前端用户查询反馈列表的接口。
    public R list(@RequestParam Map<String, Object> params,FankuiEntity fankui, 
		HttpServletRequest request){
        EntityWrapper<FankuiEntity> ew = new EntityWrapper<FankuiEntity>();
		PageUtils page = fankuiService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, fankui), params), params));//fankuiService.queryPage(...)：调用 fankuiService 中的分页查询方法，支持排序、范围查询和模糊查询等操作。
        return R.ok().put("data", page);
    }

	/**
     * 列表
     */
    @RequestMapping("/lists")//路径：/fankui/lists，用于查询所有的反馈记录。
    public R list( FankuiEntity fankui){
       	EntityWrapper<FankuiEntity> ew = new EntityWrapper<FankuiEntity>();
      	ew.allEq(MPUtil.allEQMapPre( fankui, "fankui")); //ew.allEq(MPUtil.allEQMapPre(fankui, "fankui"))：根据 fankui 对象的属性构建精确查询条件，查询所有符合条件的反馈记录。
        return R.ok().put("data", fankuiService.selectListView(ew));//fankuiService.selectListView(ew)：调用服务层的 selectListView 方法，查询所有符合条件的反馈记录。
    }

	 /**
     * 查询
     *///路径：/fankui/query，用于查询单个反馈记录。
    @RequestMapping("/query")
    public R query(FankuiEntity fankui){
        EntityWrapper< FankuiEntity> ew = new EntityWrapper< FankuiEntity>();
 		ew.allEq(MPUtil.allEQMapPre( fankui, "fankui")); //ew.allEq(MPUtil.allEQMapPre(fankui, "fankui"))：构建查询条件，查询与 fankui 对象匹配的单个反馈记录。
		FankuiView fankuiView =  fankuiService.selectView(ew);//fankuiService.selectOne(ew)：调用 fankuiService 中的 selectOne 方法查询单个反馈记录。
		return R.ok("查询反馈成功").put("data", fankuiView);
    }
	
    /**
     * 后端详情
     */
    @RequestMapping("/info/{id}")//路径：/fankui/info/{id}，这是一个用于查询反馈详情的接口，通常供管理员使用。
    public R info(@PathVariable("id") Long id){//@PathVariable("id") Long id：从 URL 中提取反馈的 ID。
        FankuiEntity fankui = fankuiService.selectById(id);//fankuiService.selectById(id)：调用服务层的方法根据反馈 ID 查询单个反馈记录。
        return R.ok().put("data", fankui);//返回查询到的反馈详情。
    }

    /**
     * 前端详情
     */
    @RequestMapping("/detail/{id}")//路径：/fankui/detail/{id}，用于查询用户提交的反馈的详情。
    public R detail(@PathVariable("id") Long id){
        FankuiEntity fankui = fankuiService.selectById(id);//fankuiService.selectById(id)：查询数据库中指定 ID 的反馈记录。
        return R.ok().put("data", fankui);
    }
    



    /**
     * 后端保存
     */
    @RequestMapping("/save")//路径：/fankui/save，这是一个 POST 请求，用于管理员在后台添加反馈记录。
    public R save(@RequestBody FankuiEntity fankui, HttpServletRequest request){//@RequestBody FankuiEntity fankui：从请求体中接收一个 FankuiEntity 对象，表示要保存的反馈数据。
    	fankui.setId(new Date().getTime()+new Double(Math.floor(Math.random()*1000)).longValue());//fankui.setId(...)：为反馈生成唯一的 ID，使用当前时间戳和随机数。
    	//ValidatorUtils.validateEntity(fankui);
        fankuiService.insert(fankui);//fankuiService.insert(fankui)：调用 fankuiService 的 insert 方法，将新的反馈数据插入数据库。
        return R.ok();
    }
    
    /**
     * 前端保存
     */
    @RequestMapping("/add")//路径：/fankui/add，这是一个 POST 请求，用于前端用户提交反馈。
    public R add(@RequestBody FankuiEntity fankui, HttpServletRequest request){//@RequestBody FankuiEntity fankui：从请求体中接收一个 FankuiEntity 对象，表示前端用户提交的反馈数据。
    	fankui.setId(new Date().getTime()+new Double(Math.floor(Math.random()*1000)).longValue());//fankui.setId(...)：为反馈生成唯一的 ID。
    	//ValidatorUtils.validateEntity(fankui);
        fankuiService.insert(fankui);//fankuiService.insert(fankui)：调用服务层的方法将反馈数据保存到数据库中。
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")//路径：/fankui/update，这是一个 POST 请求，路径为 /fankui/update，用于更新已有的反馈记录。
    public R update(@RequestBody FankuiEntity fankui, HttpServletRequest request){//@RequestBody FankuiEntity fankui：从请求体中接收一个 FankuiEntity 对象，包含要更新的反馈数据。
        //ValidatorUtils.validateEntity(fankui);
        fankuiService.updateById(fankui);//全部更新fankuiService.updateById(fankui)：调用 fankuiService 的 updateById 方法，根据反馈的 ID 完整更新数据库中的反馈数据。
        return R.ok();
    }
    

    /**
     * 删除
     */
    @RequestMapping("/delete")//路径：/fankui/delete，这是一个 POST 请求，路径为 /fankui/delete，用于删除反馈记录。
    public R delete(@RequestBody Long[] ids){//@RequestBody Long[] ids：从请求体中接收一个包含多个反馈 ID 的数组，表示要删除的反馈记录。
        fankuiService.deleteBatchIds(Arrays.asList(ids));//fankuiService.deleteBatchIds(Arrays.asList(ids))：调用 fankuiService 的 deleteBatchIds 方法，批量删除指定的反馈记录。
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
		
		Wrapper<FankuiEntity> wrapper = new EntityWrapper<FankuiEntity>();
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

		int count = fankuiService.selectCount(wrapper);
		return R.ok().put("count", count);
	}
	


}
