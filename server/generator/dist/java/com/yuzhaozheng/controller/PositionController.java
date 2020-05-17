package com.yuzhaozheng.controller;

import com.alibaba.excel.EasyExcel;
import com.yuzhaozheng.query.PositionQuery;
import com.yuzhaozheng.response.ResponseData;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.yuzhaozheng.entity.Position;
import com.yuzhaozheng.query.PositionQuery;
import com.yuzhaozheng.service.PositionService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

/**
 * <p>职位 前端控制器</p>
 *
 * @author yuzhaozheng
 * @since 2020-05-17
 */
@RestController
public class PositionController {

    @Autowired
    private PositionService positionService;

    @RequestMapping("/position/page")
    public IPage<Position> page(@RequestBody PositionQuery query){
        return positionService.page(query);
    }

    @RequestMapping("/position/list")
    public List<Position> list(@RequestBody PositionQuery query){
        return positionService.list();
    }

    @RequestMapping("/position/findById")
    public ResponseData findById(@RequestParam Integer id) {
        Position position = positionService.getById(id);
        return ResponseData.success(position);
    }

    @RequestMapping("/position/save")
    public ResponseData save(@RequestBody Position position) {
        positionService.save(position);
        return ResponseData.success(position);
    }

    @RequestMapping("/position/update")
    public ResponseData update(@RequestBody Position position) {
        positionService.updateById(position);
        return ResponseData.success(position);
    }

    @RequestMapping("/position/delete")
    public ResponseData delete(@RequestParam List<Integer> id) {
        positionService.removeByIds(id);
        return ResponseData.success(id);
    }

    @ResponseBody
    @RequestMapping("/position/export")
    public void export(PositionQuery query, HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("职位.xlsx", "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
        EasyExcel.write(response.getOutputStream(), Position.class).sheet("职位").doWrite(list(query));
    }
}
