const login = async (username, password) => {
  const { post } = require('./request');
  try {
    const res = await post('/api/auth/login', { username, password });
    if (res.code === 200) {
      wx.setStorageSync('token', res.data.token);
      wx.setStorageSync('userInfo', res.data.userInfo);
      wx.setStorageSync('role', res.data.role);
      return res.data;
    }
    throw new Error(res.message);
  } catch (error) {
    throw error;
  }
};

const register = async (username, password, phone) => {
  const { post } = require('./request');
  try {
    const res = await post('/api/auth/register', { username, password, phone });
    if (res.code === 200) {
      return res.data;
    }
    throw new Error(res.message);
  } catch (error) {
    throw error;
  }
};

const wxLogin = async () => {
  const { post } = require('./request');
  try {
    const codeRes = await new Promise((resolve, reject) => {
      wx.login({
        success: resolve,
        fail: reject
      });
    });
    
    const res = await post('/api/auth/wx-login', { code: codeRes.code });
    if (res.code === 200) {
      wx.setStorageSync('token', res.data.token);
      wx.setStorageSync('userInfo', res.data.userInfo);
      wx.setStorageSync('role', res.data.role);
      return res.data;
    }
    throw new Error(res.message);
  } catch (error) {
    throw error;
  }
};

const logout = async () => {
  const { post } = require('./request');
  try {
    await post('/api/auth/logout');
    wx.removeStorageSync('token');
    wx.removeStorageSync('userInfo');
    wx.removeStorageSync('role');
  } catch (error) {
    wx.removeStorageSync('token');
    wx.removeStorageSync('userInfo');
    wx.removeStorageSync('role');
  }
};

const getUserInfo = async () => {
  const { get } = require('./request');
  try {
    const res = await get('/api/user/info');
    if (res.code === 200) {
      return res.data;
    }
    throw new Error(res.message);
  } catch (error) {
    throw error;
  }
};

const isLoggedIn = () => {
  return !!wx.getStorageSync('token');
};

const getRole = () => {
  return wx.getStorageSync('role');
};

module.exports = {
  login,
  register,
  wxLogin,
  logout,
  getUserInfo,
  isLoggedIn,
  getRole
};
