package com.example.awj.service.impl;

import com.example.awj.entity.CommunityService;
import com.example.awj.entity.Favorite;
import com.example.awj.entity.Product;
import com.example.awj.mapper.FavoriteMapper;
import com.example.awj.mapper.ProductMapper;
import com.example.awj.mapper.ServiceMapper;
import com.example.awj.service.FavoriteService;
import com.example.awj.vo.FavoriteVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteMapper favoriteMapper;
    private final ProductMapper productMapper;
    private final ServiceMapper serviceMapper;

    public FavoriteServiceImpl(FavoriteMapper favoriteMapper, ProductMapper productMapper,
                               ServiceMapper serviceMapper) {
        this.favoriteMapper = favoriteMapper;
        this.productMapper = productMapper;
        this.serviceMapper = serviceMapper;
    }

    @Override
    public List<FavoriteVo> getFavoriteList(Long userId) {
        LambdaQueryWrapper<Favorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favorite::getUserId, userId);
        wrapper.orderByDesc(Favorite::getCreateTime);
        
        return favoriteMapper.selectList(wrapper).stream()
            .map(this::convertToVo)
            .collect(Collectors.toList());
    }

    @Override
    public boolean checkFavorite(Long userId, Long productId) {
        LambdaQueryWrapper<Favorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favorite::getUserId, userId);
        wrapper.eq(Favorite::getProductId, productId);
        
        return favoriteMapper.selectCount(wrapper) > 0;
    }

    @Override
    @Transactional
    public FavoriteVo addFavorite(Long userId, Long productId, String type) {
        if (checkFavorite(userId, productId)) {
            throw new RuntimeException("已收藏");
        }
        
        Favorite favorite = new Favorite();
        favorite.setUserId(userId);
        favorite.setProductId(productId);
        favorite.setType(type != null ? type : "PRODUCT");
        
        Object item;
        if ("SERVICE".equals(favorite.getType())) {
            item = serviceMapper.selectById(productId);
        } else {
            item = productMapper.selectById(productId);
        }
        
        if (item != null) {
            if (item instanceof Product) {
                Product p = (Product) item;
                favorite.setName(p.getName());
                favorite.setImage(p.getImage());
                favorite.setPrice(p.getPrice());
            } else if (item instanceof CommunityService) {
                CommunityService s = (CommunityService) item;
                favorite.setName(s.getName());
                favorite.setImage(s.getImage());
                favorite.setPrice(s.getPrice());
            }
        }
        
        favoriteMapper.insert(favorite);
        return convertToVo(favorite);
    }

    @Override
    @Transactional
    public void removeFavorite(Long userId, Long productId) {
        LambdaQueryWrapper<Favorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favorite::getUserId, userId);
        wrapper.eq(Favorite::getProductId, productId);
        
        favoriteMapper.delete(wrapper);
    }

    @Override
    @Transactional
    public void removeFavoriteById(Long userId, Long id) {
        Favorite favorite = favoriteMapper.selectById(id);
        if (favorite == null || !favorite.getUserId().equals(userId)) {
            throw new RuntimeException("收藏不存在");
        }
        favoriteMapper.deleteById(id);
    }

    private FavoriteVo convertToVo(Favorite favorite) {
        FavoriteVo vo = new FavoriteVo();
        vo.setId(favorite.getId());
        vo.setProductId(favorite.getProductId());
        vo.setType(favorite.getType());
        vo.setName(favorite.getName());
        vo.setImage(favorite.getImage());
        vo.setPrice(favorite.getPrice());
        vo.setCreateTime(favorite.getCreateTime());
        return vo;
    }
}
