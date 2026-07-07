const { get, delete: deleteReq } = require('../../utils/request');

Page({
  data: {
    addresses: [],
    selectMode: false
  },

  onLoad(options) {
    this.setData({ selectMode: options.select === 'true' });
    this.loadAddresses();
  },

  onShow() {
    this.loadAddresses();
  },

  async loadAddresses() {
    try {
      const res = await get('/api/address/list');
      this.setData({ addresses: res.data || [] });
    } catch (error) {
      console.error('加载地址失败', error);
    }
  },

  selectAddress(e) {
    const id = e.currentTarget.dataset.id;
    if (this.data.selectMode) {
      const address = this.data.addresses.find(a => a.id === id);
      if (address) {
        const pages = getCurrentPages();
        const prevPage = pages[pages.length - 2];
        prevPage.setData({ address });
        wx.navigateBack();
      }
    }
  },

  addAddress() {
    wx.navigateTo({ url: '/pages/address/edit' });
  },

  editAddress(e) {
    e.stopPropagation();
    const id = e.currentTarget.dataset.id;
    wx.navigateTo({ url: `/pages/address/edit?id=${id}` });
  },

  async deleteAddress(e) {
    e.stopPropagation();
    const id = e.currentTarget.dataset.id;
    
    wx.showModal({
      title: '确认删除',
      content: '确定要删除该地址吗？',
      success: async (res) => {
        if (res.confirm) {
          try {
            await deleteReq('/api/address/delete/' + id);
            wx.showToast({ title: '删除成功', icon: 'success' });
            this.loadAddresses();
          } catch (error) {
            wx.showToast({ title: error.message || '删除失败', icon: 'none' });
          }
        }
      }
    });
  }
});
