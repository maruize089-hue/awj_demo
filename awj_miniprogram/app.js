App({
  onLaunch: function () {
    const token = wx.getStorageSync('token');
    if (!token) {
      wx.navigateTo({ url: '/pages/login/login' });
    }
  },

  onShow: function () {
    const token = wx.getStorageSync('token');
    if (!token) {
      wx.navigateTo({ url: '/pages/login/login' });
    }
  },

  globalData: {
    userInfo: null,
    role: '',
    token: ''
  }
});
