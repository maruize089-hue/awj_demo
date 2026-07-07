const { get } = require('../../utils/request');
const { logout } = require('../../utils/auth');

Page({
  data: {
    userInfo: {},
    isLoggedIn: false,
    orderStats: {
      pending: 0,
      paid: 0,
      completed: 0,
      refunded: 0
    }
  },

  onLoad() {
    this.loadUserInfo();
  },

  onShow() {
    this.loadUserInfo();
  },

  async loadUserInfo() {
    try {
      const res = await get('/api/user/info');
      this.setData({
        userInfo: res.data || {},
        isLoggedIn: true
      });
      this.loadOrderStats();
    } catch (error) {
      this.setData({
        userInfo: {},
        isLoggedIn: false
      });
    }
  },

  async loadOrderStats() {
    try {
      const res = await get('/api/order/stats');
      this.setData({ orderStats: res.data || {} });
    } catch (error) {
      console.error('加载订单统计失败', error);
    }
  },

  goLogin() {
    wx.navigateTo({ url: '/pages/login/login' });
  },

  async logout() {
    try {
      await logout();
      this.setData({
        userInfo: {},
        isLoggedIn: false,
        orderStats: { pending: 0, paid: 0, completed: 0, refunded: 0 }
      });
      wx.showToast({ title: '已退出登录', icon: 'success' });
    } catch (error) {
      console.error('退出登录失败', error);
    }
  },

  goToOrders(e) {
    const status = e.currentTarget.dataset.status;
    wx.navigateTo({ url: `/pages/order/list?status=${status}` });
  },

  goToAddress() {
    wx.navigateTo({ url: '/pages/address/list' });
  },

  goToCoupons() {
    wx.navigateTo({ url: '/pages/coupon/list' });
  },

  goToWallet() {
    wx.navigateTo({ url: '/pages/wallet/index' });
  },

  goToFavorites() {
    wx.navigateTo({ url: '/pages/favorite/list' });
  },

  goToReviews() {
    wx.navigateTo({ url: '/pages/review/list' });
  },

  goToHelp() {
    wx.navigateTo({ url: '/pages/help/index' });
  }
});
