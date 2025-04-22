
package com.controller;


import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.annotation.IgnoreAuth;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.entity.ConfigEntity;
import com.service.ConfigService;
import com.utils.PageUtils;
import com.utils.R;
import com.utils.ValidatorUtils;

/**
 * 登录相关
 */
@RequestMapping("config")
@RestController
public class ConfigController{
	
	@Autowired//@Autowired：自动注入 ConfigService，该服务类负责与配置相关的业务逻辑。
	private ConfigService configService;

	/**
     * 列表
     */
    @RequestMapping("/page")//路径：/config/page，这是一个用于分页查询配置列表的接口。
    public R page(@RequestParam Map<String, Object> params,ConfigEntity config){//@RequestParam Map<String, Object> params：用于接收前端传来的分页查询参数（如当前页码、每页显示数量）。ConfigEntity config：用于接收配置的查询条件（如配置名称、值等）。
        EntityWrapper<ConfigEntity> ew = new EntityWrapper<ConfigEntity>();
    	PageUtils page = configService.queryPage(params);//configService.queryPage(params)：调用 configService 的分页查询方法，根据传入的参数进行查询。
        return R.ok().put("data", page);//return R.ok().put("data", page)：返回分页查询的结果，封装在 R.ok() 中，page 包含了查询到的配置数据。
    }
    
	/**
     * 列表
     */
    @IgnoreAuth//@IgnoreAuth：表示该接口不需要身份验证，可能是公开的接口。
    @RequestMapping("/list")//路径：/config/list，用于查询所有配置列表。
    public R list(@RequestParam Map<String, Object> params,ConfigEntity config){
        EntityWrapper<ConfigEntity> ew = new EntityWrapper<ConfigEntity>();
    	PageUtils page = configService.queryPage(params);//configService.queryPage(params)：调用服务层的分页查询方法，查询配置数据。
        return R.ok().put("data", page);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{id}")//路径：/config/info/{id}，用于查询指定 ID 的配置详情。
    public R info(@PathVariable("id") String id){//@PathVariable("id") String id：从 URL 路径中提取配置的 ID
        ConfigEntity config = configService.selectById(id);//configService.selectById(id)：调用 configService 方法根据 ID 查询配置详情。
        return R.ok().put("data", config);//return R.ok().put("data", config)：返回查询到的配置详情。
    }
    
    /**
     * 前端详情
     */
    @IgnoreAuth//@IgnoreAuth：表示该接口不需要身份验证。
    @RequestMapping("/detail/{id}")//路径：/config/detail/{id}，这是一个公开的接口，通常用于前端用户查看配置详情。
    public R detail(@PathVariable("id") String id){//configService.selectById(id)：根据配置的 ID 查询该配置的详细信息。
        ConfigEntity config = configService.selectById(id);
        return R.ok().put("data", config);
    }
    
    /**
     * 根据name获取信息
     */
    @RequestMapping("/info")//路径：/config/info，用于根据 name 获取特定配置项。
    public R infoByName(@RequestParam String name){//@RequestParam String name：从请求中获取配置的名称（name），例如，查询 faceFile 配置项。
        ConfigEntity config = configService.selectOne(new EntityWrapper<ConfigEntity>().eq("name", "faceFile"));//configService.selectOne(...)：调用 ConfigService 的 selectOne 方法，根据名称查询数据库中的配置项。
        return R.ok().put("data", config);
    }
    
    /**
     * 保存
     */
    @PostMapping("/save")//@PostMapping("/save")：表示这是一个 POST 请求，路径为 /config/save，用于保存新的配置项。
    public R save(@RequestBody ConfigEntity config){//@RequestBody ConfigEntity config：通过请求体接收一个 ConfigEntity 对象，表示要保存的配置数据。
//    	ValidatorUtils.validateEntity(config);
    	configService.insert(config);//configService.insert(config)：调用 ConfigService 的 insert 方法，将配置数据保存到数据库中。
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")//路径：/config/update，用于更新已有配置项。
    public R update(@RequestBody ConfigEntity config){//@RequestBody ConfigEntity config：通过请求体接收一个 ConfigEntity 对象，表示要更新的配置项数据。
//        ValidatorUtils.validateEntity(config);
        configService.updateById(config);//全部更新configService.updateById(config)：调用 ConfigService 的 updateById 方法，更新数据库中的配置项。
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){//@RequestBody Long[] ids：从请求体中接收要删除的配置 ID 数组。
    	configService.deleteBatchIds(Arrays.asList(ids));//configService.deleteBatchIds(...)：调用 ConfigService 的 deleteBatchIds 方法，批量删除指定的配置项
        return R.ok();
    }
}
