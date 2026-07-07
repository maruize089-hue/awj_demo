const { get, post } = require('../../utils/request');

Page({
  data: {
    orderItems: [],
    address: null,
    remark: '',
    serviceDate: '',
    serviceTime: '',
    coupon: null,
    hasService: false,
    subtotal: '0.00',
    shippingFee: '0.00',
    totalAmount: '0.00',
    loading: false
  },

  onLoad(options) {
    if (options.itemIds) {
      this.loadCartItems(options.itemIds);
    } else if (options.productId) {
      this.loadProductItem(options.productId, options.type, options.quantity);
    }
    this.loadAddresses();
  },

  async loadCartItems(itemIds) {
    try {
      const res = await get('/api/cart/items/' + itemIds);
      this.processItems(res.data || []);
    } catch (error) {
      console.error('加载购物车商品失败', error);
    }
  },

  async loadProductItem(productId, type, quantity) {
    try {
      const url = type === 'PRODUCT' ? `/api/product/${productId}` : `/api/service/${productId}`;
      const res = await get(url);
      const item = {
        id: Date.now(),
        productId: productId,
        type: type,
        name: res.data.name,
        price: res.data.price,
        image: res.data.image,
        quantity: parseInt(quantity) || 1
      };
      this.processItems([item]);
    } catch (error) {
      console.error('加载商品失败', error);
    }
  },

  processItems(items) {
    const hasService = items.some(item => item.type === 'SERVICE');
    const subtotal = items.reduce((sum, item) => sum + item.price * item.quantity, 0);
    const shippingFee = items.some(item => item.type === 'PRODUCT') ? 0 : 0;
    const totalAmount = subtotal + shippingFee;
    
    this.setData({
      orderItems: items,
      hasService: hasService,
      subtotal: subtotal.toFixed(2),
      shippingFee: shippingFee.toFixed(2),
      totalAmount: totalAmount.toFixed(2)
    });
  },

  async loadAddresses() {
    try {
      const res = await get('/api/address/list');
      const addresses = res.data || [];
      if (addresses.length > 0) {
        const defaultAddress = addresses.find(a => a.isDefault) || addresses[0];
        this.setData({ address: defaultAddress });
      }
    } catch (error) {
      console.error('加载地址失败', error);
    }
  },

  selectAddress() {
    wx.navigateTo({ url: '/pages/address/list?select=true' });
  },

  onRemarkInput(e) {
    this.setData({ remark: e.detail.value });
  },

  onDateChange(e) {
    this.setData({ serviceDate: e.detail.value });
  },

  onTimeChange(e) {
    this.setData({ serviceTime: e.detail.value });
  },

  selectCoupon() {
    wx.navigateTo({ url: '/pages/coupon/list?select=true' });
  },

  async submitOrder() {
    const { orderItems, address, remark, serviceDate, serviceTime, coupon } = this.data;
    
    if (!address) {
      wx.showToast({ title: '请选择收货地址', icon: 'none' });
      return;
    }
    
    if (this.data.hasService && (!serviceDate || !serviceTime)) {
      wx.showToast({ title: '请选择服务时间', icon: 'none' });
      return;
    }
    
    this.setData({ loading: true });
    
    try {
      const orderData = {
        items: orderItems.map(item => ({
          productId: item.productId,
          type: item.type,
          name: item.name,
          price: item.price,
          image: item.image,
          quantity: item.quantity
        })),
        addressId: address.id,
        remark: remark,
        serviceTime: serviceDate && serviceTime ? `${serviceDate} ${serviceTime}` : null,
        couponId: coupon && coupon.id ? coupon.id : null
      };
      
      const res = await post('/api/order/create', orderData);
      wx.showToast({ title: '订单创建成功', icon: 'success' });
      
      setTimeout(() => {
        wx.redirectTo({ url: `/pages/order/detail?id=${res.data.id}` });
      }, 1500);
    } catch (error) {
      wx.showToast({ title: error.message || '创建订单失败', icon: 'none' });
    } finally {
      this.setData({ loading: false });
    }
  }
});
