package com.stone.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.stone.bean.Allege;
import com.stone.bean.Company;
import com.stone.bean.Student;
import com.stone.bean.User;
import com.stone.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class UserServiceImp implements UserService{
    @Autowired
    UserDao udao;

    @Override
    public List<String> selPwd(String email)
    {
        return udao.selPwd(email);
    }

    //注册
    public boolean Register(User user) {
        boolean isSuccess = false;
        udao.Register(user);
        isSuccess = true;
        return isSuccess;
    }

    //判断邮箱是否被注册
    public boolean isEmailExist(String email){
        boolean isSuccess = false;
        User user = udao.getEmail(email);
        if(StringUtils.isEmpty(user)){
            isSuccess = true;
        }
        return isSuccess;
    }

    //  判断用户类型
    public String UserType(String email,String password){
        String str = null;
        //  得到邮箱
        User user = udao.getEmail(email);
        List<String> list = selPwd(email);
        if(user==null){
            str = "no";
        }else if(user.getState().equals("锁定")){ //  判断用户的状态
            str = "lock";
        }
//        else if(user.getEmail().equals("email") && !(user.getPassword().equals("password"))){ //    判断输入的账号和密码不一致
//            str="qt";
//        }
        else if(user.getPassword().equals(password)){ //   判断用户密码是否输入正确
            if(user.getType().equals("company")){ //    判断输入的用户是否是公司
                Company company = udao.getComByEmail(email);
                if(company==null){
                    str = "noCom";
                }else {
                    str = "company";
                }
            }else if(user.getType().equals("student")){//   判断用户是否是学生
                str = "student";
                Student student = udao.getStuByEmail(email);
                if(student==null){
                    udao.addStudent(email);
                }
            }else if(user.getType().equals("admin")){ //    判断用户是否是管理员
                str = "admin";
            }
        }
        return str; //  返回判断的结果
    }

    public Company getCom(String email){
        return udao.getComByEmail(email);
    }

    public Student getStu(String email){
        return udao.getStuByEmail(email);
    }

    public List<User> listStu() {
        return udao.listStu();
    }

    //查询用户列表
    public PageInfo<User> pageUser(User user, Integer pageNum, Integer pageSize) {
        PageInfo<User> page = null;

        PageHelper.startPage(pageNum, pageSize);
        List<User> ulist = udao.listUser(user);
        page = new PageInfo<User>(ulist);
        return page;
    }

    //删除用户
    public boolean delUser(int id) {
        boolean isSuccess = false;
        //删除用户
        udao.delUser(id);
        isSuccess = true;
        return isSuccess;
    }

    //锁定用户
    public boolean lockUser(int id) {
        boolean isSuccess = false;
        udao.lockUser(id);
        isSuccess = true;
        return isSuccess;
    }

    //解锁用户
    public boolean clearUser(int id) {
        boolean isSuccess = false;
        udao.clearUser(id);
        isSuccess = true;
        return isSuccess;
    }

    //管理员重置密码
    public boolean resetPassword(User user) {
        boolean isSuccess = false;
        udao.updatePassword(user);
        isSuccess = true;
        return isSuccess;
    }

    //通过id获取用户
    public User getUserById(int id) {
        return udao.getUserById(id);
    }

    //查询申述列表
    public PageInfo<Allege> pageAllege(Allege allege, Integer pageNum, Integer pageSize) {
        PageInfo<Allege> page = null;
        PageHelper.startPage(pageNum, pageSize);
        List<Allege> aList = udao.listAllege(allege);
        page = new PageInfo<Allege>(aList);
        return page;
    }

    //获取申述
    public Allege getAllegeById(int id) {
        return udao.getAllegeById(id);
    }

    //处理申述
    public Boolean dealAllege(Allege allege) {
        Boolean result = false;
        Date dNow = new Date( );
        SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd HH:mm");
        allege.setDealTime(ft.format(dNow));
        udao.dealAllege(allege);
        result = true;
        return result;
    }

    //删除申述
    public Boolean delAllege(int id) {
        boolean isSuccess = false;
        udao.delAllege(id);
        isSuccess = true;
        return isSuccess;
    }

    //重置密码
    public String resetPassword(String email, String phone, String password) {
        Company company = udao.getComByEmail(email);
        String str = "";
        if(!StringUtils.isEmpty(company)){
            if(company.getPhone().equals(phone)){
                User user = new User();
                user.setEmail(email);
                user.setPassword(password);
                udao.resetPassword(user);
                str = "修改成功";
            }else{
                str = "手机号错误！";
            }
        }else{
            str = "该用户不存在！";
        }
        return str;
    }
}
