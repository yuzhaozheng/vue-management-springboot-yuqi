package com.yuzhaozheng.controller;

import com.alibaba.excel.EasyExcel;
import com.yuzhaozheng.query.UserQuery;
import com.yuzhaozheng.response.ResponseData;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.yuzhaozheng.entity.User;
import com.yuzhaozheng.query.UserQuery;
import com.yuzhaozheng.service.UserService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

/**
 * <p>用户 前端控制器</p>
 *
 * @author yuzhaozheng
 * @since 2020-05-17
 */
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/user/page")
    public IPage<User> page(@RequestBody UserQuery query){
        return userService.page(query);
    }

    @RequestMapping("/user/list")
    public List<User> list(@RequestBody UserQuery query){
        return userService.list();
    }

    @RequestMapping("/user/findById")
    public ResponseData findById(@RequestParam Integer id) {
        User user = userService.getById(id);
        return ResponseData.success(user);
    }

    @RequestMapping("/user/save")
    public ResponseData save(@RequestBody User user) {
        userService.save(user);
        return ResponseData.success(user);
    }

    @RequestMapping("/user/update")
    public ResponseData update(@RequestBody User user) {
        userService.updateById(user);
        return ResponseData.success(user);
    }

    @RequestMapping("/user/delete")
    public ResponseData delete(@RequestParam List<Integer> id) {
        userService.removeByIds(id);
        return ResponseData.success(id);
    }

    @ResponseBody
    @RequestMapping("/user/export")
    public void export(UserQuery query, HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("用户.xlsx", "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
        EasyExcel.write(response.getOutputStream(), User.class).sheet("用户").doWrite(list(query));
    }
}
