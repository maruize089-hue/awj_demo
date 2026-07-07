const { get, post, delete: deleteReq } = require('../../utils/request');

Page({
  data: {
    cartItems: [],
    allSelected: false
  },

  onLoad() {
    this.loadCart();
  },

  onShow() {
    this.loadCart();
  },

  async loadCart() {
    try {
      const res = await get('/api/cart/list');
      this.setData({ cartItems: res.data || [] });
      this.updateSelectStatus();
    } catch (error) {
      console.error('加载购物车失败', error);
    }
  },

  updateSelectStatus() {
    const { cartItems } = this.data;
    if (cartItems.length === 0) {
      this.setData({ allSelected: false });
      return;
    }
    const allSelected = cartItems.every(item => item.selected);
    this.setData({ allSelected });
  },

  get allSelected() {
    return this.data.cartItems.every(item => item.selected);
  },

  get selectedCount() {
    return this.data.cartItems.filter(item => item.selected).length;
  },

  get totalPrice() {
    return this.data.cartItems
      .filter(item => item.selected)
      .reduce((sum, item) => sum + item.price * item.quantity, 0)
      .toFixed(2);
  },

  toggleSelect(e) {
    const id = e.currentTarget.dataset.id;
    const items = this.data.cartItems.map(item => {
      if (item.id === id) {
        item.selected = !item.selected;
      }
      return item;
    });
    this.setData({ cartItems: items });
    this.updateSelectStatus();
  },

  toggleSelectAll() {
    const allSelected = !this.allSelected;
    const items = this.data.cartItems.map(item => ({ ...item, selected: allSelected }));
    this.setData({ cartItems: items });
  },

  async increaseQty(e) {
    const id = e.currentTarget.dataset.id;
    try {
      await post('/api/cart/update', { id, quantity: 1 });
      this.loadCart();
    } catch (error) {
      console.error('更新数量失败', error);
    }
  },

  async decreaseQty(e) {
    const id = e.currentTarget.dataset.id;
    try {
      await post('/api/cart/update', { id, quantity: -1 });
      this.loadCart();
    } catch (error) {
      console.error('更新数量失败', error);
    }
  },

  async deleteItem(e) {
    const id = e.currentTarget.dataset.id;
    try {
      await deleteReq('/api/cart/delete/' + id);
      this.loadCart();
    } catch (error) {
      console.error('删除失败', error);
    }
  },

  submitOrder() {
    const selectedItems = this.data.cartItems.filter(item => item.selected);
    if (selectedItems.length === 0) {
      wx.showToast({ title: '请选择商品', icon: 'none' });
      return;
    }
    
    const itemIds = selectedItems.map(item => item.id).join(',');
    wx.navigateTo({ url: `/pages/order/create?itemIds=${itemIds}` });
  },

  goToDetail(e) {
    const id = e.currentTarget.dataset.id;
    const type = e.currentTarget.dataset.type;
    wx.navigateTo({ url: `/pages/detail/detail?id=${id}&type=${type}` });
  },

  goShopping() {
    wx.switchTab({ url: '/pages/index/index' });
  }
});
