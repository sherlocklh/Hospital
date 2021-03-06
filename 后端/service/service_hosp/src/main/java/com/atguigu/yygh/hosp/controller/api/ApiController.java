package com.atguigu.yygh.hosp.controller.api;


import com.atguigu.yygh.common.exception.YyghException;
import com.atguigu.yygh.common.helper.HttpRequestHelper;
import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.common.result.ResultCodeEnum;
import com.atguigu.yygh.hosp.service.DepartmentService;
import com.atguigu.yygh.hosp.service.HospitalService;
import com.atguigu.yygh.hosp.service.HospitalSetService;
import com.atguigu.yygh.hosp.service.ScheduleService;
import com.atguigu.yygh.hosp.utils.CheckSign;
import com.atguigu.yygh.model.hosp.Department;
import com.atguigu.yygh.model.hosp.Hospital;
import com.atguigu.yygh.model.hosp.Schedule;
import com.atguigu.yygh.vo.hosp.DepartmentQueryVo;
import com.atguigu.yygh.vo.hosp.ScheduleQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Api("医院管理添加")
@RestController
@RequestMapping("/api/hosp")
public class ApiController {

    @Autowired
    private HospitalService hospitalService;

    @Autowired
    private HospitalSetService hospitalSetService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private ScheduleService scheduleService;

    /**
     * 上传医院接口
     * @param request
     * @return
     */
    @ApiOperation("上传医院接口")
    @PostMapping("saveHospital")
    public Result saveHospital(HttpServletRequest request){
        //获取传递过来医院信息
        Map<String, String[]> requestParameterMap = request.getParameterMap();
        //转换
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestParameterMap);

        //4.判断签名是否一致
        if(!CheckSign.checkSignEquals(paramMap)){
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }

        //传输过程照片转换
        String logoData = (String) paramMap.get("logoData");
        logoData = logoData.replaceAll(" ", "+");
        paramMap.put("logoData",logoData);

        //调用service方法
        hospitalService.save(paramMap);
        return Result.ok();
    }

    /**
     * 查询医院
     * @param request
     * @return
     */
    @ApiOperation("查询医院")
    @PostMapping("hospital/show")
    public Result<Hospital> getHospital(HttpServletRequest request){
        //获取传递过来医院信息
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);
        /**
         * sign判断
         */
        //获取医院编号
        String hoscode = (String) paramMap.get("hoscode");

        //4.判断签名是否一致
        if(!CheckSign.checkSignEquals(paramMap)){
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }

        //调用service方法实现 根据医院编号进行查询
        Hospital hospital = hospitalService.getByHoscode(hoscode);
        return Result.ok(hospital);
    }


    /**
     * 上传科室接口
     * @param request
     * @return
     */
    @ApiOperation("上传科室接口")
    @PostMapping("saveDepartment")
    public Result saveDepartment(HttpServletRequest request){
        //获取传递过来科室信息
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);


        //4.判断签名是否一致
        if(!CheckSign.checkSignEquals(paramMap)){
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }

        //调用service保存
        departmentService.save(paramMap);
        return Result.ok();
    }

    /**
     * 查询科室接口
     * @param request
     * @return
     */
    @ApiOperation("查询科室接口")
    @PostMapping("department/list")
    public Result findDepartment(HttpServletRequest request){
        //获取传递过来科室信息
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);

        //获取医院编号
        String  hoscode = (String) paramMap.get("hoscode");
        //获取当前页 和 每页记录数
        int page = StringUtils.isEmpty(paramMap.get("page")) ? 1:Integer.parseInt((String)paramMap.get("page"));
        int limit = StringUtils.isEmpty(paramMap.get("limit")) ? 10:Integer.parseInt((String)paramMap.get("limit"));

        //4.判断签名是否一致
        if(!CheckSign.checkSignEquals(paramMap)){
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }

        DepartmentQueryVo departmentQueryVo = new DepartmentQueryVo();
        departmentQueryVo.setHoscode(hoscode);
        //调用service方法
        Page<Department> pageModel =  departmentService.findPageDepartment(page, limit, departmentQueryVo);

        return Result.ok(pageModel);
    }

    /**
     * 删除科室接口
     * @param request
     * @return
     */
    @ApiOperation("删除科室接口")
    @PostMapping("department/remove")
    public Result removeDepartment(HttpServletRequest request){
        //获取传递过来科室信息
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);

        //获取医院编号
        String  hoscode = (String) paramMap.get("hoscode");
        //获取科室编号
        String depcode = (String) paramMap.get("depcode");

        //4.判断签名是否一致
        if(!CheckSign.checkSignEquals(paramMap)){
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }

        departmentService.remove(hoscode, depcode);
        return Result.ok();
    }

    /**
     * 上传排班的接口
     * @param request
     * @return
     */
    @ApiOperation("上传排班接口")
    @PostMapping("saveSchedule")
    public Result saveSchedule(HttpServletRequest request){
        //获取传递过来科室信息
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);

        //签名校验
        if(!CheckSign.checkSignEquals(paramMap)){
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }

        //保存
        scheduleService.save(paramMap);
        return Result.ok();
    }

    /**
     * 查询排班接口，进行分页返回
     * @param request
     * @return
     */
    @ApiOperation("查询排班接口")
    @PostMapping("schedule/list")
    public Result findSchedule(HttpServletRequest request){
        //获取传递过来排班信息
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);

        //4.判断签名是否一致
        if(!CheckSign.checkSignEquals(paramMap)){
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
        //获取医院编号
        String  hoscode = (String) paramMap.get("hoscode");
        //获取科室编号
        String depcode = (String) paramMap.get("depcode");
        //获取当前页 和 每页记录数
        int page = StringUtils.isEmpty(paramMap.get("page")) ? 1:Integer.parseInt((String)paramMap.get("page"));
        int limit = StringUtils.isEmpty(paramMap.get("limit")) ? 10:Integer.parseInt((String)paramMap.get("limit"));

        ScheduleQueryVo scheduleQueryVo = new ScheduleQueryVo();
        scheduleQueryVo.setHoscode(hoscode);
        scheduleQueryVo.setDepcode(depcode);
        //调用service方法
        Page<Schedule> pageModel =  scheduleService.findPageSchedule(page, limit, scheduleQueryVo);

        return Result.ok(pageModel);
    }

    /**
     * 删除排班接口
     * @param request
     * @return
     */
    @ApiOperation("删除排班接口")
    @PostMapping("schedule/remove")
    public Result remove(HttpServletRequest request){
        //获取传递过来的排班信息
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);

        //4.判断签名是否一致
        if(!CheckSign.checkSignEquals(paramMap)){
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
        //获取医院编号和排班信息
        String hoscode = (String) paramMap.get("hoscode");
        String hosScheduleId = (String) paramMap.get("hosScheduleId");

        scheduleService.remove(hoscode, hosScheduleId);
        return Result.ok();
    }
}
