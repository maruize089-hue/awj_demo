const { get, post } = require('../../utils/request');

Page({
  data: {
    order: {},
    hasReview: false
  },

  onLoad(options) {
    this.loadOrder(options.id);
  },

  async loadOrder(id) {
    try {
      const res = await get('/api/order/' + id);
      this.setData({ order: res.data || {} });
      this.checkReview(id);
    } catch (error) {
      console.error('加载订单失败', error);
    }
  },

  async checkReview(orderId) {
    try {
      const res = await get('/api/review/check', { orderId });
      this.setData({ hasReview: res.data || false });
    } catch (error) {
      console.error('检查评价失败', error);
    }
  },

  async payOrder() {
    const orderId = this.data.order.id;
    try {
      await post('/api/order/pay', { orderId });
      wx.showToast({ title: '支付成功', icon: 'success' });
      this.loadOrder(orderId);
    } catch (error) {
      wx.showToast({ title: error.message || '支付失败', icon: 'none' });
    }
  },

  async confirmOrder() {
    const orderId = this.data.order.id;
    try {
      await post('/api/order/complete', { orderId });
      wx.showToast({ title: '确认完成', icon: 'success' });
      this.loadOrder(orderId);
    } catch (error) {
      wx.showToast({ title: error.message || '操作失败', icon: 'none' });
    }
  },

  writeReview() {
    const orderId = this.data.order.id;
    wx.navigateTo({ url: `/pages/review/create?orderId=${orderId}` });
  },

  async cancelOrder() {
    const orderId = this.data.order.id;
    
    wx.showModal({
      title: '确认取消',
      content: '确定要取消该订单吗？',
      success: async (res) => {
        if (res.confirm) {
          try {
            await post('/api/order/cancel', { orderId });
            wx.showToast({ title: '订单已取消', icon: 'success' });
            this.loadOrder(orderId);
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
