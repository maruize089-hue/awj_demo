const BASE_URL = 'http://localhost:8080';

const filterNullParams = (data) => {
  const filtered = {};
  for (const key in data) {
    if (data[key] !== null && data[key] !== undefined && data[key] !== '') {
      filtered[key] = data[key];
    }
  }
  return filtered;
};

const request = (options) => {
  return new Promise((resolve, reject) => {
    const token = wx.getStorageSync('token');
    
    wx.request({
      url: BASE_URL + options.url,
      method: options.method || 'GET',
      data: filterNullParams(options.data || {}),
      header: {
        'Content-Type': 'application/json',
        'Authorization': token ? `Bearer ${token}` : '',
        ...options.header
      },
      timeout: options.timeout || 60000,
      success: (res) => {
        if (res.statusCode === 200) {
          const data = res.data;
          if (data.code === 200) {
            resolve(data);
          } else if (data.code === 401) {
            wx.removeStorageSync('token');
            wx.removeStorageSync('userInfo');
            wx.removeStorageSync('role');
            wx.navigateTo({ url: '/pages/login/login' });
            reject(data);
          } else {
            if (!options.silent) {
              wx.showToast({ title: data.message || '请求失败', icon: 'none' });
            }
            reject(data);
          }
        } else {
          if (!options.silent) {
            wx.showToast({ title: '网络错误', icon: 'none' });
          }
          reject(res);
        }
      },
      fail: (err) => {
        if (!options.silent) {
          wx.showToast({ title: '网络请求失败', icon: 'none' });
        }
        reject(err);
      }
    });
  });
};

const get = (url, data = {}, options = {}) => {
  return request({ url, method: 'GET', data, ...options });
};

const post = (url, data = {}, options = {}) => {
  return request({ url, method: 'POST', data, ...options });
};

const put = (url, data = {}, options = {}) => {
  return request({ url, method: 'PUT', data, ...options });
};

const del = (url, data = {}, options = {}) => {
  return request({ url, method: 'DELETE', data, ...options });
};

module.exports = {
  get,
  post,
  put,
  del,
  BASE_URL
};
