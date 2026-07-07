const { get, post } = require('../../utils/request');

Page({
  data: {
    orderId: null,
    order: {},
    rating: 5,
    content: '',
    loading: false
  },

  onLoad(options) {
    if (options.orderId) {
      this.setData({ orderId: options.orderId });
      this.loadOrder(options.orderId);
    }
  },

  async loadOrder(id) {
    try {
      const res = await get('/api/order/' + id);
      this.setData({ order: res.data || {} });
    } catch (error) {
      console.error('加载订单失败', error);
    }
  },

  setRating(e) {
    const rating = e.currentTarget.dataset.rating;
    this.setData({ rating });
  },

  onContentInput(e) {
    this.setData({ content: e.detail.value });
  },

  async submitReview() {
    if (!this.data.content.trim()) {
      wx.showToast({ title: '请输入评价内容', icon: 'none' });
      return;
    }
    
    this.setData({ loading: true });
    
    try {
      await post('/api/review/create', {
        orderId: this.data.orderId,
        rating: this.data.rating,
        content: this.data.content
      });
      
      wx.showToast({ title: '评价成功', icon: 'success' });
      setTimeout(() => {
        wx.navigateBack();
      }, 1500);
    } catch (error) {
      wx.showToast({ title: error.message || '评价失败', icon: 'none' });
    } finally {
      this.setData({ loading: false });
    }
  }
});
