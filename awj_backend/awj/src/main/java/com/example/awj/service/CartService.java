package com.example.awj.service;

import com.example.awj.dto.CartDto;
import com.example.awj.vo.CartVo;

import java.util.List;

public interface CartService {
    List<CartVo> getCartList(Long userId);
    List<CartVo> getCartItems(Long userId, String itemIds);
    CartVo addCart(Long userId, CartDto dto);
    CartVo updateCart(Long userId, CartDto dto);
    void deleteCart(Long userId, Long id);
    void clearCart(Long userId);
}
