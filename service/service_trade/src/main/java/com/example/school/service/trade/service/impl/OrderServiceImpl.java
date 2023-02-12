package com.example.school.service.trade.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.school.common.base.result.ResultCodeEnum;
import com.example.school.service.base.dto.CourseDto;
import com.example.school.service.base.dto.MemberDto;
import com.example.school.service.base.exception.SchoolException;
import com.example.school.service.trade.entity.Order;
import com.example.school.service.trade.feign.EduCourseService;
import com.example.school.service.trade.feign.UcenterMemberService;
import com.example.school.service.trade.mapper.OrderMapper;
import com.example.school.service.trade.service.OrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.school.service.trade.util.OrderNoUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author SWenW
 * @since 2023-02-12
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Resource
    private EduCourseService eduCourseService;

    @Resource
    private UcenterMemberService ucenterMemberService;

    @Override
    public String saveOrder(String courseId, String memberId) {

        //查询当前用户是否已有当前课程的订单
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id", courseId);
        queryWrapper.eq("member_id", memberId);
        Order orderExist = baseMapper.selectOne(queryWrapper);
        if(orderExist != null){
            return orderExist.getId(); //如果订单已存在，则直接返回订单id
        }

        MemberDto memberDto = ucenterMemberService.getMemberDtoByMemberId(memberId);
        if (memberDto == null) {
            throw new SchoolException(ResultCodeEnum.PARAM_ERROR);
        }

        CourseDto courseDto = eduCourseService.getCourseDtoById(courseId);
        if (courseDto == null) {
            throw new SchoolException(ResultCodeEnum.PARAM_ERROR);
        }

        // 创建订单
        Order order = new Order();
        order.setOrderNo(OrderNoUtils.getOrderNo());
        order.setCourseId(courseId);
        order.setCourseTitle(courseDto.getTitle());
        order.setCourseCover(courseDto.getCover());
        order.setTeacherName(courseDto.getTeacherName());
        // course数据库以元为单位，这边以分为单位
        order.setTotalFee(courseDto.getPrice().multiply(new BigDecimal(100)));//分
        order.setMemberId(memberId);
        order.setMobile(memberDto.getMobile());
        order.setNickname(memberDto.getNickname());

        order.setStatus(0);//未支付
        order.setPayType(1);//微信支付
        baseMapper.insert(order);
        return order.getId();
    }

    @Override
    public Order getByOrderId(String orderId, String memberId) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .eq("id", orderId)
                .eq("member_id", memberId);
        Order order = baseMapper.selectOne(queryWrapper);
        return order;
    }

    @Override
    public Boolean isBuyByCourseId(String courseId, String memberId) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .eq("member_id", memberId)
                .eq("course_id", courseId)
                .eq("status", 1);
        Integer count = baseMapper.selectCount(queryWrapper);
        return count > 0;
    }

    @Override
    public List<Order> selectByMemberId(String memberId) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("gmt_create");
        queryWrapper.eq("member_id", memberId);
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public boolean removeById(String orderId, String memberId) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", orderId).eq("member_id", memberId);

        return this.remove(queryWrapper);
    }
}
