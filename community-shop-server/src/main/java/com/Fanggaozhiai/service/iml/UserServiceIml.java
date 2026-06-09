package com.Fanggaozhiai.service.iml;

import com.Fanggaozhiai.dto.UserLoginDTO;
import com.Fanggaozhiai.dto.UserPageParam;
import com.Fanggaozhiai.dto.UserPut;
import com.Fanggaozhiai.entity.Employee;
import com.Fanggaozhiai.entity.User;
import com.Fanggaozhiai.mapper.UserMapper;
import com.Fanggaozhiai.result.PageResult;
import com.Fanggaozhiai.service.UserService;
import com.Fanggaozhiai.utils.JwtUtil;
import com.Fanggaozhiai.vo.LoginReturn;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceIml implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Override
    public LoginReturn login(UserLoginDTO userLoginDTO) {
        // 根据用户名查询员工 返回员工
        com.Fanggaozhiai.entity.User user= userMapper.findByUsernameAndPassword(userLoginDTO);
        //查询到员工后 生成token
        if(user != null)
        {
            Map<String,Object> claims = new HashMap<>();
            claims.put("id",user.getId());
            claims.put("username",user.getUsername());
            claims.put("phone",user.getPhone());
            String token = JwtUtil.getToken(claims);
            return new LoginReturn(user.getId(),user.getUsername(),token);
        }
        else {
            return new LoginReturn(null,null,null);
        }
    }

    // 注册
    @Override
    public void register(User user) {
        //设置create time
        user.setCreateTime(LocalDate.now());
        userMapper.addByNameAndUsernameAndPassword(user);
    }
    //分页查询
    @Override
    public PageResult<Employee> list(UserPageParam userPageParam) {
        //开启分页
        PageHelper.startPage(userPageParam.getPage(),userPageParam.getPageSize());
        //查询到所有数据
        List<Employee> list = userMapper.list(userPageParam);
        //强转为 list 类型以用来返回到前端
        Page<Employee> page = (Page<Employee>) list;
        //返回分页数据
        return new PageResult<>(page.getTotal(),page.getResult());
    }

    //查询返回单条详细数据
    @Override
    public Employee getInfo(Integer id) {
        return userMapper.getInfo(id);
    }

    @Override
    public void update(UserPut user) {
        userMapper.update(user);
    }

    @Override
    public void delete(Integer id) {
        userMapper.delete(id);
    }
}
