const { get, post } = require('../../utils/request');

Page({
  data: {
    balance: '0.00',
    records: []
  },

  onLoad() {
    this.loadWallet();
    this.loadRecords();
  },

  async loadWallet() {
    try {
      const res = await get('/api/wallet/info');
      this.setData({ balance: res.data && res.data.balance ? res.data.balance.toFixed(2) : '0.00' });
    } catch (error) {
      console.error('加载钱包失败', error);
    }
  },

  async loadRecords() {
    try {
      const res = await get('/api/wallet/records');
      this.setData({ records: res.data || [] });
    } catch (error) {
      console.error('加载账单记录失败', error);
    }
  },

  recharge() {
    wx.showModal({
      title: '充值',
      content: '充值功能开发中',
      showCancel: false
    });
  },

  withdraw() {
    wx.showModal({
      title: '提现',
      content: '提现功能开发中',
      showCancel: false
    });
  },

  getRecordIcon(type) {
    const map = {
      'RECHARGE': '💰',
      'WITHDRAW': '💸',
      'PAY': '💳',
      'REFUND': '🔄'
    };
    return map[type] || '📝';
  },

  getRecordName(type) {
    const map = {
      'RECHARGE': '充值',
      'WITHDRAW': '提现',
      'PAY': '消费',
      'REFUND': '退款'
    };
    return map[type] || type;
  }
});
