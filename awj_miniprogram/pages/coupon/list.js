const { get } = require('../../utils/request');

Page({
  data: {
    activeTab: 'AVAILABLE',
    coupons: [],
    selectMode: false
  },

  onLoad(options) {
    this.setData({ selectMode: options.select === 'true' });
    this.loadCoupons();
  },

  async loadCoupons() {
    try {
      const res = await get('/api/coupon/list', { status: this.data.activeTab });
      this.setData({ coupons: res.data || [] });
    } catch (error) {
      console.error('加载优惠券失败', error);
    }
  },

  switchTab(e) {
    const tab = e.currentTarget.dataset.tab;
    this.setData({ activeTab: tab });
    this.loadCoupons();
  },

  selectCoupon(e) {
    const id = e.currentTarget.dataset.id;
    if (this.data.selectMode) {
      const coupon = this.data.coupons.find(c => c.id === id);
      if (coupon) {
        const pages = getCurrentPages();
        const prevPage = pages[pages.length - 2];
        prevPage.setData({ coupon });
        wx.navigateBack();
      }
    }
  }
});
