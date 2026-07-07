const { get } = require('../../utils/request');

Page({
  data: {
    reviews: []
  },

  onLoad() {
    this.loadReviews();
  },

  async loadReviews() {
    try {
      const res = await get('/api/review/list');
      this.setData({ reviews: res.data.list || [] });
    } catch (error) {
      console.error('加载评价失败', error);
    }
  }
});
