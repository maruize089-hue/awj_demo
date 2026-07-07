package com.example.awj.service.impl;

import com.example.awj.entity.Wallet;
import com.example.awj.entity.WalletRecord;
import com.example.awj.mapper.WalletMapper;
import com.example.awj.mapper.WalletRecordMapper;
import com.example.awj.service.WalletService;
import com.example.awj.vo.WalletVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class WalletServiceImpl implements WalletService {

    private final WalletMapper walletMapper;
    private final WalletRecordMapper walletRecordMapper;

    public WalletServiceImpl(WalletMapper walletMapper, WalletRecordMapper walletRecordMapper) {
        this.walletMapper = walletMapper;
        this.walletRecordMapper = walletRecordMapper;
    }

    @Override
    public WalletVo getWalletInfo(Long userId) {
        Wallet wallet = walletMapper.selectOne(new LambdaQueryWrapper<Wallet>().eq(Wallet::getUserId, userId));
        if (wallet == null) {
            wallet = new Wallet();
            wallet.setUserId(userId);
            wallet.setBalance(BigDecimal.ZERO);
            walletMapper.insert(wallet);
        }
        return convertToVo(wallet);
    }

    @Override
    public List<Map<String, Object>> getWalletRecords(Long userId) {
        LambdaQueryWrapper<WalletRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WalletRecord::getUserId, userId);
        wrapper.orderByDesc(WalletRecord::getCreateTime);
        
        return walletRecordMapper.selectList(wrapper).stream()
            .map(record -> {
                Map<String, Object> map = new HashMap<>();
                map.put("id", record.getId());
                map.put("type", record.getType());
                map.put("amount", record.getAmount());
                map.put("createTime", record.getCreateTime());
                return map;
            })
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public WalletVo recharge(Long userId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("充值金额必须大于0");
        }
        
        Wallet wallet = walletMapper.selectOne(new LambdaQueryWrapper<Wallet>().eq(Wallet::getUserId, userId));
        if (wallet == null) {
            wallet = new Wallet();
            wallet.setUserId(userId);
            wallet.setBalance(BigDecimal.ZERO);
        }
        
        wallet.setBalance(wallet.getBalance().add(amount));
        if (wallet.getId() == null) {
            walletMapper.insert(wallet);
        } else {
            walletMapper.updateById(wallet);
        }
        
        WalletRecord record = new WalletRecord();
        record.setUserId(userId);
        record.setType("RECHARGE");
        record.setAmount(amount);
        record.setBalanceAfter(wallet.getBalance());
        record.setDescription("充值");
        walletRecordMapper.insert(record);
        
        return convertToVo(wallet);
    }

    @Override
    @Transactional
    public WalletVo withdraw(Long userId, BigDecimal amount) {
        Wallet wallet = walletMapper.selectOne(new LambdaQueryWrapper<Wallet>().eq(Wallet::getUserId, userId));
        if (wallet == null || wallet.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("余额不足");
        }
        
        wallet.setBalance(wallet.getBalance().subtract(amount));
        walletMapper.updateById(wallet);
        
        WalletRecord record = new WalletRecord();
        record.setUserId(userId);
        record.setType("WITHDRAW");
        record.setAmount(amount);
        record.setBalanceAfter(wallet.getBalance());
        record.setDescription("提现");
        walletRecordMapper.insert(record);
        
        return convertToVo(wallet);
    }

    private WalletVo convertToVo(Wallet wallet) {
        WalletVo vo = new WalletVo();
        vo.setId(wallet.getId());
        vo.setUserId(wallet.getUserId());
        vo.setBalance(wallet.getBalance());
        return vo;
    }
}
