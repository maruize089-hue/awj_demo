const formatDate = (date) => {
  if (!date) return '';
  const d = new Date(date);
  const year = d.getFullYear();
  const month = String(d.getMonth() + 1).padStart(2, '0');
  const day = String(d.getDate()).padStart(2, '0');
  const hour = String(d.getHours()).padStart(2, '0');
  const minute = String(d.getMinutes()).padStart(2, '0');
  return `${year}-${month}-${day} ${hour}:${minute}`;
};

const formatPrice = (price) => {
  if (!price) return '0.00';
  return parseFloat(price).toFixed(2);
};

const formatMoney = (price) => {
  if (!price) return '¥0.00';
  return '¥' + parseFloat(price).toFixed(2);
};

const debounce = (fn, delay) => {
  let timer = null;
  return function (...args) {
    if (timer) clearTimeout(timer);
    timer = setTimeout(() => {
      fn.apply(this, args);
    }, delay);
  };
};

const throttle = (fn, delay) => {
  let lastTime = 0;
  return function (...args) {
    const now = Date.now();
    if (now - lastTime >= delay) {
      fn.apply(this, args);
      lastTime = now;
    }
  };
};

const getStatusText = (status, orderType) => {
  const productStatusMap = {
    'PENDING': '待支付',
    'PAID': '已支付',
    'SHIPPED': '已发货',
    'RECEIVED': '已收货',
    'CANCELLED': '已取消'
  };
  
  const serviceStatusMap = {
    'PENDING': '待支付',
    'PAID': '已支付',
    'CONFIRMED': '商家确认',
    'IN_PROGRESS': '进行中',
    'FINISHED': '已完成',
    'CANCELLED': '已取消'
  };
  
  return orderType === 'PRODUCT' ? productStatusMap[status] : serviceStatusMap[status] || status;
};

const getCouponStatusText = (status) => {
  const map = {
    'AVAILABLE': '可使用',
    'USED': '已使用',
    'EXPIRED': '已过期'
  };
  return map[status] || status;
};

const getDistance = (lat1, lng1, lat2, lng2) => {
  const R = 6371;
  const dLat = (lat2 - lat1) * Math.PI / 180;
  const dLng = (lng2 - lng1) * Math.PI / 180;
  const a = Math.sin(dLat/2) * Math.sin(dLat/2) +
            Math.cos(lat1 * Math.PI / 180) * Math.cos(lat2 * Math.PI / 180) *
            Math.sin(dLng/2) * Math.sin(dLng/2);
  const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
  return (R * c).toFixed(2);
};

const generateOrderNo = () => {
  const date = new Date();
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  const hour = String(date.getHours()).padStart(2, '0');
  const minute = String(date.getMinutes()).padStart(2, '0');
  const second = String(date.getSeconds()).padStart(2, '0');
  const random = Math.floor(Math.random() * 10000).toString().padStart(4, '0');
  return `ORD${year}${month}${day}${hour}${minute}${second}${random}`;
};

module.exports = {
  formatDate,
  formatPrice,
  formatMoney,
  debounce,
  throttle,
  getStatusText,
  getCouponStatusText,
  getDistance,
  generateOrderNo
};
