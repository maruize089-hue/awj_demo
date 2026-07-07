const { get, post } = require('../../utils/request');

Page({
  data: {
    item: {},
    type: 'PRODUCT',
    specs: [],
    selectedSpec: null,
    reviews: [],
    reviewCount: 0,
    isFavorite: false
  },

  onLoad(options) {
    const { id, type } = options;
    this.setData({ type: type || 'PRODUCT' });
    this.loadDetail(id, type);
    this.loadReviews(id);
    this.checkFavorite(id);
  },

  async loadDetail(id, type) {
    try {
      const url = type === 'PRODUCT' ? `/api/product/${id}` : `/api/service/${id}`;
      const res = await get(url);
      this.setData({ 
        item: res.data || {},
        specs: res.data && res.data.specs ? res.data.specs : []
      });
    } catch (error) {
      console.error('加载详情失败', error);
    }
  },

  async loadReviews(id) {
    try {
      const res = await get('/api/review/list', { productId: id });
      this.setData({ 
        reviews: res.data && res.data.list ? res.data.list : [],
        reviewCount: res.data && res.data.total ? res.data.total : 0
      });
    } catch (error) {
      console.error('加载评价失败', error);
    }
  },

  async checkFavorite(id) {
    try {
      const res = await get('/api/favorite/check', { productId: id });
      this.setData({ isFavorite: res.data || false });
    } catch (error) {
      console.error('检查收藏失败', error);
    }
  },

  selectSpec(e) {
    const spec = e.currentTarget.dataset.spec;
    this.setData({ selectedSpec: spec });
  },

  async toggleFavorite() {
    const { item, isFavorite } = this.data;
    try {
      if (isFavorite) {
        await post('/api/favorite/remove', { productId: item.id });
        this.setData({ isFavorite: false });
        wx.showToast({ title: '已取消收藏', icon: 'success' });
      } else {
        await post('/api/favorite/add', { productId: item.id });
        this.setData({ isFavorite: true });
        wx.showToast({ title: '已收藏', icon: 'success' });
      }
    } catch (error) {
      wx.showToast({ title: error.message || '操作失败', icon: 'none' });
    }
  },

  async addToCart() {
    const { item, type } = this.data;
    try {
      await post('/api/cart/add', {
        productId: item.id,
        type: type,
        name: item.name,
        price: item.price,
        image: item.image,
        quantity: 1
      });
      wx.showToast({ title: '已加入购物车', icon: 'success' });
    } catch (error) {
      wx.showToast({ title: error.message || '操作失败', icon: 'none' });
    }
  },

  buyNow() {
    const { item, type } = this.data;
    wx.navigateTo({
      url: `/pages/order/create?productId=${item.id}&type=${type}&quantity=1`
    });
  },

  goToCart() {
    wx.switchTab({ url: '/pages/cart/cart' });
  }
});
