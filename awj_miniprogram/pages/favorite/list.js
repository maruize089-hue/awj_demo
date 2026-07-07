const { get, post } = require('../../utils/request');

Page({
  data: {
    favorites: []
  },

  onLoad() {
    this.loadFavorites();
  },

  onShow() {
    this.loadFavorites();
  },

  async loadFavorites() {
    try {
      const res = await get('/api/favorite/list');
      this.setData({ favorites: res.data || [] });
    } catch (error) {
      console.error('加载收藏失败', error);
    }
  },

  goToDetail(e) {
    const id = e.currentTarget.dataset.id;
    const type = e.currentTarget.dataset.type;
    wx.navigateTo({ url: `/pages/detail/detail?id=${id}&type=${type}` });
  },

  async removeFavorite(e) {
    const id = e.currentTarget.dataset.id;
    
    wx.showModal({
      title: '确认删除',
      content: '确定要取消收藏吗？',
      success: async (res) => {
        if (res.confirm) {
          try {
            await post('/api/favorite/remove', { id });
            wx.showToast({ title: '已取消收藏', icon: 'success' });
            this.loadFavorites();
          } catch (error) {
            wx.showToast({ title: error.message || '操作失败', icon: 'none' });
          }
        }
      }
    });
  }
});
