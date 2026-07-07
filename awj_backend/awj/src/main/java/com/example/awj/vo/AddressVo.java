package com.example.awj.vo;

import lombok.Data;

@Data
public class AddressVo {
    private Long id;
    private String receiverName;
    private String receiverPhone;
    private String province;
    private String city;
    private String district;
    private String detail;
    private String fullAddress;
    private Integer isDefault;
}
