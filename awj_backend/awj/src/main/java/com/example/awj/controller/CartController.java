package com.example.awj.controller;

import com.example.awj.common.result.Result;
import com.example.awj.dto.CartDto;
import com.example.awj.security.LoginUser;
import com.example.awj.service.CartService;
import com.example.awj.vo.CartVo;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    private Long getCurrentUserId() {
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return loginUser.getId();
    }

    @GetMapping("/list")
    @PreAuthorize("hasRole('USER')")
    public Result<List<CartVo>> getCartList() {
        return Result.success(cartService.getCartList(getCurrentUserId()));
    }

    @GetMapping("/items/{itemIds}")
    @PreAuthorize("hasRole('USER')")
    public Result<List<CartVo>> getCartItems(@PathVariable String itemIds) {
        return Result.success(cartService.getCartItems(getCurrentUserId(), itemIds));
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('USER')")
    public Result<CartVo> addCart(@Valid @RequestBody CartDto dto) {
        return Result.success(cartService.addCart(getCurrentUserId(), dto));
    }

    @PostMapping("/update")
    @PreAuthorize("hasRole('USER')")
    public Result<CartVo> updateCart(@Valid @RequestBody CartDto dto) {
        return Result.success(cartService.updateCart(getCurrentUserId(), dto));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('USER')")
    public Result<Void> deleteCart(@PathVariable Long id) {
        cartService.deleteCart(getCurrentUserId(), id);
        return Result.success();
    }

    @DeleteMapping("/clear")
    @PreAuthorize("hasRole('USER')")
    public Result<Void> clearCart() {
        cartService.clearCart(getCurrentUserId());
        return Result.success();
    }
}
