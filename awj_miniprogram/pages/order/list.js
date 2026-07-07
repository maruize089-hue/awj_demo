const { get, post } = require('../../utils/request');

Page({
  data: {
    activeTab: 'ALL',
    orders: []
  },

  onLoad(options) {
    if (options.status) {
      this.setData({ activeTab: options.status });
    }
    this.loadOrders();
  },

  onShow() {
    this.loadOrders();
  },

  async loadOrders() {
    try {
      const status = this.data.activeTab === 'ALL' ? '' : this.data.activeTab;
      const res = await get('/api/order/list', { status });
      this.setData({ orders: res.data.list || [] });
    } catch (error) {
      console.error('加载订单失败', error);
    }
  },

  switchTab(e) {
    const tab = e.currentTarget.dataset.tab;
    this.setData({ activeTab: tab });
    this.loadOrders();
  },

  goToDetail(e) {
    const id = e.currentTarget.dataset.id;
    wx.navigateTo({ url: `/pages/order/detail?id=${id}` });
  },

  async payOrder(e) {
    e.stopPropagation();
    const id = e.currentTarget.dataset.id;
    try {
      await post('/api/order/pay', { orderId: id });
      wx.showToast({ title: '支付成功', icon: 'success' });
      this.loadOrders();
    } catch (error) {
      wx.showToast({ title: error.message || '支付失败', icon: 'none' });
    }
  },

  async confirmOrder(e) {
    e.stopPropagation();
    const id = e.currentTarget.dataset.id;
    try {
      await post('/api/order/complete', { orderId: id });
      wx.showToast({ title: '确认完成', icon: 'success' });
      this.loadOrders();
    } catch (error) {
      wx.showToast({ title: error.message || '操作失败', icon: 'none' });
    }
  },

  writeReview(e) {
    e.stopPropagation();
    const id = e.currentTarget.dataset.id;
    wx.navigateTo({ url: `/pages/review/create?orderId=${id}` });
  },

  async cancelOrder(e) {
    e.stopPropagation();
    const id = e.currentTarget.dataset.id;
    
    wx.showModal({
      title: '确认取消',
      content: '确定要取消该订单吗？',
      success: async (res) => {
        if (res.confirm) {
          try {
            await post('/api/order/cancel', { orderId: id });
            wx.showToast({ title: '订单已取消', icon: 'success' });
            this.loadOrders();
          } catch (error) {
            wx.showToast({ title: error.message || '操作失败', icon: 'none' });
          }
        }
      }
    });
  },

  getStatusText(status) {
    const map = {
      'PENDING': '待支付',
      'PAID': '待服务',
      'COMPLETED': '已完成',
      'REFUNDED': '已退款',
      'CANCELLED': '已取消'
    };
    return map[status] || status;
  },

  getStatusColor(status) {
    const map = {
      'PENDING': '#ff6b35',
      'PAID': '#ffb385',
      'COMPLETED': '#66cc66',
      'REFUNDED': '#999999',
      'CANCELLED': '#999999'
    };
    return map[status] || '#333333';
  }
});
