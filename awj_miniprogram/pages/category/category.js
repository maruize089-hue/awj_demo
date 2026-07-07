const { get } = require('../../utils/request');

Page({
  data: {
    keyword: '',
    activeTab: 'PRODUCT',
    activeCategory: null,
    categories: [],
    products: [],
    services: []
  },

  onLoad(options) {
    if (options.keyword) {
      this.setData({ keyword: options.keyword });
    }
    if (options.categoryId) {
      this.setData({ activeCategory: parseInt(options.categoryId) });
    }
    this.loadCategories();
    this.loadItems();
  },

  onShow() {
    this.loadItems();
  },

  async loadCategories() {
    try {
      const res = await get('/api/category/list');
      this.setData({ categories: res.data || [] });
    } catch (error) {
      console.error('加载分类失败', error);
    }
  },

  get currentCategories() {
    const { categories, activeTab } = this.data;
    return categories.filter(c => c.type === activeTab || !c.type);
  },

  async loadItems() {
    const { activeTab, activeCategory, keyword } = this.data;
    
    try {
      if (activeTab === 'PRODUCT') {
        const params = keyword ? { keyword } : {};
        if (activeCategory) {
          params.categoryId = activeCategory;
        }
        const res = await get('/api/product/list', params);
        let products = res.data || [];
        if (keyword) {
          products = products.filter(p => p.name.includes(keyword));
        }
        this.setData({ products });
      } else {
        const params = keyword ? { keyword } : {};
        if (activeCategory) {
          params.categoryId = activeCategory;
        }
        const res = await get('/api/service/list', params);
        let services = res.data || [];
        if (keyword) {
          services = services.filter(s => s.name.includes(keyword));
        }
        this.setData({ services });
      }
    } catch (error) {
      console.error('加载数据失败', error);
    }
  },

  switchTab(e) {
    const tab = e.currentTarget.dataset.tab;
    this.setData({ activeTab: tab, activeCategory: null });
    this.loadItems();
  },

  selectCategory(e) {
    const id = e.currentTarget.dataset.id;
    this.setData({ activeCategory: this.data.activeCategory === id ? null : id });
    this.loadItems();
  },

  onSearchInput(e) {
    this.setData({ keyword: e.detail.value });
  },

  onSearch() {
    this.loadItems();
  },

  goToDetail(e) {
    const id = e.currentTarget.dataset.id;
    const type = e.currentTarget.dataset.type;
    wx.navigateTo({ url: `/pages/detail/detail?id=${id}&type=${type}` });
  }
});
