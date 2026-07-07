package com.example.awj.service;

import com.example.awj.vo.FavoriteVo;

import java.util.List;

public interface FavoriteService {
    List<FavoriteVo> getFavoriteList(Long userId);
    boolean checkFavorite(Long userId, Long productId);
    FavoriteVo addFavorite(Long userId, Long productId, String type);
    void removeFavorite(Long userId, Long productId);
    void removeFavoriteById(Long userId, Long id);
}
