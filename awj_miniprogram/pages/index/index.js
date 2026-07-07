const { get } = require('../../utils/request');

Page({
  data: {
    banners: [],
    categories: [],
    products: [],
    services: [],
    searchKeyword: ''
  },

  onLoad() {
    this.loadData();
  },

  onShow() {
    this.loadData();
  },

  async loadData() {
    try {
      const [bannerRes, categoryRes, productRes, serviceRes] = await Promise.all([
        get('/api/banner/list'),
        get('/api/category/list'),
        get('/api/product/list'),
        get('/api/service/list')
      ]);
      
      this.setData({
        banners: bannerRes.data || [],
        categories: categoryRes.data || [],
        products: productRes.data || [],
        services: serviceRes.data || []
      });
    } catch (error) {
      console.error('加载数据失败', error);
    }
  },

  onSearchInput(e) {
    this.setData({ searchKeyword: e.detail.value });
  },

  onSearch() {
    if (this.data.searchKeyword) {
      wx.navigateTo({
        url: `/pages/category/category?keyword=${this.data.searchKeyword}`
      });
    }
  },

  onBannerTap(e) {
    const index = e.currentTarget.dataset.index;
    const banner = this.data.banners[index];
    if (banner.link) {
      wx.navigateTo({ url: banner.link });
    }
  },

  onCategoryTap(e) {
    const id = e.currentTarget.dataset.id;
    wx.navigateTo({ url: `/pages/category/category?categoryId=${id}` });
  },

  goToCategory() {
    wx.switchTab({ url: '/pages/category/category' });
  },

  goToDetail(e) {
    const id = e.currentTarget.dataset.id;
    const type = e.currentTarget.dataset.type;
    wx.navigateTo({ url: `/pages/detail/detail?id=${id}&type=${type}` });
  },

  getCategoryIcon(name) {
    const iconMap = {
      '食品饮料': '🍔',
      '日用百货': '🛍️',
      '生鲜蔬果': '🥬',
      '家政服务': '🧹',
      '维修服务': '🔧',
      '美容美发': '💇'
    };
    return iconMap[name] || '📦';
  }
});
