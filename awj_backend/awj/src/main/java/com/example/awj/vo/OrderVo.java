package com.example.awj.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderVo {
    private Long id;
    private String orderNo;
    private String orderType;
    private Long userId;
    private Long addressId;
    private BigDecimal subtotal;
    private BigDecimal shippingFee;
    private BigDecimal couponAmount;
    private BigDecimal totalAmount;
    private String payType;
    private String status;
    private String statusText;
    private String remark;
    private String serviceTime;
    private Integer itemCount;
    private LocalDateTime createTime;
    private LocalDateTime payTime;
    private LocalDateTime completeTime;
    
    private List<OrderItemVo> items;
    private AddressVo address;
    
    @Data
    public static class OrderItemVo {
        private Long id;
        private Long productId;
        private String type;
        private String name;
        private String spec;
        private String image;
        private BigDecimal price;
        private Integer quantity;
    }
    
    @Data
    public static class AddressVo {
        private String receiver;
        private String phone;
        private String province;
        private String city;
        private String district;
        private String detail;
    }
}
