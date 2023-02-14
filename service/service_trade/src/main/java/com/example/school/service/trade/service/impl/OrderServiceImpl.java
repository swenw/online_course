package com.example.school.service.trade.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.school.common.base.result.ResultCodeEnum;
import com.example.school.service.base.dto.CourseDto;
import com.example.school.service.base.dto.MemberDto;
import com.example.school.service.base.exception.SchoolException;
import com.example.school.service.trade.entity.Order;
import com.example.school.service.trade.entity.PayLog;
import com.example.school.service.trade.feign.EduCourseService;
import com.example.school.service.trade.feign.UcenterMemberService;
import com.example.school.service.trade.mapper.OrderMapper;
import com.example.school.service.trade.mapper.PayLogMapper;
import com.example.school.service.trade.service.OrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.school.service.trade.util.OrderNoUtils;
import com.google.gson.Gson;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

    @Resource
    private PayLogMapper payLogMapper;

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

    @Override
    public Order getOrderByOrderNo(String orderNo) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_no", orderNo);
        return baseMapper.selectOne(queryWrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateOrderStatus(Map<String, String> notifyMap) {
        //更新订单状态
        String orderNo = notifyMap.get("out_trade_no");
        Order order = this.getOrderByOrderNo(orderNo);
        order.setStatus(1);//支付成功
        baseMapper.updateById(order);

        //记录支付日志
        PayLog payLog = new PayLog();
        payLog.setOrderNo(orderNo);
        payLog.setPayTime(new Date());
        payLog.setPayType(1);//支付类型
        payLog.setTotalFee(Long.parseLong(notifyMap.get("total_fee")));//总金额(分)
        payLog.setTradeState(notifyMap.get("result_code"));//支付状态
        payLog.setTransactionId(notifyMap.get("transaction_id"));
        payLog.setAttr(new Gson().toJson(notifyMap));
        payLogMapper.insert(payLog);

        //更新课程销量：有问题直接熔断
        eduCourseService.updateBuyCountById(order.getCourseId());
    }

    @Override
    public boolean queryPayStatus(String orderNo) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_no", orderNo);
        Order order = baseMapper.selectOne(queryWrapper);
        return order.getStatus() == 1;
    }
}
