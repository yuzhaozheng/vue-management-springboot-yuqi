package com.yuzhaozheng.controller;

import com.alibaba.excel.EasyExcel;
import com.yuzhaozheng.query.LogQuery;
import com.yuzhaozheng.response.ResponseData;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.yuzhaozheng.entity.Log;
import com.yuzhaozheng.query.LogQuery;
import com.yuzhaozheng.service.LogService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

/**
 * <p>日志 前端控制器</p>
 *
 * @author yuzhaozheng
 * @since 2020-05-17
 */
@RestController
public class LogController {

    @Autowired
    private LogService logService;

    @RequestMapping("/log/page")
    public IPage<Log> page(@RequestBody LogQuery query){
        return logService.page(query);
    }

    @RequestMapping("/log/list")
    public List<Log> list(@RequestBody LogQuery query){
        return logService.list();
    }

    @RequestMapping("/log/findById")
    public ResponseData findById(@RequestParam Integer id) {
        Log log = logService.getById(id);
        return ResponseData.success(log);
    }

    @RequestMapping("/log/save")
    public ResponseData save(@RequestBody Log log) {
        logService.save(log);
        return ResponseData.success(log);
    }

    @RequestMapping("/log/update")
    public ResponseData update(@RequestBody Log log) {
        logService.updateById(log);
        return ResponseData.success(log);
    }

    @RequestMapping("/log/delete")
    public ResponseData delete(@RequestParam List<Integer> id) {
        logService.removeByIds(id);
        return ResponseData.success(id);
    }

    @ResponseBody
    @RequestMapping("/log/export")
    public void export(LogQuery query, HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("日志.xlsx", "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
        EasyExcel.write(response.getOutputStream(), Log.class).sheet("日志").doWrite(list(query));
    }
}
