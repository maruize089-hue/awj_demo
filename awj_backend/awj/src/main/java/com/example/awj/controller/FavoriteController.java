package com.example.awj.controller;

import com.example.awj.common.result.Result;
import com.example.awj.security.LoginUser;
import com.example.awj.service.FavoriteService;
import com.example.awj.vo.FavoriteVo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorite")
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    private Long getCurrentUserId() {
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return loginUser.getId();
    }

    @GetMapping("/list")
    @PreAuthorize("hasRole('USER')")
    public Result<List<FavoriteVo>> getFavoriteList() {
        return Result.success(favoriteService.getFavoriteList(getCurrentUserId()));
    }

    @GetMapping("/check")
    @PreAuthorize("hasRole('USER')")
    public Result<Boolean> checkFavorite(@RequestParam Long productId) {
        return Result.success(favoriteService.checkFavorite(getCurrentUserId(), productId));
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('USER')")
    public Result<FavoriteVo> addFavorite(@RequestBody AddFavoriteDto dto) {
        return Result.success(favoriteService.addFavorite(getCurrentUserId(), dto.getProductId(), dto.getType()));
    }

    @PostMapping("/remove")
    @PreAuthorize("hasRole('USER')")
    public Result<Void> removeFavorite(@RequestBody RemoveFavoriteDto dto) {
        if (dto.getId() != null) {
            favoriteService.removeFavoriteById(getCurrentUserId(), dto.getId());
        } else if (dto.getProductId() != null) {
            favoriteService.removeFavorite(getCurrentUserId(), dto.getProductId());
        }
        return Result.success();
    }

    @lombok.Data
    public static class AddFavoriteDto {
        private Long productId;
        private String type;
    }

    @lombok.Data
    public static class RemoveFavoriteDto {
        private Long id;
        private Long productId;
    }
}
