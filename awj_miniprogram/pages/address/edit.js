const { get, post } = require('../../utils/request');

Page({
  data: {
    addressId: null,
    formData: {
      receiver: '',
      phone: '',
      province: '',
      city: '',
      district: '',
      detail: '',
      isDefault: false
    },
    region: [],
    regionText: '',
    loading: false
  },

  onLoad(options) {
    if (options.id) {
      this.setData({ addressId: parseInt(options.id) });
      this.loadAddress(options.id);
    }
  },

  async loadAddress(id) {
    try {
      const res = await get('/api/address/' + id);
      const data = res.data || {};
      this.setData({
        formData: {
          receiver: data.receiver || '',
          phone: data.phone || '',
          province: data.province || '',
          city: data.city || '',
          district: data.district || '',
          detail: data.detail || '',
          isDefault: data.isDefault || false
        },
        region: [data.province || '', data.city || '', data.district || ''],
        regionText: `${data.province || ''}${data.city || ''}${data.district || ''}`
      });
    } catch (error) {
      console.error('加载地址失败', error);
    }
  },

  onReceiverInput(e) {
    this.setData({ 'formData.receiver': e.detail.value });
  },

  onPhoneInput(e) {
    this.setData({ 'formData.phone': e.detail.value });
  },

  onRegionChange(e) {
    const region = e.detail.value;
    this.setData({
      region: region,
      regionText: region.join(''),
      'formData.province': region[0],
      'formData.city': region[1],
      'formData.district': region[2]
    });
  },

  onDetailInput(e) {
    this.setData({ 'formData.detail': e.detail.value });
  },

  onDefaultChange(e) {
    this.setData({ 'formData.isDefault': e.detail.value });
  },

  async submitForm() {
    const { formData, addressId } = this.data;
    
    if (!formData.receiver || !formData.phone || !formData.province || !formData.detail) {
      wx.showToast({ title: '请填写完整信息', icon: 'none' });
      return;
    }
    
    this.setData({ loading: true });
    
    try {
      if (addressId) {
        await post('/api/address/update', { ...formData, id: addressId });
      } else {
        await post('/api/address/create', formData);
      }
      
      wx.showToast({ title: '保存成功', icon: 'success' });
      setTimeout(() => {
        wx.navigateBack();
      }, 1500);
    } catch (error) {
      wx.showToast({ title: error.message || '保存失败', icon: 'none' });
    } finally {
      this.setData({ loading: false });
    }
  }
});
