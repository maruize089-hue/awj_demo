const { login, register, wxLogin } = require('../../utils/auth');

Page({
  data: {
    username: '',
    password: '',
    phone: '',
    isRegister: false,
    loading: false
  },

  onUsernameInput(e) {
    this.setData({ username: e.detail.value });
  },

  onPasswordInput(e) {
    this.setData({ password: e.detail.value });
  },

  onPhoneInput(e) {
    this.setData({ phone: e.detail.value });
  },

  onSwitchChange(e) {
    this.setData({ isRegister: e.detail.value });
  },

  async onSubmit() {
    const { username, password, phone, isRegister } = this.data;
    
    if (!username || !password) {
      wx.showToast({ title: '请填写用户名和密码', icon: 'none' });
      return;
    }

    this.setData({ loading: true });

    try {
      if (isRegister) {
        await register(username, password, phone);
        wx.showToast({ title: '注册成功，请登录', icon: 'success' });
        this.setData({ isRegister: false });
      } else {
        await login(username, password);
        wx.showToast({ title: '登录成功', icon: 'success' });
        wx.switchTab({ url: '/pages/index/index' });
      }
    } catch (error) {
      wx.showToast({ title: error.message || '操作失败', icon: 'none' });
    } finally {
      this.setData({ loading: false });
    }
  },

  async onWxLogin() {
    this.setData({ loading: true });
    try {
      await wxLogin();
      wx.showToast({ title: '登录成功', icon: 'success' });
      wx.switchTab({ url: '/pages/index/index' });
    } catch (error) {
      wx.showToast({ title: error.message || '登录失败', icon: 'none' });
    } finally {
      this.setData({ loading: false });
    }
  }
});
