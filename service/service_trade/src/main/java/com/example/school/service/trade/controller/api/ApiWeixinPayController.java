package com.example.school.service.trade.controller.api;

import com.example.school.common.base.result.R;
import com.example.school.common.base.result.ResultCodeEnum;
import com.example.school.common.base.util.StreamUtils;
import com.example.school.service.trade.entity.Order;
import com.example.school.service.trade.service.OrderService;
import com.example.school.service.trade.service.WeixinPayService;
import com.example.school.service.trade.util.WeixinPayProperties;
import com.github.wxpay.sdk.WXPayUtil;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Handler;

@RestController
@RequestMapping("/api/trade/weixin-pay")
@Api(tags = "网站微信支付")
// @CrossOrigin //跨域
@Slf4j
public class ApiWeixinPayController {

    @Resource
    private WeixinPayService weixinPayService;

    @Resource
    private WeixinPayProperties weixinPayProperties;

    @Resource
    private OrderService orderService;

    @GetMapping("create-native/{orderNo}")
    public R createNative(@PathVariable String orderNo, HttpServletRequest request) {
        String remoteAddr = request.getRemoteAddr();
        Map<String, Object> map = weixinPayService.createNative(orderNo, remoteAddr);
        return R.ok().data(map);
    }

    @PostMapping("callback/notify")
    public String wxNotify(HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("callback/notify 被调用");

        // 获得通知结果
        ServletInputStream inputStream = request.getInputStream();
        String notifyXml = StreamUtils.inputStream2String(inputStream, "utf-8");
        System.out.println("xmlString = " + notifyXml);

        // 定义响应对象
        HashMap<String, String> returnMap = new HashMap<>();

        // 签名认证，防止伪造回调
        if (WXPayUtil.isSignatureValid(notifyXml, weixinPayProperties.getPartnerKey())) {
            // 解析返回结果
            Map<String, String> notifyMap = WXPayUtil.xmlToMap(notifyXml);

            //判断支付是否成功
            if("SUCCESS".equals(notifyMap.get("result_code"))){

                // 校验订单金额是否一致
                String totalFee = notifyMap.get("total_fee");
                String outTradeNo = notifyMap.get("out_trade_no");
                Order order = orderService.getOrderByOrderNo(outTradeNo);
                if(order != null && order.getTotalFee().intValue() == Integer.parseInt(totalFee)){

                    // 判断订单状态：保证接口调用的幂等性，如果订单状态已更新直接返回成功响应
                    // 幂等性：无论调用多少次结果都是一样的
                    if(order.getStatus() == 1){
                        returnMap.put("return_code", "SUCCESS");
                        returnMap.put("return_msg", "OK");
                        String returnXml = WXPayUtil.mapToXml(returnMap);
                        response.setContentType("text/xml");
                        log.warn("通知已处理");
                        return returnXml;
                    }else{
                        // 更新订单支付状态，并返回成功响应
                        orderService.updateOrderStatus(notifyMap);
                        returnMap.put("return_code", "SUCCESS");
                        returnMap.put("return_msg", "OK");
                        String returnXml = WXPayUtil.mapToXml(returnMap);
                        response.setContentType("text/xml");
                        log.info("支付成功，通知已处理");
                        return returnXml;
                    }
                }
            }
        }
        // 校验失败，返回失败应答
        returnMap.put("return_code", "FAIL");
        returnMap.put("return_msg", "");
        String returnXml = WXPayUtil.mapToXml(returnMap);
        response.setContentType("text/xml");
        log.warn("校验失败");
        return returnXml;
    }
}
