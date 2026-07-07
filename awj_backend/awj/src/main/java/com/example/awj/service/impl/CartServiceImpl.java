package com.example.awj.service.impl;

import com.example.awj.dto.CartDto;
import com.example.awj.entity.Cart;
import com.example.awj.mapper.CartMapper;
import com.example.awj.service.CartService;
import com.example.awj.vo.CartVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    private final CartMapper cartMapper;

    public CartServiceImpl(CartMapper cartMapper) {
        this.cartMapper = cartMapper;
    }

    @Override
    public List<CartVo> getCartList(Long userId) {
        LambdaQueryWrapper<Cart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Cart::getUserId, userId)
               .orderByDesc(Cart::getCreateTime);
        
        return cartMapper.selectList(wrapper).stream()
            .map(this::convertToVo)
            .collect(Collectors.toList());
    }

    @Override
    public List<CartVo> getCartItems(Long userId, String itemIds) {
        List<Long> ids = Arrays.stream(itemIds.split(","))
            .map(Long::parseLong)
            .collect(Collectors.toList());
        
        LambdaQueryWrapper<Cart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Cart::getUserId, userId)
               .in(Cart::getId, ids);
        
        return cartMapper.selectList(wrapper).stream()
            .map(this::convertToVo)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CartVo addCart(Long userId, CartDto dto) {
        if (dto.getProductId() == null) {
            throw new RuntimeException("商品ID不能为空");
        }
        
        LambdaQueryWrapper<Cart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Cart::getUserId, userId)
               .eq(Cart::getProductId, dto.getProductId())
               .eq(Cart::getType, dto.getType());
        
        Cart existing = cartMapper.selectOne(wrapper);
        if (existing != null) {
            existing.setQuantity(existing.getQuantity() + (dto.getQuantity() != null ? dto.getQuantity() : 1));
            cartMapper.updateById(existing);
            return convertToVo(existing);
        }
        
        Cart cart = new Cart();
        cart.setUserId(userId);
        cart.setProductId(dto.getProductId());
        cart.setType(dto.getType() != null ? dto.getType() : "PRODUCT");
        cart.setName(dto.getName());
        cart.setSpec(dto.getSpec());
        cart.setImage(dto.getImage());
        cart.setPrice(dto.getPrice());
        cart.setQuantity(dto.getQuantity() != null ? dto.getQuantity() : 1);
        cart.setSelected(1);
        
        cartMapper.insert(cart);
        return convertToVo(cart);
    }

    @Override
    @Transactional
    public CartVo updateCart(Long userId, CartDto dto) {
        Cart cart = cartMapper.selectById(dto.getId());
        if (cart == null || !cart.getUserId().equals(userId)) {
            throw new RuntimeException("购物车项不存在");
        }
        
        if (dto.getQuantity() != null) {
            cart.setQuantity(cart.getQuantity() + dto.getQuantity());
            if (cart.getQuantity() <= 0) {
                cartMapper.deleteById(cart.getId());
                return null;
            }
        }
        
        cartMapper.updateById(cart);
        return convertToVo(cart);
    }

    @Override
    @Transactional
    public void deleteCart(Long userId, Long id) {
        Cart cart = cartMapper.selectById(id);
        if (cart == null || !cart.getUserId().equals(userId)) {
            throw new RuntimeException("购物车项不存在");
        }
        cartMapper.deleteById(id);
    }

    @Override
    @Transactional
    public void clearCart(Long userId) {
        LambdaQueryWrapper<Cart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Cart::getUserId, userId);
        cartMapper.delete(wrapper);
    }

    private CartVo convertToVo(Cart cart) {
        CartVo vo = new CartVo();
        vo.setId(cart.getId());
        vo.setProductId(cart.getProductId());
        vo.setType(cart.getType());
        vo.setName(cart.getName());
        vo.setSpec(cart.getSpec());
        vo.setImage(cart.getImage());
        vo.setPrice(cart.getPrice());
        vo.setQuantity(cart.getQuantity());
        vo.setSelected(cart.getSelected());
        return vo;
    }
}
